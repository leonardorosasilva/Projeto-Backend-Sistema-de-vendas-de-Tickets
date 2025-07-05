package com.example.ticketmaster.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Username não pode estar em branco.")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres.")
    private String username;

    @NotBlank(message = "Email não pode estar em branco.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "Email não pode exceder 100 caracteres.")
    private String email;

    @NotBlank(message = "Senha não pode estar em branco.")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres.")
    private String password;
}