package com.example.ticketmaster.controller;

import com.example.ticketmaster.dto.event.EventRequestDTO;
import com.example.ticketmaster.dto.event.EventResponseDTO;
import com.example.ticketmaster.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem criar eventos
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventRequestDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        EventResponseDTO event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        List<EventResponseDTO> events = eventService.searchEvents(name, category);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem atualizar
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventRequestDTO);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem deletar
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Evento deletado com sucesso.", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/deactivate") // Método PATCH para desativar um evento
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> deactivateEvent(@PathVariable Long id) {
        EventResponseDTO deactivatedEvent = eventService.deactivateEvent(id);
        return new ResponseEntity<>(deactivatedEvent, HttpStatus.OK);
    }
}