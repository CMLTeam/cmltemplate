package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityCustomerService {
  private final AuthenticationManager authenticationManager;
  // ToDo microservices
  private final JwtTokenProvider jwtTokenProvider;

  public void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  public String generateShort(String subject) {
    return jwtTokenProvider.generateShort(subject);
  }

  public String generateLong(String subject) {
    return jwtTokenProvider.generateLong(subject);
  }
}
