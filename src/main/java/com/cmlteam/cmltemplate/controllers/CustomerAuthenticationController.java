package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.services.CustomerAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@ConditionalOnProperty("jwt.enabled")
public class CustomerAuthenticationController {
  private final CustomerAuthenticationService customerAuthenticationService;

  @PostMapping("sign-up")
  public Object signUp(@RequestBody @Valid AuthenticationRequest request) {
    return customerAuthenticationService.signUp(request);
  }

  @PostMapping("confirm-email/{id}")
  public void confirmEmail(@PathVariable Long id) {
    customerAuthenticationService.confirmEmail(id);
  }

  @PostMapping("sign-in")
  public Object signIn(@RequestBody @Valid AuthenticationRequest request) {
    return customerAuthenticationService.signIn(request);
  }

  @GetMapping("get-current")
  @PreAuthorize("hasRole('USER')")
  public Object getCurrent() {
    return customerAuthenticationService.getCurrentCustomer();
  }

  @PostMapping("change-password/{id}/{password}")
  public void setPassword(@PathVariable Long id, @PathVariable String password) {
    customerAuthenticationService.setPassword(id, password);
  }
}
