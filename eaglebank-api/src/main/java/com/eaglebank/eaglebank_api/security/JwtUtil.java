package com.eaglebank.eaglebank_api.security;

import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    
    public String generateToken(String userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("sub", email);
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
        claims.put("iss", "eaglebank-api");
        claims.put("aud", "eaglebank-api");
        claims.put("role", "user");
        // Simple JWT implementation for demo purposes
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = claims.toString();
        String headerEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
        String payloadEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(
            (headerEncoded + "." + payloadEncoded + "." + SECRET_KEY).getBytes()
        );
        return headerEncoded + "." + payloadEncoded + "." + signature;
    }
    
    public Boolean validateToken(String token, String email) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            if (!payload.contains(email)) return false;
            if (isTokenExpired(token)) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isTokenValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            if (isTokenExpired(token)) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String extractEmail(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Try sub= first, then email=
            String email = extractValueFromPayload(payload, "sub");
            if (email == null) {
                email = extractValueFromPayload(payload, "email");
            }
            return email;
        } catch (Exception e) {
            return null;
        }
    }
    
    public String extractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return extractValueFromPayload(payload, "userId");
        } catch (Exception e) {
            return null;
        }
    }

    // Helper to extract value from Map.toString() format: {key1=val1, key2=val2, ...}
    private String extractValueFromPayload(String payload, String key) {
        String search = key + "=";
        int start = payload.indexOf(search);
        if (start == -1) return null;
        start += search.length();
        int end = payload.indexOf(",", start);
        if (end == -1) end = payload.indexOf("}", start);
        if (end == -1) end = payload.length();
        return payload.substring(start, end).trim();
    }
    
    private Boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            String expStr = extractValueFromPayload(payload, "exp");
            if (expStr == null) return true;
            // Try to parse as long (timestamp), fallback to Date.toString()
            try {
                long expTime = Long.parseLong(expStr);
                return new Date().getTime() > expTime;
            } catch (NumberFormatException nfe) {
                // Try parsing as Date string
                try {
                    Date expDate = new Date(expStr);
                    return new Date().after(expDate);
                } catch (Exception e) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
    }
} 