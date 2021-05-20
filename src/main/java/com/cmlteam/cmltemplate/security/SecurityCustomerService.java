package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.User;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.exceptions.UnauthorizedException;
import com.cmlteam.cmltemplate.repository.UserRepository;
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
  private final UserRepository userRepository;

  @Transactional
  public String register(String username, String password) {
    if (userRepository.findByEmail(username).isPresent())
      throw new ConflictException("Customer with such email already exist");
    var newUser = User.builder().email(username).password(passwordEncoder.encode(password)).build();
    var savedUser = userRepository.save(newUser);

    return jwtTokenProvider.generateToken(savedUser.getId().toString());
  }

  @Transactional
  public String generateToken(String username, String password) {
    authenticate(username, password);
    var customer =
        userRepository
            .findByEmail(username)
            .orElseThrow(() -> new NotFoundException("User doesn't exists"));

    return jwtTokenProvider.generateToken(customer.getId().toString());
  }

  @Transactional
  public void setPassword(Long id, String newPassword) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Not found user with id " + id));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  public User getCurrentCustomer() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return (User) authentication.getPrincipal();
    } catch (ClassCastException e) {
      throw new UnauthorizedException("Can't get current customer from context");
    }
  }

  private void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }
}
