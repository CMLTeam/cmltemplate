package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerAuthenticationService {
  private final SecurityCustomerService securityCustomerService;
  private final CustomerRepository customerRepository;

  public Object signUp(AuthenticationRequest request) {
    var entity =
        Customer.builder()
            .email(request.getEmail())
            .password(securityCustomerService.encode(request.getPassword()))
            .build();
    var customer = customerRepository.save(entity);

    String shortToken = securityCustomerService.generateShort(customer.getId().toString());
    String longToken = securityCustomerService.generateLong(customer.getId().toString());
    return Map.of("id", customer.getId(), "shortToken", shortToken, "longToken", longToken);
  }

  public Object signIn(AuthenticationRequest request) {
    securityCustomerService.authenticate(request.getEmail(), request.getPassword());
    var customer =
        customerRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));

    String shortToken = securityCustomerService.generateShort(customer.getId().toString());
    String longToken = securityCustomerService.generateLong(customer.getId().toString());
    return Map.of("id", customer.getId(), "shortToken", shortToken, "longToken", longToken);
  }
}
