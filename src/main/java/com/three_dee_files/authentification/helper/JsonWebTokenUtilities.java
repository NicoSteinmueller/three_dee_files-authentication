package com.three_dee_files.authentification.helper;

import com.three_dee_files.authentification.tables.Account;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JsonWebTokenUtilities {

    @Value("${app.jsonWebToken.secret}")
    private static Key SECRET_KEY;

    @Value("${app.jsonWebToken.duration}")
    private static long EXPIRE_DURATION;


    public String generateToken(Account account){
        return Jwts.builder()
                .setSubject(account.getId()+ account.getEmail())
                .setIssuer("ThreeDeeFilesAuthenticationService")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_DURATION))
                .signWith(SECRET_KEY)
                .compact();
    }

}
