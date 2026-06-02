package com.cumpon.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cupons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cupom {
    
    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;
    
    @Column(nullable = false, unique = true, length = 6)
    private String code;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;
    
    @Column(nullable = false)
    private LocalDateTime expirationDate;
    
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
    
    @Column(nullable = false)
    private Boolean published = false;
    
    @Column(nullable = false)
    private Boolean redeemed = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        LocalDateTime now = LocalDateTime.now();
        
        createdAt = now;
        updatedAt = now;
        
        
        if ( status.isEmpty() || status == null) {
            status = "ACTIVE";
        }
        if (published == null) {
            published = false;
        }
        if (redeemed == null) {
            redeemed = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}