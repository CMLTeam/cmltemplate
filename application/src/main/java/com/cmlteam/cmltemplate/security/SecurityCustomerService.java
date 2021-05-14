package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityCustomerService {
  private final AuthenticationManager authenticationManager;
  // ToDo microservices
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  public String generateShort(String subject) {
    return jwtTokenProvider.generateShort(subject);
  }

  public String generateLong(String subject) {
    return jwtTokenProvider.generateLong(subject);
  }
}
