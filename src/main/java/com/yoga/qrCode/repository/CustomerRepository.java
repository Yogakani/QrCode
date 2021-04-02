package com.yoga.qrCode.repository;

import com.yoga.qrCode.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(String userId);
    Optional<Customer> findByEmailId(String emailId);
}
