package com.example.ticketmaster.repository;

import com.example.ticketmaster.model.User;
import com.example.ticketmaster.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
    List<Purchase> findByStatus(String status);
}
