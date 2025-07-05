package com.example.ticketmaster.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
    @NotBlank(message = "Nome do evento não pode estar em branco.")
    @Size(max = 255, message = "Nome do evento não pode exceder 255 caracteres.")
    private String name;

    @NotBlank(message = "Descrição do evento não pode estar em branco.")
    private String description;

    @NotNull(message = "Data e hora do evento não podem ser nulas.")
    private LocalDateTime dateTime;

    @NotNull(message = "ID do local não pode ser nulo.")
    private Long venueId;

    @NotNull(message = "ID do organizador não pode ser nulo.")
    private Long organizerId;

    @Size(max = 50, message = "Categoria não pode exceder 50 caracteres.")
    private String category;

    private Boolean isActive; // Opcional, pode ser true por padrão no serviço
}