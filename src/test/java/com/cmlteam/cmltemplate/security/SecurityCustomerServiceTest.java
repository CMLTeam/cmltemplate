package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityCustomerServiceTest {
  public static final String SOME_EMAIL = "some@email";
  public static final String SOME_PASSWORD = "some-password";
  private final Customer SAMPLE_CUSTOMER = Customer.builder().id(-1L).build();
  @Mock AuthenticationManager authenticationManager;
  @Mock JwtTokenProvider jwtTokenProvider;
  @Mock PasswordEncoder passwordEncoder;
  @Mock CustomerRepository securityCustomerRepository;

  @InjectMocks SecurityCustomerService securityCustomerService;

  @Test
  void registerAlreadyExist() {
    when(securityCustomerRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_CUSTOMER));

    assertThrows(
        ConflictException.class, () -> securityCustomerService.register(SOME_EMAIL, SOME_PASSWORD));
  }

  @Test
  void registerSuccess() {
    when(securityCustomerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    when(securityCustomerRepository.save(any(Customer.class))).thenReturn(SAMPLE_CUSTOMER);
    when(passwordEncoder.encode(any(String.class))).thenReturn("encode password");
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("new token");

    String response = securityCustomerService.register(SOME_EMAIL, SOME_PASSWORD);
    assertEquals("new token", response);
  }

  @Test
  void generateTokenForInvalidCredentials() {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(InternalAuthenticationServiceException.class);

    assertThrows(
        InternalAuthenticationServiceException.class,
        () -> securityCustomerService.generateToken(SOME_EMAIL, SOME_PASSWORD));
  }

  @Test
  void generateTokenForNotExistCustomer() {
    when(securityCustomerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> securityCustomerService.generateToken(SOME_EMAIL, SOME_PASSWORD));
  }

  @Test
  void generateTokenSuccess() {
    when(securityCustomerRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_CUSTOMER));
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("new token");

    String response = securityCustomerService.generateToken(SOME_EMAIL, SOME_PASSWORD);
    assertEquals("new token", response);
  }

  @Test
  void setPasswordForNotExistCustomer() {
    when(securityCustomerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> securityCustomerService.setPassword(-2L, "new-pass"));
  }

  @Test
  void setPasswordSuccess() {
    when(securityCustomerRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(SAMPLE_CUSTOMER));

    assertDoesNotThrow(() -> securityCustomerService.setPassword(-2L, "new-pass"));
  }
}
