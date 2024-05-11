package com.example.sources.utils.jwt;

import com.example.sources.domain.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${secretKey}")
    private String jwtSecret;
    @Value("${expirationMs}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication){
        Account account = (Account) authentication.getPrincipal();

        return Jwts.builder().setSubject((account.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateJwtToken(String jwt){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        }
        catch (MalformedJwtException e){
            System.err.println(e.getMessage());
        }
        catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }

        return false;
    }

    public String getUsername(String jwt){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }
}
