package com.example.waterbilling.repository;

import com.example.waterbilling.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomerIdOrderByDateDesc(Long customerId);

    List<Bill> findAllByOrderByDateDesc();

    List<Bill> findBySyncedFalseOrderByDateDesc();

    Optional<Bill> findByBillCode(String billCode);
}
