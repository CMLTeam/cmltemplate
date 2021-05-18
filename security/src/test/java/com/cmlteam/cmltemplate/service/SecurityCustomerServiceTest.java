package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.SecurityCustomer;
import com.cmlteam.cmltemplate.exceptions.ConflictException;
import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.model.request.AuthenticationRequest;
import com.cmlteam.cmltemplate.model.response.AuthenticationResponse;
import com.cmlteam.cmltemplate.repository.SecurityCustomerRepository;
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
  public static final AuthenticationRequest SAMPLE_REQUEST =
      AuthenticationRequest.builder().email("some@email").password("some-password").build();
  private final SecurityCustomer SAMPLE_CUSTOMER = SecurityCustomer.builder().id(-1L).build();
  @Mock AuthenticationManager authenticationManager;
  @Mock JwtTokenProvider jwtTokenProvider;
  @Mock PasswordEncoder passwordEncoder;
  @Mock SecurityCustomerRepository securityCustomerRepository;

  @InjectMocks SecurityCustomerService securityCustomerService;

  @Test
  void registerAlreadyExist() {
    when(securityCustomerRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_CUSTOMER));

    assertThrows(ConflictException.class, () -> securityCustomerService.register(SAMPLE_REQUEST));
  }

  @Test
  void registerSuccess() {
    when(securityCustomerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    when(securityCustomerRepository.save(any(SecurityCustomer.class))).thenReturn(SAMPLE_CUSTOMER);
    when(passwordEncoder.encode(any(String.class))).thenReturn("encode password");
    when(jwtTokenProvider.generateShort(any(String.class))).thenReturn("new short");
    when(jwtTokenProvider.generateLong(any(String.class))).thenReturn("new long");

    AuthenticationResponse response = securityCustomerService.register(SAMPLE_REQUEST);
    assertEquals("new short", response.getShortToken());
    assertEquals("new long", response.getLongToken());
  }

  @Test
  void generateTokenForInvalidCredentials() {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(InternalAuthenticationServiceException.class);

    assertThrows(
        InternalAuthenticationServiceException.class,
        () -> securityCustomerService.generateToken(SAMPLE_REQUEST));
  }

  @Test
  void generateTokenForNotExistCustomer() {
    when(securityCustomerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> securityCustomerService.generateToken(SAMPLE_REQUEST));
  }

  @Test
  void generateTokenSuccess() {
    when(securityCustomerRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.of(SAMPLE_CUSTOMER));
    when(jwtTokenProvider.generateShort(any(String.class))).thenReturn("new short");
    when(jwtTokenProvider.generateLong(any(String.class))).thenReturn("new long");

    AuthenticationResponse response = securityCustomerService.generateToken(SAMPLE_REQUEST);
    assertEquals("new short", response.getShortToken());
    assertEquals("new long", response.getLongToken());
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
