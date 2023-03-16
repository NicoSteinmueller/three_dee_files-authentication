package com.three_dee_files.authentification.controllers;

import com.three_dee_files.authentification.helper.HashUtilities;
import com.three_dee_files.authentification.helper.JsonWebTokenUtilities;
import com.three_dee_files.authentification.helper.TotpUtilities;
import com.three_dee_files.authentification.repositorys.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JsonWebTokenUtilities jsonWebTokenUtilities;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password ,@RequestParam Optional<String> otp){
        byte[] passwordHash =  HashUtilities.hashSHA512(password);
        if (accountRepository.existsAccountByEmailAndPasswordHash(email,passwordHash)){
            var account = accountRepository.getAccountByEmailAndPasswordHash(email, passwordHash);
            if (account.getTotpSecret() != null)
                if (otp.isEmpty() || !TotpUtilities.validate(account.getTotpSecret(),otp.get()))
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("OTP is wrong or missing!");
            var token = jsonWebTokenUtilities.generateToken(accountRepository.getAccountByEmailAndPasswordHash(email, passwordHash));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email or password is wrong!");
    }

    @PostMapping("/validateToken")
    public ResponseEntity<HttpStatus> validateToken(@RequestParam String token){
        if (jsonWebTokenUtilities.isTokenValid(token))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);
        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }
}
