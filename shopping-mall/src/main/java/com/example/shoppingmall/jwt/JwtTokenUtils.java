package com.example.shoppingmall.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;

@Slf4j
@Component
// 토큰을 발급 받기 위한 빈으로 등록
public class JwtTokenUtils {
    private final Key singningKey;
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        this.singningKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.singningKey).build();

    }

    // jwt로 변환하여 발급하는 메서드
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24 * 7 * 2)));

        jwtClaims.put("authorities", userDetails.getAuthorities());

        log.info("토큰 정보: {}", jwtClaims);
        // jwt 발급
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.singningKey)
                .compact();
    }

    // 발급받은 jwt가 정상적인 jwt인지 확인하는 메서드
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("유효하지 않은 JWT입니다.");
        }
        return false;
    }

    // 실제 데이터 payload 부분을 회수하는 메서드
    // TODO 실제 어떻게 돌아오는지 보기
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
