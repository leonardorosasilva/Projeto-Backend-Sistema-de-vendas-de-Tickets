package br.com.ticketmaster.repository;

import br.com.ticketmaster.model.User;
import com.example.ticketmaster.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<com.example.ticketmaster.model.Purchase, Long> {
    List<Purchase> findByUser(User user);
    List<Purchase> findByStatus(String status);
}
