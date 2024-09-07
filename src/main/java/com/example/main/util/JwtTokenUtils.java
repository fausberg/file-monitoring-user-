package com.example.main.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.lifetime}")
  private Duration jwtLifetime;

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    List<String> role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    claims.put("role", role);

    Date issueDate = new Date();
    Date expiredDate = new Date(issueDate.getTime() + jwtLifetime.toMillis());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(issueDate)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public String getUsername(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public List<String> getRoles(String token) {
    return getAllClaimsFromToken(token).get("role", List.class);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token).getBody();
  }
}
