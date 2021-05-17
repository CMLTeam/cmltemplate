package com.cmlteam.cmltemplate.controller;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.service.CustomerAuthenticationService;
import com.cmlteam.cmltemplate.service.SecurityCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerAuthenticationController {
  private final CustomerAuthenticationService customerAuthenticationService;
  private final SecurityCustomerService securityCustomerService;

  @GetMapping("get-current")
  @PreAuthorize("hasRole('CUSTOMER')")
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

  @PostMapping("change-password/{id}/{password}")
  public void setPassword(@PathVariable Long id, @PathVariable String password) {
    customerAuthenticationService.setPassword(id, password);
  }
}
