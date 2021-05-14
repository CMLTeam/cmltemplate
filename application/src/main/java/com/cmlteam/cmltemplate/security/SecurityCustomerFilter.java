package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.exceptions.NotFoundException;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
import com.cmlteam.cmltemplate.service.CustomerAuthenticationFilter;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityCustomerFilter extends OncePerRequestFilter {
  private final CustomerRepository customerRepository;
  // ToDo microservices
  private final CustomerAuthenticationFilter customerAuthenticationFilter;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtShort = request.getHeader("Customer-Authentication");
    String jwtLong = request.getHeader("Customer-Authentication-Long");

    customerAuthenticationFilter
        .getAuthId(jwtShort, jwtLong)
        .ifPresent(id -> setContext(request, id));
    filterChain.doFilter(request, response);
  }

  private void setContext(HttpServletRequest request, Long id) {
    var authentication = getAuthentication(request, id);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request, Long id) {
    var userDetails =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("User doesn't exists with id " + id));
    var authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
  }
}
