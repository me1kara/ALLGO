package test.testspring.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import test.testspring.domain.Member;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Service
public class SecurityService {
    private static final String secret_key = "asdsabsabsdasqweddddddddddddddddddddddqweqeqweqweqweqweqwe";

    //createToken
    public String createToken(String id, String role, long expTime) {
        if (expTime <= 0) {
            throw new IllegalArgumentException("만료시간이 0보다 커야합니다.");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = Base64.getEncoder().encode(secret_key.getBytes(StandardCharsets.UTF_8));
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .compact();
    }

    //토큰검증하는 메서드를 boolean
    public Member getMember(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(secret_key.getBytes(StandardCharsets.UTF_8)).getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Member.builder()
                .id(claims.getSubject())
                .role(claims.get("role", String.class))
                .build();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
