package com.security.spring.unit.entity;

import com.security.spring.user.entity.User;
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
@Table(name="transition_history")
public class TransitionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String backendTransitionId;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="from_user_id")
    private User fromUser;
    private double fromUserBeforeBalance;
    private double fromUserAfterBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="to_user_id")
    private User toUser;
    private double toUserBeforeBalance;
    private double toUserAfterBalance;

    private String description;
    private LocalDateTime actionTime;
    private String status;

}
