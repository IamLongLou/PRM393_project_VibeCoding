package com.example.waterbilling.repository;

import com.example.waterbilling.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCode(String code);

    List<Customer> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
}
