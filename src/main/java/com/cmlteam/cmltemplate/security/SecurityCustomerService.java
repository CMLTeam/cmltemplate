package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.exceptions.UnauthorizedException;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
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
  private final CustomerRepository customerRepository;

  @Transactional
  public String register(String username, String password) {
    if (customerRepository.findByEmail(username).isPresent())
      throw new ConflictException("Customer with such email already exist");
    var entity = Customer.builder().email(username).password(passwordEncoder.encode(password)).build();
    var customer = customerRepository.save(entity);

    return generateToken(customer);
  }

  @Transactional
  public String generateToken(String username, String password) {
    authenticate(username, password);
    var customer =
        customerRepository
            .findByEmail(username)
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));

    return generateToken(customer);
  }

  @Transactional
  public void setPassword(Long id, String newPassword) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Not found user with id " + id));
    customer.setPassword(passwordEncoder.encode(newPassword));
    customerRepository.save(customer);
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

  private String generateToken(Customer customer) {
    return jwtTokenProvider.generateToken(customer.getId().toString());
  }
}
