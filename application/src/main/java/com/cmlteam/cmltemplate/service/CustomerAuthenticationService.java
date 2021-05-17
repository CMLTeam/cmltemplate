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
    return securityCustomerService.signUp(request);
  }

  public Object signIn(AuthenticationRequest request) {
    return securityCustomerService.signIn(request);
  }
}
