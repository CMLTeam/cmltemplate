package com.cmlteam.cmltemplate.repository;

import com.cmlteam.cmltemplate.entities.Customer;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepositoryImplementation<Customer, Long> {
  Optional<Customer> findByEmail(String email);
}
