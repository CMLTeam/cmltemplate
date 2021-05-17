package com.cmlteam.cmltemplate.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class SecurityCustomer extends Customer implements UserDetails {
  @Column(name = "password")
  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  // TODO remove mock function
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // TODO remove mock function
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // TODO remove mock function
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // TODO remove mock function
  @Override
  public boolean isEnabled() {
    return true;
  }
}
