package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.Customer;
import com.cmlteam.cmltemplate.repository.CustomerRepository;
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
class SecurityCustomerFilter extends OncePerRequestFilter {
  private final CustomerRepository customerRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    getJwtFromRequest(request)
        .flatMap(this::getAuthCustomerFromToken)
        .ifPresent(customer -> setContext(request, customer));
    filterChain.doFilter(request, response);
  }

  private Optional<String> getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }

  private Optional<Customer> getAuthCustomerFromToken(String jwt) {
    if (jwtTokenProvider.isValid(jwt)) {
      var id = Long.valueOf(jwtTokenProvider.getSubject(jwt));
      return customerRepository.findById(id).filter(Customer::isEnabled);
    }
    return Optional.empty();
  }

  private void setContext(HttpServletRequest request, Customer customer) {
    var authentication1 =
        new UsernamePasswordAuthenticationToken(customer, null, customer.getAuthorities());
    authentication1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    var authentication = authentication1;
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
