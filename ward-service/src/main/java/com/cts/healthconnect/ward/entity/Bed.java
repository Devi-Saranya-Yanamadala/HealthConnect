package com.cts.healthconnect.ward.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "beds",
    uniqueConstraints = @UniqueConstraint(columnNames = "bed_number")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bed_number", nullable = false)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    private WardType wardType;

    // true = occupied, false = available
    private Boolean occupied;
}

