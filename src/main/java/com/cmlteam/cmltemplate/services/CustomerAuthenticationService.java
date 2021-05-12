package com.cmlteam.cmltemplate.services;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import com.cmlteam.cmltemplate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerAuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final CustomerRepository customerRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public Object signUp(AuthenticationRequest request) {
    var entity = new Customer();
    entity.setEmail(request.getEmail());
    entity.setPassword(passwordEncoder.encode(request.getPassword()));
    var customer = customerRepository.save(entity);
    String token = jwtTokenProvider.generateShort(customer.getId().toString());
    return Map.of("id", customer.getId(), "token", token);
  }

  public Object signIn(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var customer =
        customerRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));
    String token = jwtTokenProvider.generateShort(customer.getId().toString());
    return Map.of("id", customer.getId(), "token", token);
  }
}
