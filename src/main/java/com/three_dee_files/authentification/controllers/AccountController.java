package com.three_dee_files.authentification.controllers;

import com.three_dee_files.authentification.helper.EmailChecker;
import com.three_dee_files.authentification.helper.HashUtilities;
import com.three_dee_files.authentification.helper.KeyChecker;
import com.three_dee_files.authentification.repositorys.AccountRepository;
import com.three_dee_files.authentification.tables.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addUser(@RequestParam String key ,@RequestParam String email, @RequestParam String password){
        if (!keyChecker.isValidBackendKey(key))
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
        if (!keyChecker.isValidBackendKey(key))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!accountRepository.existsAccountByEmail(email))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        accountRepository.deleteAccountByEmail(email);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
