package com.example.ticketmaster.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {
    @NotNull(message = "ID do evento não pode ser nulo.")
    private Long eventId;

    @NotBlank(message = "Tipo de ingresso não pode estar em branco.")
    @Size(max = 100, message = "Tipo de ingresso não pode exceder 100 caracteres.")
    private String type;

    @NotNull(message = "Preço não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero.")
    private BigDecimal price;

    @NotNull(message = "Quantidade total não pode ser nula.")
    @Min(value = 0, message = "Quantidade total deve ser no mínimo 0.")
    private Integer totalQuantity; // Quantidade total a ser criada

    @Size(max = 50, message = "Seção não pode exceder 50 caracteres.")
    private String section;
}