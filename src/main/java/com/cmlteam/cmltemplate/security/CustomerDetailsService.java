package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return customerRepository
        .findByEmail(username)
        .orElseThrow(() -> new NotFoundException("No customer with username" + username));
  }

  public Optional<Customer> loadCustomerById(Long id) {
    return customerRepository.findById(id).filter(Customer::isEnabled);
  }
}
