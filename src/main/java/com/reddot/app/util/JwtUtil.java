package com.reddot.app.util;

import com.reddot.app.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

// TODO: Implement this class
// TODO: IMPROVE THIS CLASS WITH TRY-CATCH BLOCKS
@Log4j2
@Component
public class JwtUtil {

    static final long JWT_EXPIRATION = 1000 * 60 * 60 * 10;
    static final long seconds = 2 * 60;

    // Generate a test key suitable for HMAC
    MacAlgorithm alg = Jwts.SIG.HS512; // or HS256, HS384
    SecretKey key;

    public JwtUtil() {
        // to save this secret key, you can encode it to base64
        // TODO: encrypt secret string (key) before saving it to database
        // JJWT has provided builder class to generate sufficiently secure key
        // using the JCA default provider's KeyGenerator under the hood
        this.key = alg.key().build();
        String encoded = Encoders.BASE64.encode(key.getEncoded());

        log.warn("DO NOT LOG SECRET KEY IN PRODUCTION");
        log.info("encoded:{}", encoded);
    }

    public String extractUsername(String jws) {
        return extractClaim(jws, Claims::getSubject);
    }

    public Integer extractUserId(String jws) {
        return extractClaim(jws, claims -> claims.get("userId", Integer.class));
    }

    private <T> T extractClaim(String jws, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jws);
        return claimsResolver.apply(claims);
    }
    // TODO: properly configure this method match with createToken method if token is signed or encrypted

    private Claims extractAllClaims(String jws) {
        try {
            // Read (Parse) JWS
            log.info("Extracting all claims from JWS");
            return (Claims) Jwts.parser()
                    .verifyWith(key)    // call verifyWith or decryptWith methods if you want to parse signed or encrypted JWTs
                    .clockSkewSeconds(seconds)  // set the clock skew to 2 minutes
                    .build()
                    .parse(jws).getPayload();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token");
        }
    }

    private Boolean isTokenExpired(String jws) {
        return extractExpiration(jws).before(new Date());
    }

    private Date extractExpiration(String jws) {
        return extractClaim(jws, Claims::getExpiration);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        // add additional claims to the JWT
        claims.put("iss", "reddot");
        claims.put("sub", user.getUsername());
        claims.put("aud", "end-user");
        claims.put("userId", user.getId());
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        try {
            // Create compact JWS
            // convert claim maps to LinkedHashMap to keep the order of the claims
            HashMap<String, Object> c = new LinkedHashMap<>(claims);
            c.put("iat", new Date());
            c.put("exp", new Date(System.currentTimeMillis() + JWT_EXPIRATION));
            // create JWS
            return Jwts.builder().
                    signWith(key, alg)
                    .claims(c)
                    .compact();
            // TODO: to compress the JWT to reduce its size.
            // https://github.com/jwtk/jjwt?tab=readme-ov-file#compression
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateToken(String jws, UserDetails userDetails) {
        final String username = extractUsername(jws);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jws));
    }
}