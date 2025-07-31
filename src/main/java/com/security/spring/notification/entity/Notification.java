package com.security.spring.notification.entity;

import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String senderId;
    private String receiverId;
    private Type type;

    public enum Type{
        DEPOSIT,WITHDRAW,BAN_STATUS,FAST_TRANSFER
    }
}
