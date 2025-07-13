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
        claims.put("exp", new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        claims.put("iss", "eaglebank-api");
        claims.put("aud", "eaglebank-api");
        claims.put("role", "user");
        
        // Simple JWT implementation for demo purposes
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = claims.toString().replace("=", "\":\"").replace(", ", "\",\"").replace("{", "{\"").replace("}", "\"}");
        
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
            return payload.contains(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    public String extractEmail(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Simple extraction - in production use proper JSON parsing
            if (payload.contains("sub")) {
                int start = payload.indexOf("sub") + 5;
                int end = payload.indexOf("\"", start);
                return payload.substring(start, end);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public String extractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Simple extraction - in production use proper JSON parsing
            if (payload.contains("userId")) {
                int start = payload.indexOf("userId") + 8;
                int end = payload.indexOf("\"", start);
                return payload.substring(start, end);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private Boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // Simple expiration check - in production use proper JSON parsing
            if (payload.contains("exp")) {
                int start = payload.indexOf("exp") + 5;
                int end = payload.indexOf("\"", start);
                String expStr = payload.substring(start, end);
                long expTime = Long.parseLong(expStr);
                return new Date().getTime() > expTime;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
} 