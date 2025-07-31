package com.security.spring.unit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="unit_create_history")
public class AdminUnitCreateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , unique = true , nullable = false)
    private Integer id;
    @Column(name = "unit_amt")
    private double unitAmount;
    @Column(name="create_date")
    private LocalDateTime dateTime;
    private String description;
}
