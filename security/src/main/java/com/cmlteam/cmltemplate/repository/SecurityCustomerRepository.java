package com.cmlteam.cmltemplate.repository;

import com.cmlteam.cmltemplate.entities.SecurityCustomer;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityCustomerRepository
    extends JpaRepositoryImplementation<SecurityCustomer, Long> {
  Optional<SecurityCustomer> findByEmail(String email);
}
