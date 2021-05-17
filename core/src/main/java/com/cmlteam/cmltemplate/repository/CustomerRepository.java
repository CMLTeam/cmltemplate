package com.cmlteam.cmltemplate.repository;

import com.cmlteam.cmltemplate.entities.Customer;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepositoryImplementation<Customer, Long> {}
