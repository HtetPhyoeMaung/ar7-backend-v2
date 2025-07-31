package com.security.spring.promotion.service.impl;

import com.security.spring.promotion.entity.TicketBox;
import com.security.spring.promotion.repository.TicketBoxRepository;
import com.security.spring.promotion.service.TicketBoxService;
import com.security.spring.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketBoxServiceImpl implements TicketBoxService {
    private final TicketBoxRepository ticketBoxRepository;

    @Override
    public TicketBox createTicketBox(User user, double depositAmount, double promotionUnit) {
        TicketBox ticketBox = ticketBoxRepository.findByUser_Ar7Id(user.getAr7Id()).orElse(null);

        double v = Math.round((depositAmount / promotionUnit) * 10) / 10.0;
        if(ticketBox == null){
            long ticketAmount = (long) v;
            double outlierValue = Math.round((v - ticketAmount) * 10) / 10.0;;
            ticketBox = TicketBox.builder()
                    .ticketAmount(ticketAmount)
                    .outlierValue(outlierValue)
                    .user(user)
                    .build();
        }else{
            ticketBox = user.getTicketBox();
            double result = v + ticketBox.getTicketAmount() + ticketBox.getOutlierValue();
            long ticketAmount = (long) result;
            double outlierValue = Math.round((result - ticketAmount) * 10) / 10.0;

            ticketBox.setTicketAmount(ticketAmount);
            ticketBox.setOutlierValue(outlierValue);
        }

        return ticketBoxRepository.save(ticketBox);
    }
}
