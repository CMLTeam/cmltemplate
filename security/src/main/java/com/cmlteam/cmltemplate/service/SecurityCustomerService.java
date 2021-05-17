package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.entities.SecurityCustomer;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.exceptions.UnauthorizedException;
import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.model.response.AuthenticationResponse;
import com.cmlteam.cmltemplate.repository.SecurityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityCustomerService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final SecurityCustomerRepository securityCustomerRepository;

  @Transactional
  public AuthenticationResponse register(AuthenticationRequest request) {
    if (securityCustomerRepository.findByEmail(request.getEmail()).isPresent())
      throw new ConflictException("Customer with such email already exist");
    var entity =
        SecurityCustomer.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();
    var customer = securityCustomerRepository.save(entity);

    return getAuthenticationResponse(customer);
  }

  @Transactional
  public AuthenticationResponse generateToken(AuthenticationRequest request) {
    authenticate(request.getEmail(), request.getPassword());
    var customer =
        securityCustomerRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));

    return getAuthenticationResponse(customer);
  }

  public Customer getCurrentCustomer() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return (Customer) authentication.getPrincipal();
    } catch (ClassCastException e) {
      throw new UnauthorizedException("Can't get current customer from context");
    }
  }

  private void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  private AuthenticationResponse getAuthenticationResponse(SecurityCustomer customer) {
    String shortToken = jwtTokenProvider.generateShort(customer.getId().toString());
    String longToken = jwtTokenProvider.generateLong(customer.getId().toString());
    return AuthenticationResponse.builder().shortToken(shortToken).longToken(longToken).build();
  }

  public void setPassword(Long id, String newPassword) {
    SecurityCustomer customer =
        securityCustomerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Not found user with id " + id));
    customer.setPassword(passwordEncoder.encode(newPassword));
    securityCustomerRepository.save(customer);
  }
}
