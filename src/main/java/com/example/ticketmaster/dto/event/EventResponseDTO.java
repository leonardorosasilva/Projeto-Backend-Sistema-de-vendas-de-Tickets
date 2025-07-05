package com.example.ticketmaster.dto.event;

import com.example.ticketmaster.dto.user.UserResponseDTO;
import com.example.ticketmaster.dto.venue.VenueResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private VenueResponseDTO venue; // DTO aninhado
    private UserResponseDTO organizer; // DTO aninhado
    private String category;
    private Boolean isActive;
}