package com.example.ticketmaster.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedTicketResponseDTO {
    private Long id;
    private Long purchaseId; // ID da compra
    private Long ticketId;   // ID do tipo de ingresso original
    private String eventName; // Nome do evento
    private String ticketType; // Tipo do ingresso (ex: Inteira)
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private String uniqueCode;
    private Boolean redeemed;
}