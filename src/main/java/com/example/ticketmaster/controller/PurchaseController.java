package com.example.ticketmaster.controller;

import com.example.ticketmaster.dto.purchase.PurchaseRequestDTO;
import com.example.ticketmaster.dto.purchase.PurchaseResponseDTO;
import com.example.ticketmaster.dto.purchase.PurchasedTicketResponseDTO;
import com.example.ticketmaster.services.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Apenas usuários logados podem fazer compras
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        PurchaseResponseDTO createdPurchase = purchaseService.createPurchase(purchaseRequestDTO);
        return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')") // Usuário da compra, Organizador ou Admin podem ver
    public ResponseEntity<PurchaseResponseDTO> getPurchaseById(@PathVariable Long id) {
        PurchaseResponseDTO purchase = purchaseService.getPurchaseById(id);
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (#userId == authentication.principal.id)") // Admin ou o próprio usuário podem ver suas compras
    public ResponseEntity<List<PurchaseResponseDTO>> getPurchasesByUser(@PathVariable Long userId) {
        List<PurchaseResponseDTO> purchases = purchaseService.getPurchasesByUser(userId);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status") // Atualizar status da compra (ex: de PENDING para COMPLETED após pagamento)
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')") // Admin ou organizador podem mudar status da compra
    public ResponseEntity<PurchaseResponseDTO> updatePurchaseStatus(@PathVariable Long id, @RequestParam String newStatus) {
        PurchaseResponseDTO updatedPurchase = purchaseService.updatePurchaseStatus(id, newStatus);
        return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
    }

    @PostMapping("/redeem-ticket") // Endpoint para resgatar um ingresso (no evento)
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')") // Organizador ou Admin pode resgatar ingressos
    public ResponseEntity<PurchasedTicketResponseDTO> redeemTicket(@RequestParam String uniqueCode) {
        PurchasedTicketResponseDTO redeemedTicket = purchaseService.redeemTicket(uniqueCode);
        return new ResponseEntity<>(redeemedTicket, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Usuário pode cancelar sua própria compra, ou Admin
    public ResponseEntity<PurchaseResponseDTO> cancelPurchase(@PathVariable Long id) {
        PurchaseResponseDTO cancelledPurchase = purchaseService.cancelPurchase(id);
        return new ResponseEntity<>(cancelledPurchase, HttpStatus.OK);
    }
}