package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.exceptions.ForbiddenException;
import com.cmlteam.cmltemplate.repository.SecurityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
  private final SecurityCustomerRepository securityCustomerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return securityCustomerRepository
        .findByEmail(username)
        .orElseThrow(() -> new ForbiddenException("No customer with email " + username));
  }
}
