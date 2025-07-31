package com.security.spring.unit.services;

import com.security.spring.rro.ResponseFormat;
import com.security.spring.unit.dto.*;
import com.security.spring.unit.entity.UserUnits;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface UnitService {
     UnitResponse createUnit(String ar7Id, UnitRequest data);
    ResponseFormat changeProUnit(String ar7Id, double proUnit);
     TransitionResponse unitTransferPlus(String ar7Id, TransitionRequest data);
    TransitionResponse unitTransferMinus(String ar7Id,TransitionRequest data);
     TransitionResponse getAllCreateUnitHistory(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate);
     TransitionResponse getAllTransitionHistory(Pageable pageable,LocalDateTime startDate, LocalDateTime endDate);
     TransitionResponse getAllTransitionHistoryOwn(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate);
    TransitionResponse getAllTransitionHistoryByUserId(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate);


    AccountCheckResponse accountCheck(String ar7Id);
     UnitResponse getOwnUnit(String ar7Id);

    UserUnits findByUser_Ar7Id(String agentAr7Id);

    void saveAll(List<UserUnits> userUnitsList);

    TransitionResponse fastTransferPlus(TransitionRequest data);
}
//