package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.services.CustomerAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerAuthenticationController {
  private final CustomerAuthenticationService customerAuthenticationService;

  @PostMapping("sign-up")
  public Object signUp(@RequestBody AuthenticationRequest request) {
    return customerAuthenticationService.signUp(request);
  }

  @PostMapping("sign-in")
  public Object signIn(@RequestBody AuthenticationRequest request) {
    return customerAuthenticationService.signIn(request);
  }
}
