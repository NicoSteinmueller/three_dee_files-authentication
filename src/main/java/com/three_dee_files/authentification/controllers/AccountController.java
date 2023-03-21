package com.three_dee_files.authentification.controllers;

import com.three_dee_files.authentification.helper.*;
import com.three_dee_files.authentification.repositorys.AccountRepository;
import com.three_dee_files.authentification.repositorys.BackupCodeRepository;
import com.three_dee_files.authentification.repositorys.TempTotpSecretRepository;
import com.three_dee_files.authentification.tables.Account;
import com.three_dee_files.authentification.tables.TempTotpSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private KeyChecker keyChecker;

    @Autowired
    private JsonWebTokenUtilities jsonWebTokenUtilities;

    @Autowired
    private TempTotpSecretRepository tempTotpSecretRepository;

    @Autowired
    private BackupCodeRepository backupCodeRepository;

    @Autowired
    private TotpUtilities totpUtilities;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addUser(@RequestParam String key ,@RequestParam String email, @RequestParam String password){
        if (keyChecker.isInvalidBackendKey(key))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (accountRepository.existsAccountByEmail(email))
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        //TODO add password rules
        if (EmailChecker.isValidEmailAddress(email) && !password.isEmpty()) {
            Account user = new Account(email, HashUtilities.hashSHA512(password));
            accountRepository.saveAndFlush(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpStatus> removeUser(@RequestParam String key, @RequestParam String email){
        if (keyChecker.isInvalidBackendKey(key))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!accountRepository.existsAccountByEmail(email))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        accountRepository.deleteAccountByEmail(email);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/addTOTP")
    @Transactional
    public ResponseEntity<String> addTOTP(@RequestParam String token){
        if (!jsonWebTokenUtilities.isTokenValid(token))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var account = accountRepository.getAccountByEmail(jsonWebTokenUtilities.getEmail(token));


        if (account.getTotpSecret() != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        String secret = TotpUtilities.generateNewSecret();

        if (tempTotpSecretRepository.existsByAccount(account))
            tempTotpSecretRepository.deleteByAccount(account);

        TempTotpSecret tempTotpSecret = new TempTotpSecret(account,secret);
        tempTotpSecretRepository.saveAndFlush(tempTotpSecret);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("secret: "+secret);

    }

    @PostMapping("/verifyTOTP")
    @Transactional
    public ResponseEntity<String> verifyTOTP(@RequestParam String token, @RequestParam String otp){
        if (!jsonWebTokenUtilities.isTokenValid(token))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var account = accountRepository.getAccountByEmail(jsonWebTokenUtilities.getEmail(token));
        if (!tempTotpSecretRepository.existsByAccount(account))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        TempTotpSecret tempTotpSecret = tempTotpSecretRepository.getTempTotpSecretByAccount(account);

        if (!TotpUtilities.validate(tempTotpSecret.getTotpSecret(), otp))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        account.setTotpSecret(tempTotpSecret.getTotpSecret());
        accountRepository.saveAndFlush(account);

        tempTotpSecretRepository.deleteByAccount(account);

        var list = totpUtilities.generateBackupCodes(account);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(list.toString());
    }

    @PostMapping("/removeTOTP")
    @Transactional
    public ResponseEntity<String> removeTOTP(@RequestParam String token, @RequestParam String otp){
        if (!jsonWebTokenUtilities.isTokenValid(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid.");

        var account = accountRepository.getAccountByEmail(jsonWebTokenUtilities.getEmail(token));

        if (!totpUtilities.validate(account, otp))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP invalid.");

        backupCodeRepository.deleteByAccount(account);
        account.setTotpSecret(null);
        accountRepository.saveAndFlush(account);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
