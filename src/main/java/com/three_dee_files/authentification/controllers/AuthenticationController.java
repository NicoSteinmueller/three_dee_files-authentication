package com.three_dee_files.authentification.controllers;

import com.three_dee_files.authentification.helper.HashUtilities;
import com.three_dee_files.authentification.helper.JsonWebTokenUtilities;
import com.three_dee_files.authentification.repositorys.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JsonWebTokenUtilities jsonWebTokenUtilities;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password){
        byte[] passwordHash =  HashUtilities.hashSHA512(password);
        if (accountRepository.existsAccountByEmailAndPasswordHash(email,passwordHash)){
            var token = jsonWebTokenUtilities.generateToken(accountRepository.getAccountByEmailAndPasswordHash(email, passwordHash));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PostMapping("/validateToken")
    public ResponseEntity<HttpStatus> validateToken(@RequestParam String token){
        if (jsonWebTokenUtilities.isTokenValid(token))
            return new ResponseEntity<> (HttpStatus.ACCEPTED);
        return new ResponseEntity<> (HttpStatus.NOT_ACCEPTABLE);
    }
}
