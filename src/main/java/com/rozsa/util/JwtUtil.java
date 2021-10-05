package com.rozsa.util;

import com.rozsa.repository.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {
    @Value("${token.expiration.time.default}")
    private long tokenExpirationInMinutes;

    @Value("${token.expiration.time.service}")
    private long serviceTokenExpirationInMinutes;

    @Value("${token.secret}")
    private String secretKey;

    @Value("${token.source.header}")
    private boolean headerTokenEnabled;

    @Value("${token.source.cookie}")
    private boolean cookieTokenEnabled;

    @Value("${token.source.cookie-name}")
    private String cookieTokenName;

    public boolean isCookieTokenEnabled() {
        return cookieTokenEnabled;
    }

    public boolean isHeaderTokenEnabled() {
        return headerTokenEnabled;
    }

    public String getCookieTokenName() {
        return cookieTokenName;
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.info("Failed to extract token from string \"{}\"", token);
        }

        return null;
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            log.info("Failed to extract claim from string \"{}\"", token);
        }

        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenNotExpired(String token) {
        return !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails, boolean isService) {
        Map<String, Object> claims = new HashMap<>();
        final long expirationTime = isService ? serviceTokenExpirationInMinutes : tokenExpirationInMinutes;
        return createToken(claims, userDetails.getUsername(), expirationTime);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Boolean validateToken(String token, User user) {
        final String username = extractUsername(token);

        if (!username.equals(user.getLogin())) {
            return false;
        }

        return isTokenNotExpired(token);
    }
}
