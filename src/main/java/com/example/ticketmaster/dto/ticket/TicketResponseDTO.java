package com.example.ticketmaster.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private Long eventId; // Apenas o ID do evento
    private String type;
    private BigDecimal price;
    private Integer totalQuantity;
    private Integer availableQuantity; // Inclui a quantidade disponível
    private String section;
}