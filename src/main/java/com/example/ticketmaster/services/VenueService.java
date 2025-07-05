package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.venue.VenueRequestDTO;
import com.example.ticketmaster.dto.venue.VenueResponseDTO;
import com.example.ticketmaster.exception.ResourceNotFoundException;
import com.example.ticketmaster.model.Venue;
import com.example.ticketmaster.repository.VenueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final ModelMapper modelMapper;

    public VenueService(VenueRepository venueRepository, ModelMapper modelMapper) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public VenueResponseDTO createVenue(VenueRequestDTO venueRequestDTO) {
        Venue venue = modelMapper.map(venueRequestDTO, Venue.class);
        Venue savedVenue = venueRepository.save(venue);
        return modelMapper.map(savedVenue, VenueResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public VenueResponseDTO getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));
        return modelMapper.map(venue, VenueResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<VenueResponseDTO> getAllVenues() {
        List<Venue> venues = venueRepository.findAll();
        return venues.stream()
                .map(venue -> modelMapper.map(venue, VenueResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public VenueResponseDTO updateVenue(Long id, VenueRequestDTO venueRequestDTO) {
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));

        modelMapper.map(venueRequestDTO, existingVenue); // Atualiza os campos do objeto existente
        existingVenue.setId(id); // Garante que o ID não seja alterado pelo mapeamento

        Venue updatedVenue = venueRepository.save(existingVenue);
        return modelMapper.map(updatedVenue, VenueResponseDTO.class);
    }

    @Transactional
    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com ID: " + id));
        venueRepository.delete(venue);
    }
}