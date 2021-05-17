package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityCustomerService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  public Customer getCurrentCustomer() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return (Customer) authentication.getPrincipal();
    } catch (ClassCastException e) {
      throw new UnauthorizedException("Can't get current customer from context");
    }
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
