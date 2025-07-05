package com.example.ticketmaster.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username ou email não pode estar em branco.")
    private String usernameOrEmail;

    @NotBlank(message = "Senha não pode estar em branco.")
    private String password;
}