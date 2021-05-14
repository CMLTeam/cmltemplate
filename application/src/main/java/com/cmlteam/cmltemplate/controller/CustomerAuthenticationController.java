package com.cmlteam.cmltemplate.controller;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.service.CustomerAuthenticationService;
import com.cmlteam.cmltemplate.service.SecurityCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerAuthenticationController {
  private final CustomerAuthenticationService customerAuthenticationService;
  private final SecurityCustomerService securityCustomerService;

  @GetMapping("get-current")
  public Object getCurrent() {
    return securityCustomerService.getCurrentCustomer();
  }

  @PostMapping("sign-up")
  public Object signUp(@RequestBody AuthenticationRequest request) {
    return customerAuthenticationService.signUp(request);
  }

  @PostMapping("sign-in")
  public Object signIn(@RequestBody AuthenticationRequest request) {
    return customerAuthenticationService.signIn(request);
  }
}
