package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.purchase.PurchaseRequestDTO;
import com.example.ticketmaster.dto.purchase.PurchaseResponseDTO;
import com.example.ticketmaster.dto.purchase.PurchasedTicketRequestDTO;
import com.example.ticketmaster.dto.purchase.PurchasedTicketResponseDTO;
import com.example.ticketmaster.exception.BusinessException;
import com.example.ticketmaster.exception.ResourceNotFoundException;
import com.example.ticketmaster.model.*;
import com.example.ticketmaster.repository.PurchaseRepository;
import com.example.ticketmaster.repository.PurchaseTicketRepository;
import com.example.ticketmaster.repository.TicketRepository;
import com.example.ticketmaster.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseTicketRepository purchaseTicketRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           PurchaseTicketRepository purchaseTicketRepository,
                           UserRepository userRepository,
                           TicketRepository ticketRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseTicketRepository = purchaseTicketRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PurchaseResponseDTO createPurchase(PurchaseRequestDTO purchaseRequestDTO) {
        // 1. Validar e buscar o usuário comprador
        User user = userRepository.findById(purchaseRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + purchaseRequestDTO.getUserId()));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setStatus("PENDING"); // Status inicial da compra

        BigDecimal totalAmount = BigDecimal.ZERO;
        Set<PurchasedTicket> purchasedTickets = new HashSet<>();

        // 2. Processar cada item de ingresso na compra
        for (PurchasedTicketRequestDTO itemDTO : purchaseRequestDTO.getPurchasedTickets()) {
            Ticket ticketType = ticketRepository.findById(itemDTO.getTicketId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de ingresso não encontrado com ID: " + itemDTO.getTicketId()));

            if (ticketType.getAvailableQuantity() < itemDTO.getQuantity()) {
                throw new BusinessException("Quantidade de ingressos '" + ticketType.getType() + "' indisponível para o evento '" + ticketType.getEvent().getName() + "'. Disponível: " + ticketType.getAvailableQuantity());
            }

            // Decrementar a quantidade disponível do tipo de ingresso
            ticketType.setAvailableQuantity(ticketType.getAvailableQuantity() - itemDTO.getQuantity());
            ticketRepository.save(ticketType); // Salva a atualização do estoque

            // Criar a entidade PurchasedTicket
            PurchasedTicket purchasedTicket = new PurchasedTicket();
            purchasedTicket.setPurchase(purchase); // Associa à compra atual
            purchasedTicket.setTicket(ticketType);
            purchasedTicket.setQuantity(itemDTO.getQuantity());
            purchasedTicket.setPriceAtPurchase(ticketType.getPrice()); // Registra o preço no momento da compra
            // O uniqueCode será gerado automaticamente pelo @PrePersist

            purchasedTickets.add(purchasedTicket);

            totalAmount = totalAmount.add(ticketType.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }

        purchase.setTotalAmount(totalAmount);
        purchase.setPurchasedTickets(purchasedTickets); // Associa os ingressos comprados à compra

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Atualizar o purchase_id em cada purchasedTicket após salvar a compra
        // Isso é necessário porque o purchase_id só é gerado após o save da compra
        // e PurchasedTicket precisa dele para persistir corretamente.
        for (PurchasedTicket pt : purchasedTickets) {
            pt.setPurchase(savedPurchase);
            PurchaseTicketRepository.save(pt);
        }

        return modelMapper.map(savedPurchase, PurchaseResponseDTO.class);
    }


    @Transactional(readOnly = true)
    public PurchaseResponseDTO getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada com ID: " + id));
        return modelMapper.map(purchase, PurchaseResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponseDTO> getPurchasesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + userId));
        List<Purchase> purchases = purchaseRepository.findByUser(user);
        return purchases.stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseResponseDTO updatePurchaseStatus(Long id, String newStatus) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada com ID: " + id));
        purchase.setStatus(newStatus);
        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return modelMapper.map(updatedPurchase, PurchaseResponseDTO.class);
    }

    @Transactional
    public PurchasedTicketResponseDTO redeemTicket(String uniqueCode) {
        PurchasedTicket purchasedTicket = purchaseTicketRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ingresso com código único não encontrado: " + uniqueCode));

        if (purchasedTicket.getRedeemed()) {
            throw new BusinessException("Ingresso com código " + uniqueCode + " já foi resgatado.");
        }

        purchasedTicket.setRedeemed(true);
        PurchasedTicket updatedTicket = purchaseTicketRepository.save(purchasedTicket);
        return modelMapper.map(updatedTicket, PurchasedTicketResponseDTO.class);
    }

    // Método para cancelar uma compra e devolver ingressos ao estoque
    @Transactional
    public PurchaseResponseDTO cancelPurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada com ID: " + purchaseId));

        if ("CANCELLED".equals(purchase.getStatus()) || "REFUNDED".equals(purchase.getStatus())) {
            throw new BusinessException("A compra já está cancelada ou reembolsada.");
        }

        // Devolver ingressos ao estoque
        for (PurchasedTicket pt : purchase.getPurchasedTickets()) {
            Ticket ticketType = pt.getTicket();
            ticketType.setAvailableQuantity(ticketType.getAvailableQuantity() + pt.getQuantity());
            ticketRepository.save(ticketType);
        }

        purchase.setStatus("CANCELLED");
        Purchase cancelledPurchase = purchaseRepository.save(purchase);
        return modelMapper.map(cancelledPurchase, PurchaseResponseDTO.class);
    }
}