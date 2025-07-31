package com.security.spring.notification.dto;

import com.security.spring.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private Notification.Type type;
    private String senderId;
    private String receiverId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
