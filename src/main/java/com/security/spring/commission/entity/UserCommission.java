package com.security.spring.commission.entity;

import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_commission")
public class UserCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float commission;

    @ManyToOne
    @ToString.Exclude
    private GameType gameType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="commission_user_id")
    @ToString.Exclude
    private User user;
    private String upLineAr7Id;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

    @PrePersist
    private void onCreate(){
        this.createdOn = LocalDateTime.now();
        this.modifiedOn = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        this.modifiedOn = LocalDateTime.now();
    }



}
