package br.com.ticketmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseTicketRepository extends JpaRepository<com.example.ticketmaster.model.PurchasedTicket, Long> {
    Optional<com.example.ticketmaster.model.PurchasedTicket> findByUniqueTicketNumber(String uniqueTicketNumber);
    List<com.example.ticketmaster.model.PurchasedTicket> findByPurchaseId(Long purchaseId);
}
