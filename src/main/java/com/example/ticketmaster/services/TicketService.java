package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.ticket.TicketRequestDTO;
import com.example.ticketmaster.dto.ticket.TicketResponseDTO;
import com.example.ticketmaster.exception.ResourceNotFoundException;
import com.example.ticketmaster.model.Event;
import com.example.ticketmaster.model.Ticket;
import com.example.ticketmaster.repository.EventRepository;
import com.example.ticketmaster.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, ModelMapper modelMapper) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        // 1. Validar e buscar o evento
        Event event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + ticketRequestDTO.getEventId()));

        // 2. Mapear DTO para Entidade e associar o Evento
        Ticket ticket = modelMapper.map(ticketRequestDTO, Ticket.class);
        ticket.setEvent(event);
        // Ao criar, a quantidade disponível é igual à quantidade total
        ticket.setAvailableQuantity(ticket.getTotalQuantity());

        Ticket savedTicket = ticketRepository.save(ticket);
        return modelMapper.map(savedTicket, TicketResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingresso não encontrado com ID: " + id));
        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getTicketsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + eventId));
        List<Ticket> tickets = ticketRepository.findByEvent(event);
        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO ticketRequestDTO) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingresso não encontrado com ID: " + id));

        // Se o evento for alterado, buscar e associar o novo evento
        if (ticketRequestDTO.getEventId() != null && !ticketRequestDTO.getEventId().equals(existingTicket.getEvent().getId())) {
            Event newEvent = eventRepository.findById(ticketRequestDTO.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Novo evento não encontrado com ID: " + ticketRequestDTO.getEventId()));
            existingTicket.setEvent(newEvent);
        }

        // Se a quantidade total for alterada, ajustar a quantidade disponível
        if (ticketRequestDTO.getTotalQuantity() != null && !ticketRequestDTO.getTotalQuantity().equals(existingTicket.getTotalQuantity())) {
            int quantityDifference = ticketRequestDTO.getTotalQuantity() - existingTicket.getTotalQuantity();
            existingTicket.setAvailableQuantity(existingTicket.getAvailableQuantity() + quantityDifference);
            existingTicket.setTotalQuantity(ticketRequestDTO.getTotalQuantity());
        }

        // Mapear outros campos do DTO para a entidade existente
        modelMapper.map(ticketRequestDTO, existingTicket);
        existingTicket.setId(id); // Garante que o ID não seja alterado pelo mapeamento

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return modelMapper.map(updatedTicket, TicketResponseDTO.class);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingresso não encontrado com ID: " + id));
        ticketRepository.delete(ticket);
    }
}