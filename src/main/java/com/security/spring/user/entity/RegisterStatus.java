package com.security.spring.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "register_status", indexes = {
        @Index(name = "idx_register_status_user", columnList = "user_id"),
        @Index(name = "idx_register_status_status", columnList = "registerStatus")
})
public class RegisterStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
    private Integer registerStatus;
}
