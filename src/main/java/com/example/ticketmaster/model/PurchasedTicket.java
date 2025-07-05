package com.example.ticketmaster.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID; // Para gerar um código único para cada ingresso comprado

@Entity
@Table(name = "purchased_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase; // A compra à qual este ingresso pertence

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket; // O tipo de ingresso original (Event Ticket)

    @Column(nullable = false)
    private Integer quantity; // Quantidade deste tipo de ingresso comprada nesta compra

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase; // Preço unitário do ingresso no momento da compra

    @Column(nullable = false, unique = true)
    private String uniqueCode; // Código único para cada ingresso (ex: QR code ID)

    @Column(nullable = false)
    private Boolean redeemed = false; // Se o ingresso já foi usado no evento

    @PrePersist
    public void generateUniqueCode() {
        if (this.uniqueCode == null || this.uniqueCode.isEmpty()) {
            this.uniqueCode = UUID.randomUUID().toString(); // Gerar um UUID como código único
        }
    }
}