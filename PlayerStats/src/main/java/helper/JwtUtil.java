package helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.nio.charset.StandardCharsets;

public class JwtUtil {

    // Must match the secret used in the Frontend microservice
    private static final String SECRET = "nba_score_jwt_secret_key_2024_lab4_coe692";

    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates a JWT token string.
     * Returns the Claims if valid, or throws an exception if invalid/expired.
     */
    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    /**
     * Extracts the username (subject) from a valid token.
     */
    public static String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    /**
     * Extracts the role from a valid token.
     */
    public static String getRoleFromToken(String token) {
        return (String) validateToken(token).get("role");
    }
}
