package com.skilluser.user.configuration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey key;

    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public JwtUtil() {
        // Use plain string as secret → must be at least 32 chars for HS256
        String secretString = "93494999932938497edvncf3r45495495u894y48y83y83y8y343434";
        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    //  Generate token with roles
    public String generateToken(Long userId, String email, List<String> roles,String username, Long contactNo, int collegeId, Long companyId, Long deptId) {
        return Jwts.builder()
                .claim("email",email)      // optional
                .claim("userId", userId)   // important!
                .claim("user_role", roles)
                .claim("name",username)
                .claim("collegeId", collegeId)
                .claim("companyId",companyId)
                .claim("contact_no",contactNo)
                .claim("deptId", deptId)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    //  Extract username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    // Extract roles
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("user_role", List.class);
    }

    //  Check if token expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //  Validate token (signature + expiry)
    public boolean validateToken(String token, Long expectedUserId) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = claims.get("userId", Long.class);
            Date expiration = claims.getExpiration();

            return (userId.equals(expectedUserId) && expiration.after(new Date()));
        } catch (JwtException e) {
            return false;
        }
    }
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims decodeToken(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                //  0.12.x API
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
