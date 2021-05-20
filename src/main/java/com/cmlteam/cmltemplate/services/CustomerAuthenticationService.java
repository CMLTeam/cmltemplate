package com.cmlteam.cmltemplate.services;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.model.response.AuthenticationResponse;
import com.cmlteam.cmltemplate.security.SecurityCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerAuthenticationService {
  private final SecurityCustomerService securityCustomerService;

  public Object signUp(AuthenticationRequest request) {
    return AuthenticationResponse.builder()
        .token(securityCustomerService.register(request.getEmail(), request.getPassword()))
        .build();
  }

  public Object signIn(AuthenticationRequest request) {
    return AuthenticationResponse.builder()
        .token(securityCustomerService.generateToken(request.getEmail(), request.getPassword()))
        .build();
  }

  public void setPassword(Long id, String newPassword) {
    securityCustomerService.setPassword(id, newPassword);
  }

  public void confirmEmail(Long id) {
    securityCustomerService.confirmEmail(id);
  }

  public Object getCurrentCustomer() {
    return securityCustomerService.getCurrentCustomer();
  }
}
