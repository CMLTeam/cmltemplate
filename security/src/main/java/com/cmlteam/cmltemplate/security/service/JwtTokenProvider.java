package com.cmlteam.cmltemplate.security.service;

import com.cmlteam.cmltemplate.security.property.JwtProperty;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
  private final JwtProperty jwtProperty;

  public String generateShort(String subject) {
    var now = new Date();
    var expiryDate = new Date(now.getTime() + jwtProperty.getJwtExpirationInMs());
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtProperty.getJwtSecret())
        .compact();
  }

  public String generateLong(String subject) {
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS512, jwtProperty.getJwtSecret())
        .compact();
  }

  public String getSubject(String token) {
    var claims =
        Jwts.parser().setSigningKey(jwtProperty.getJwtSecret()).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean isValid(String token) {
    if (!StringUtils.hasText(token)) return false;
    try {
      Jwts.parser().setSigningKey(jwtProperty.getJwtSecret()).parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    }
    return false;
  }
}
