package com.cts.healthconnect.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data @NoArgsConstructor @AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String module;      
    private String action;      
    private String performedBy; 
    private String resourceId;  
    @Column(columnDefinition = "TEXT")
    private String details;     
    private LocalDateTime timestamp;

    @PrePersist
    public void onPrePersist() { this.timestamp = LocalDateTime.now(); }
}