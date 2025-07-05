package com.example.ticketmaster.dto.purchase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedTicketRequestDTO {
    @NotNull(message = "ID do tipo de ingresso não pode ser nulo.")
    private Long ticketId;

    @NotNull(message = "Quantidade não pode ser nula.")
    @Min(value = 1, message = "Quantidade deve ser no mínimo 1.")
    private Integer quantity;
}