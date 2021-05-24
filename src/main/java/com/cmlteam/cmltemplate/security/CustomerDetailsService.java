package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.exceptions.ForbiddenException;
import com.cmlteam.cmltemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty("jwt.enabled")
class CustomerDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository
        .findByEmail(username)
        .orElseThrow(() -> new ForbiddenException("No user with email " + username));
  }
}
