package com.example.ticketmaster.controller;

import com.example.ticketmaster.dto.ticket.TicketRequestDTO;
import com.example.ticketmaster.dto.ticket.TicketResponseDTO;
import com.example.ticketmaster.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem criar tipos de ingresso
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO createdTicket = ticketService.createTicket(ticketRequestDTO);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        TicketResponseDTO ticket = ticketService.getTicketById(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}") // Buscar tickets por ID de evento
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByEvent(@PathVariable Long eventId) {
        List<TicketResponseDTO> tickets = ticketService.getTicketsByEvent(eventId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem atualizar
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO updatedTicket = ticketService.updateTicket(id, ticketRequestDTO);
        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin podem deletar
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return new ResponseEntity<>("Tipo de ingresso deletado com sucesso.", HttpStatus.NO_CONTENT);
    }
}