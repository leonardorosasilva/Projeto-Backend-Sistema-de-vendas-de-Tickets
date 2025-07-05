package com.example.ticketmaster.repository;

import com.example.ticketmaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String aLong);

    Optional<User> findByEmail(String email);
}
