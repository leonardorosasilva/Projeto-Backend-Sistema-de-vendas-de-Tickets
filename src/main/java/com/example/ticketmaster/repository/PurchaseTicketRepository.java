package com.example.ticketmaster.repository;

import com.example.ticketmaster.model.PurchasedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseTicketRepository extends JpaRepository<PurchasedTicket, Long> {
    Optional<PurchasedTicket> findByUniqueTicketNumber(String uniqueTicketNumber);
    List<PurchasedTicket> findByPurchaseId(Long purchaseId);
    Optional<PurchasedTicket> findByUniqueCode(String uniqueCode);
}
