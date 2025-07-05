package br.com.ticketmaster.repository;

import br.com.ticketmaster.model.Event;
import br.com.ticketmaster.model.User;
import jdk.jfr.Category;
import org.springframework.beans.factory.ListableBeanFactory;
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
