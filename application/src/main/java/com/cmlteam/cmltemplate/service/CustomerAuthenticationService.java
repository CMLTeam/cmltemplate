package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.SecurityCustomer;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.repository.SecurityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerAuthenticationService {
  private final SecurityCustomerService securityCustomerService;
  private final SecurityCustomerRepository securityCustomerRepository;

  @Transactional
  public Object signUp(AuthenticationRequest request) {
    if (securityCustomerRepository.findByEmail(request.getEmail()).isPresent())
      throw new ConflictException("Customer with such email already exist");
    var entity =
        SecurityCustomer.builder()
            .email(request.getEmail())
            .password(securityCustomerService.encode(request.getPassword()))
            .build();
    var customer = securityCustomerRepository.save(entity);

    String shortToken = securityCustomerService.generateShort(customer.getId().toString());
    String longToken = securityCustomerService.generateLong(customer.getId().toString());
    return Map.of("id", customer.getId(), "shortToken", shortToken, "longToken", longToken);
  }

  public Object signIn(AuthenticationRequest request) {
    securityCustomerService.authenticate(request.getEmail(), request.getPassword());
    var customer =
        securityCustomerRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));

    String shortToken = securityCustomerService.generateShort(customer.getId().toString());
    String longToken = securityCustomerService.generateLong(customer.getId().toString());
    return Map.of("id", customer.getId(), "shortToken", shortToken, "longToken", longToken);
  }
}
