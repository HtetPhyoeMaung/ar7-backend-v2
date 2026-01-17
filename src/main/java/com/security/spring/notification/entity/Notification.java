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
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_receiver", columnList = "receiverId"),
        @Index(name = "idx_notification_sender", columnList = "senderId"),
        @Index(name = "idx_notification_type", columnList = "type"),
        @Index(name = "idx_notification_created_time", columnList = "createdTime"),
        @Index(name = "idx_notification_receiver_type", columnList = "receiverId,type")
})
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
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type{
        DEPOSIT,WITHDRAW,BAN_STATUS,FAST_TRANSFER
    }
}
