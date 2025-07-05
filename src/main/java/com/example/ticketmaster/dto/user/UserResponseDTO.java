package com.example.ticketmaster.dto.user;

import com.example.ticketmaster.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles; // Retorna apenas os nomes das roles

    // Construtor ou método para mapear roles
    public void setRoles(Set<Role> roles) {
        this.roles = roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}