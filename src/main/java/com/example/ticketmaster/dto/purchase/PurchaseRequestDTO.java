package com.example.ticketmaster.dto.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDTO {
    @NotNull(message = "ID do usuário não pode ser nulo.")
    private Long userId;

    @NotNull(message = "Pelo menos um ingresso deve ser selecionado para compra.")
    @Size(min = 1, message = "Pelo menos um ingresso deve ser selecionado para compra.")
    @Valid // Para validar os DTOs aninhados
    private List<PurchasedTicketRequestDTO> purchasedTickets;
}