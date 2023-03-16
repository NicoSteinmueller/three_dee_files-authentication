package com.three_dee_files.authentification.helper;

import com.three_dee_files.authentification.repositorys.AccountRepository;
import com.three_dee_files.authentification.tables.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${app.jsonWebToken.issuer}")
    private String ISSUER;

    @Autowired
    private AccountRepository accountRepository;

    public String generateToken(Account account){
        return Jwts.builder()
                .setSubject(account.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_DURATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token){
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            long currentTime = System.currentTimeMillis();

            if (currentTime < claims.getIssuedAt().getTime() || currentTime > claims.getExpiration().getTime())
                return false;

            return accountRepository.existsAccountByEmail(claims.getSubject());
        }catch (Exception ignored) {
            return false;
        }
    }

    public String getEmail(String token){
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return "";
        }
    }

}
