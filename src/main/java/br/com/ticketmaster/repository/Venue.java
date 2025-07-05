package br.com.ticketmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Venue extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
}
