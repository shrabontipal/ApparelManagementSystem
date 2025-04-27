package com.apparel.management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "apparel_id", nullable = false)
    private Apparel apparel;
    
    @Positive(message = "Quantity must be positive")
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    private double amount;
    
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public enum TransactionType {
        PURCHASE, REFUND, RESTOCK
    }
}
