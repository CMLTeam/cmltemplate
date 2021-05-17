package com.cmlteam.cmltemplate.service;

import com.cmlteam.cmltemplate.entities.SecurityCustomer;
import com.cmlteam.cmltemplate.repository.SecurityCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityCustomerFilter extends OncePerRequestFilter {
  private final SecurityCustomerRepository securityCustomerRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtShort = request.getHeader("Customer-Authentication");
    String jwtLong = request.getHeader("Customer-Authentication-Long");

    getAuthCustomer(jwtShort, jwtLong).ifPresent(customer -> setContext(request, customer));
    filterChain.doFilter(request, response);
  }

  private Optional<SecurityCustomer> getAuthCustomer(String jwtShort, String jwtLong) {
    if (StringUtils.hasText(jwtLong)) {
      return getAuthCustomerFromToken(jwtLong);
    }
    return getAuthCustomerFromToken(jwtShort);
  }

  private Optional<SecurityCustomer> getAuthCustomerFromToken(String jwt) {
    if (jwtTokenProvider.isValid(jwt)) {
      return fetchUserDetailsId(jwt);
    }
    return Optional.empty();
  }

  private Optional<SecurityCustomer> fetchUserDetailsId(String jwt) {
    var id = Long.valueOf(jwtTokenProvider.getSubject(jwt));
    return securityCustomerRepository.findById(id).filter(SecurityCustomer::isEnabled);
  }

  private void setContext(HttpServletRequest request, SecurityCustomer customer) {
    var authentication = getAuthentication(request, customer);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      HttpServletRequest request, SecurityCustomer customer) {
    var authentication =
        new UsernamePasswordAuthenticationToken(customer, null, customer.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
  }
}
