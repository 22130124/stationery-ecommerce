package com.tqk.stationeryecommercebackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Đọc các giá trị từ file application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Trích xuất claim cụ thể từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Tạo token mới cho user chỉ chứa thông tin cơ bản
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Tạo token mới với các claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Lấy role đầu tiên vì mỗi account chỉ có 1 role
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String role = "USER";

        if (!authorities.isEmpty()) {
            role = authorities.iterator().next().getAuthority();
        }

        // Lưu role vào claims
        extraClaims.put("role", role);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Hàm kiểm tra token có hợp lệ không bằng cách so khớp với UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Token hợp lệ nếu username khớp và token chưa hết hạn
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Hàm kiểm tra xem token đã hết hạn chưa
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Hàm lấy thời gian hết hạn từ token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Giải mã toàn bộ token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy signing key từ chuỗi secret
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        Object role = claims.get("role");
        return role != null ? role.toString() : "USER";
    }
}
