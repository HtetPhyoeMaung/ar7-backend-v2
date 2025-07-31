package com.security.spring.notification.service;

import com.security.spring.notification.entity.Notification;
import com.security.spring.notification.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepo notificationRepo;

    @Override
    public Notification save(Notification notification) {
        Notification savedNotification = notificationRepo.save(notification);
        log.info("notification was saved , {}",savedNotification);
        return savedNotification;
    }
}
