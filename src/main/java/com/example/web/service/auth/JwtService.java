package com.example.web.service.auth;

import com.example.web.dto.auth.MeResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

  private static final String SECRET_KEY =
      "mi-super-clave-secreta-para-jwt-1234567890-xxxx";

  private Key getKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Long idUsuario, String username, List<String> roles, boolean enabled) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + 1000L * 60 * 60); 

    return Jwts.builder()
        .setSubject(idUsuario.toString())
        .claim("username", username)
        .claim("roles", roles)
        .claim("enabled", enabled)
        .setIssuedAt(now)
        .setExpiration(exp)
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public MeResponse parseToken(String token) {
    try {
      Jws<Claims> jws = Jwts.parserBuilder()
          .setSigningKey(getKey())
          .build()
          .parseClaimsJws(token);

      Claims c = jws.getBody();

      Long idUsuario = Long.parseLong(c.getSubject());
      String username = c.get("username", String.class);
      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) c.get("roles", List.class);
      Boolean enabled = c.get("enabled", Boolean.class);

      return new MeResponse(
          idUsuario,
          username,
          roles != null ? roles : List.of(),
          enabled != null && enabled
      );
    } catch (JwtException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
    }
  }
}
