package com.example.ticketmaster.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateTime; // Data e hora do evento

    @ManyToOne(fetch = FetchType.LAZY) // * -> 1, Um evento acontece em um único local, mas um local pode ter muitos eventos
    @JoinColumn(name = "venue_id", nullable = false) // Coluna da chave estrangeira para Venue
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY) // Um organizador cria muitos eventos
    @JoinColumn(name = "organizer_id", nullable = false) // Coluna da chave estrangeira para o Usuário organizador
    private User organizer;

    @Column(nullable = true, length = 50)
    private String category;

    @Column(nullable = false)
    private Boolean isActive = true;
}
