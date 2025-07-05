package com.example.ticketmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<VenueRepository, Long> {
    Optional<VenueRepository> findByName(String name);
}
