package com.three_dee_files.authentification.controllers;

import com.three_dee_files.authentification.helper.EmailChecker;
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
    private AccountRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addUser(@RequestParam String email, @RequestParam String passwordHash){
        if (!userRepository.findUsersByEmail(email).isEmpty())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        if (EmailChecker.isValidEmailAddress(email) && passwordHash.length()==512) {
            Account user = new Account(email, passwordHash);
            userRepository.saveAndFlush(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
