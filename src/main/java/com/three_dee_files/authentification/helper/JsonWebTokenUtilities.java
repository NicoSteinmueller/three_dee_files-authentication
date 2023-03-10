package com.three_dee_files.authentification.helper;

import com.three_dee_files.authentification.tables.Account;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class JsonWebTokenUtilities {

    private static Key SECRET_KEY;
    @Value("${app.jsonWebToken.secret}")
    protected void setSecretKey(String secret){
        SECRET_KEY = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
    }
    @Value("${app.jsonWebToken.duration}")
    long EXPIRE_DURATION;


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
