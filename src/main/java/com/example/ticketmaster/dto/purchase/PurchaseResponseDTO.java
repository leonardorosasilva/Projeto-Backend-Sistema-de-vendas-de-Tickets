package com.example.ticketmaster.dto.purchase;

import com.example.ticketmaster.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {
    private Long id;
    private UserResponseDTO user; // Informações do usuário comprador
    private LocalDateTime purchaseDate;
    private BigDecimal totalAmount;
    private String status;
    private List<PurchasedTicketResponseDTO> purchasedTickets; // Detalhes dos ingressos comprados
}