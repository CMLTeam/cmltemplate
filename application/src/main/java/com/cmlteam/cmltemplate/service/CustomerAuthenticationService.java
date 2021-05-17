package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerAuthenticationService {
  private final SecurityCustomerService securityCustomerService;

  public Object signUp(AuthenticationRequest request) {
    return securityCustomerService.register(request);
  }

  public Object signIn(AuthenticationRequest request) {
    return securityCustomerService.generateToken(request);
  }

  public void setPassword(Long id, String newPassword) {
    securityCustomerService.setPassword(id, newPassword);
  }
}
