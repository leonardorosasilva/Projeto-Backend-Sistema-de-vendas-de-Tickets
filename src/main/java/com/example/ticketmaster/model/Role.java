package com.example.ticketmaster.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private RoleName name;

    public enum RoleName {
        ROLE_USER, // --> USUARIO COMUM
        ROLE_ORGANIZER, // --> ORGANIZADOR DE EVENTOS
        ROLE_ADMIN // --> ADMINISTRADOR DO SISTEMA, ACESSO TOTAL
    }
}
