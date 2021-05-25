package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.User;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.repository.UserRepository;
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
class SecurityUserServiceTest {
  public static final String SOME_EMAIL = "some@email";
  public static final String SOME_PASSWORD = "some-password";
  private final User SAMPLE_User = User.builder().id(-1L).build();
  @Mock AuthenticationManager authenticationManager;
  @Mock JwtTokenProvider jwtTokenProvider;
  @Mock PasswordEncoder passwordEncoder;
  @Mock UserRepository securityUserRepository;

  @InjectMocks SecurityCustomerService securityCustomerService;

  @Test
  void registerAlreadyExist() {
    when(securityUserRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_User));

    assertThrows(
        ConflictException.class, () -> securityCustomerService.register(SOME_EMAIL, SOME_PASSWORD));
  }

  @Test
  void registerSuccess() {
    when(securityUserRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    when(securityUserRepository.save(any(User.class))).thenReturn(SAMPLE_User);
    when(passwordEncoder.encode(any(String.class))).thenReturn("encode password");
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("new token");

    String response = securityCustomerService.register(SOME_EMAIL, SOME_PASSWORD);
    assertEquals("Bearer new token", response);
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
    when(securityUserRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> securityCustomerService.generateToken(SOME_EMAIL, SOME_PASSWORD));
  }

  @Test
  void generateTokenSuccess() {
    when(securityUserRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_User));
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("new token");

    String response = securityCustomerService.generateToken(SOME_EMAIL, SOME_PASSWORD);
    assertEquals("Bearer new token", response);
  }

  @Test
  void setPasswordForNotExistCustomer() {
    when(securityUserRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> securityCustomerService.setPassword(-2L, "new-pass"));
  }

  @Test
  void setPasswordSuccess() {
    when(securityUserRepository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_User));

    assertDoesNotThrow(() -> securityCustomerService.setPassword(-2L, "new-pass"));
  }

  @Test
  void confirmEmailForNotExistCustomer() {
    when(securityUserRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> securityCustomerService.confirmEmail(-2L));
  }

  @Test
  void confirmEmailSuccess() {
    when(securityUserRepository.findById(any(Long.class))).thenReturn(Optional.of(SAMPLE_User));

    assertDoesNotThrow(() -> securityCustomerService.confirmEmail(-2L));
  }
}
