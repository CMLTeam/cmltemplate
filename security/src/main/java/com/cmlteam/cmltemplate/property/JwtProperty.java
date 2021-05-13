package com.cmlteam.cmltemplate.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperty {
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expirationInMs}")
  private int jwtExpirationInMs;
}
