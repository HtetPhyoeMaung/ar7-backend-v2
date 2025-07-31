package com.security.spring.promotion.service;

import com.security.spring.promotion.entity.TicketBox;
import com.security.spring.user.entity.User;

public interface TicketBoxService {
    TicketBox createTicketBox(User user , double depositAmount, double promotionUnit);
}
