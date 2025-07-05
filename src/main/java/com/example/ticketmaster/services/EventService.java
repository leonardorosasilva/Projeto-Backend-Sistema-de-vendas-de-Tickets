package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.event.EventRequestDTO;
import com.example.ticketmaster.dto.event.EventResponseDTO;
import com.example.ticketmaster.exception.ResourceNotFoundException;
import com.example.ticketmaster.model.Event;
import com.example.ticketmaster.model.User;
import com.example.ticketmaster.model.Venue;
import com.example.ticketmaster.repository.EventRepository;
import com.example.ticketmaster.repository.UserRepository;
import com.example.ticketmaster.repository.VenueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, VenueRepository venueRepository,
                        UserRepository userRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        // 1. Validar e buscar o local (Venue)
        Venue venue = venueRepository.findById(eventRequestDTO.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + eventRequestDTO.getVenueId()));

        // 2. Validar e buscar o organizador (User)
        User organizer = userRepository.findById(eventRequestDTO.getOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado com ID: " + eventRequestDTO.getOrganizerId()));

        // 3. Mapear DTO para Entidade e associar Venue e Organizer
        Event event = modelMapper.map(eventRequestDTO, Event.class);
        event.setVenue(venue);
        event.setOrganizer(organizer);
        event.setIsActive(true); // Novo evento é ativo por padrão

        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
        return modelMapper.map(event, EventResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> modelMapper.map(event, EventResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> searchEvents(String name, String category) {
        List<Event> events;
        if (name != null && !name.isEmpty()) {
            events = eventRepository.findByNameContainingIgnoreCase(name);
        } else if (category != null && !category.isEmpty()) {
            events = eventRepository.findByCategory(category);
        } else {
            events = eventRepository.findByIsActiveTrue(); // Retorna apenas eventos ativos por padrão
        }
        return events.stream()
                .map(event -> modelMapper.map(event, EventResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));

        // Atualizar Venue e Organizer se os IDs forem fornecidos e diferentes
        if (eventRequestDTO.getVenueId() != null && !eventRequestDTO.getVenueId().equals(existingEvent.getVenue().getId())) {
            Venue newVenue = venueRepository.findById(eventRequestDTO.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Novo local não encontrado com ID: " + eventRequestDTO.getVenueId()));
            existingEvent.setVenue(newVenue);
        }
        if (eventRequestDTO.getOrganizerId() != null && !eventRequestDTO.getOrganizerId().equals(existingEvent.getOrganizer().getId())) {
            User newOrganizer = userRepository.findById(eventRequestDTO.getOrganizerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Novo organizador não encontrado com ID: " + eventRequestDTO.getOrganizerId()));
            existingEvent.setOrganizer(newOrganizer);
        }

        // Mapear outros campos do DTO para a entidade existente
        modelMapper.map(eventRequestDTO, existingEvent);
        existingEvent.setId(id); // Garante que o ID não seja alterado pelo mapeamento

        Event updatedEvent = eventRepository.save(existingEvent);
        return modelMapper.map(updatedEvent, EventResponseDTO.class);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
        eventRepository.delete(event);
    }

    @Transactional
    public EventResponseDTO deactivateEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
        event.setIsActive(false);
        Event updatedEvent = eventRepository.save(event);
        return modelMapper.map(updatedEvent, EventResponseDTO.class);
    }
}