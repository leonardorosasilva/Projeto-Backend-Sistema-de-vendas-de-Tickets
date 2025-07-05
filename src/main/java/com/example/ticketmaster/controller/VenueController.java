package com.example.ticketmaster.controller;

import com.example.ticketmaster.dto.venue.VenueRequestDTO;
import com.example.ticketmaster.dto.venue.VenueResponseDTO;
import com.example.ticketmaster.services.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode criar um novo local
    public ResponseEntity<VenueResponseDTO> createVenue(@Valid @RequestBody VenueRequestDTO venueRequestDTO) {
        VenueResponseDTO createdVenue = venueService.createVenue(venueRequestDTO);
        return new ResponseEntity<>(createdVenue, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> getVenueById(@PathVariable Long id) {
        VenueResponseDTO venue = venueService.getVenueById(id);
        return new ResponseEntity<>(venue, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VenueResponseDTO>> getAllVenues() {
        List<VenueResponseDTO> venues = venueService.getAllVenues();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode atualizar um local
    public ResponseEntity<VenueResponseDTO> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueRequestDTO venueRequestDTO) {
        VenueResponseDTO updatedVenue = venueService.updateVenue(id, venueRequestDTO);
        return new ResponseEntity<>(updatedVenue, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode deletar um local
    public ResponseEntity<String> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return new ResponseEntity<>("Local deletado com sucesso.", HttpStatus.NO_CONTENT);
    }
}