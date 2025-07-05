package com.example.ticketmaster.repository;

import com.example.ticketmaster.model.Event;
import com.example.ticketmaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByCategory(String category);
    List<Event> findByDateTimeAfterOrderByDateTimeAsc(LocalDateTime date);
    List<Event> findByOrganizer(User organizer);
    List<Event> findByIsActiveTrue();

}
