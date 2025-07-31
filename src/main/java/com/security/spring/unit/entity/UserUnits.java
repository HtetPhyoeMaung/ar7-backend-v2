package com.security.spring.unit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="units")
@Builder
public class UserUnits implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer unitId;
    private double mainUnit;
    private double gameUnit;
    private double promotionUnit;
    private int tickets;
    @OneToOne(mappedBy = "userUnits")
    @ToString.Exclude
    @JsonIgnore
    private User user;
}
