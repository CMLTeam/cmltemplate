package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomerDetailsService customerDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtShort = getJwtShortFromHeader(request);
    String jwtLong = getJwtLongFromHeader(request);
    doJwtFilter(request, jwtShort);
    if (StringUtils.hasText(jwtLong)) {
      doJwtFilter(request, jwtLong);
    }
    filterChain.doFilter(request, response);
  }

  private void doJwtFilter(HttpServletRequest request, String jwt) {
    if (jwtTokenProvider.isValid(jwt)) {
      var userDetails = fetchUserDetails(jwt);
      userDetails.ifPresent(it -> setAuthentication(request, it));
    }
  }

  private Optional<Customer> fetchUserDetails(String jwt) {
    var id = Long.valueOf(jwtTokenProvider.getSubject(jwt));
    return customerDetailsService.loadCustomerById(id);
  }

  private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
    var authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String getJwtShortFromHeader(HttpServletRequest request) {
    return request.getHeader("Customer-Authentication");
  }

  private String getJwtLongFromHeader(HttpServletRequest request) {
    return request.getHeader("Customer-Authentication-Long");
  }
}
