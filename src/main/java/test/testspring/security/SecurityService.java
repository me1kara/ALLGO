package test.testspring.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
@Service
public class SecurityService {
    private static final String secret_key = "asdsabsabsdasqweddddddddddddddddddddddqweqeqweqweqweqweqwe";

    //createToken
    public String createToken(String subject, long expTime){
        if(expTime<=0){
            throw new RuntimeException("만료시간이 0보다 커야함");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(secret_key);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis()+expTime))
                .compact();
    }

    //토큰검증하는 메서드를 boolean
    public String getSubject(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret_key))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
