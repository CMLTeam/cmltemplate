package com.cmlteam.cmltemplate.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name = "customer")
public class Customer implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
  }

  @Override
  public String getUsername() {
    return email;
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
