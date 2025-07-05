package com.example.ticketmaster.dto.venue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequestDTO {
    @NotBlank(message = "Nome do local não pode estar em branco.")
    @Size(max = 100, message = "Nome do local não pode exceder 100 caracteres.")
    private String name;

    @NotBlank(message = "Endereço não pode estar em branco.")
    @Size(max = 255, message = "Endereço não pode exceder 255 caracteres.")
    private String address;

    @Size(max = 100, message = "Cidade não pode exceder 100 caracteres.")
    private String city;

    @Size(max = 100, message = "Estado não pode exceder 100 caracteres.")
    private String state;

    @Size(max = 20, message = "CEP não pode exceder 20 caracteres.")
    private String zipCode;

    @NotNull(message = "Capacidade não pode ser nula.")
    @Min(value = 1, message = "Capacidade deve ser no mínimo 1.")
    private Integer capacity;
}