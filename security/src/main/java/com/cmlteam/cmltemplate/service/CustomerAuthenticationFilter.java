package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerAuthenticationFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomerRepository customerRepository;

  public Optional<Long> getAuthId(String jwtShort, String jwtLong) {
    if (StringUtils.hasText(jwtLong)) {
      return getAuthId(jwtLong);
    }
    return getAuthId(jwtShort);
  }

  private Optional<Long> getAuthId(String jwt) {
    if (jwtTokenProvider.isValid(jwt)) {
      return fetchUserDetailsId(jwt);
    }
    return Optional.empty();
  }

  private Optional<Long> fetchUserDetailsId(String jwt) {
    var id = Long.valueOf(jwtTokenProvider.getSubject(jwt));
    if (loadCustomerById(id).isPresent()) {
      return Optional.of(id);
    } else {
      return Optional.empty();
    }
  }

  private Optional<Customer> loadCustomerById(Long id) {
    return customerRepository.findById(id).filter(Customer::isEnabled);
  }
}
