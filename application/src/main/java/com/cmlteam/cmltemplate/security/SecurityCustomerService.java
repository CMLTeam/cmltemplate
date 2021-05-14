package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.repository.CustomerRepository;
import com.cmlteam.cmltemplate.service.CustomerAuthenticationFilter;
import com.cmlteam.cmltemplate.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityCustomerService extends OncePerRequestFilter {
  private final CustomerRepository customerRepository;
  // ToDo microservices
  private final CustomerAuthenticationFilter customerAuthenticationFilter;
  private final JwtTokenProvider jwtTokenProvider;

  public String generateShort(String subject) {
    return jwtTokenProvider.generateShort(subject);
  }

  public String generateLong(String subject) {
    return jwtTokenProvider.generateLong(subject);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtShort = request.getHeader("Customer-Authentication");
    String jwtLong = request.getHeader("Customer-Authentication-Long");
    customerAuthenticationFilter
        .getAuthId(jwtShort, jwtLong)
        .ifPresent(
            id -> {
              var authentication = getAuthentication(request, id);
              SecurityContextHolder.getContext().setAuthentication(authentication);
            });
    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request, Long id) {
    var userDetails = customerRepository.getOne(id);
    var authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
  }
}
