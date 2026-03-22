package helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // Shared secret — must match the one in SearchGames and PlayerStats microservices
    private static final String SECRET         = "nba_score_jwt_secret_key_2024_lab4_coe692";
    private static final long   EXPIRY_MS      = 1000L * 60 * 60 * 2; // 2 hours

    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a signed JWT for an authenticated user.
     * Includes username (subject) and role as a custom claim.
     */
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                   .setSubject(username)
                   .claim("role", role)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
                   .signWith(getKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    /**
     * Validates a token and returns its claims.
     * Throws an exception if invalid or expired.
     */
    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public static String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public static String getRoleFromToken(String token) {
        return (String) validateToken(token).get("role");
    }
}
