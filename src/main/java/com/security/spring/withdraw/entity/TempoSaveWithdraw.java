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
@Table(name = "tempo_save_withdraw", indexes = {
        @Index(name = "idx_tempo_save_withdraw_user", columnList = "tempo_withdraw_user_id"),
        @Index(name = "idx_tempo_save_withdraw_save_date", columnList = "saveDate"),
        @Index(name = "idx_tempo_save_withdraw_user_amount_date", columnList = "tempo_withdraw_user_id,amount,saveDate")
})
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
