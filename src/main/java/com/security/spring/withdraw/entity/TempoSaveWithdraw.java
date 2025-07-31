package com.security.spring.withdraw.entity;

import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tempo_save_withdraw")
public class TempoSaveWithdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double amount;

    private LocalDateTime saveDate;

    @ManyToOne
    @JoinColumn(name="tempo_withdraw_user_id")
    @ToString.Exclude
    private User tempoWithdrawUser;
}
