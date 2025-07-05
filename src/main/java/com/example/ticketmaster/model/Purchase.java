package com.example.ticketmaster.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Um usuário pode ter muitas compras
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // O usuário que fez a compra

    @Column(nullable = false)
    private LocalDateTime purchaseDate; // Data e hora da compra

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // Valor total da compra

    @Column(nullable = false, length = 50)
    private String status; // Ex: "PENDING", "COMPLETED", "CANCELLED", "REFUNDED"

    // Relacionamento muitos-para-muitos entre Purchase e Ticket,
    // com informações adicionais na tabela de junção (quantidade, preço unitário na compra)
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchasedTicket> purchasedTickets = new HashSet<>();
}