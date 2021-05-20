package com.cmlteam.cmltemplate.security;

import com.cmlteam.cmltemplate.entities.User;
import com.cmlteam.cmltemplate.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    getJwtFromRequest(request)
        .flatMap(this::getAuthCustomerFromToken)
        .ifPresent(user -> setContext(request, user));
    filterChain.doFilter(request, response);
  }

  private Optional<String> getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }

  private Optional<User> getAuthCustomerFromToken(String jwt) {
    if (jwtTokenProvider.isValid(jwt)) {
      var id = Long.valueOf(jwtTokenProvider.getSubject(jwt));
      return userRepository.findById(id).filter(User::isEnabled);
    }
    return Optional.empty();
  }

  private void setContext(HttpServletRequest request, User user) {
    var authentication1 =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authentication1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    var authentication = authentication1;
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
