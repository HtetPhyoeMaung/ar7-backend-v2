package com.security.spring.commission.service;

import com.security.spring.commission.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface CommissionService {
    CommissionResponse saveCommission(CommissionRequest data , String token);

    CommissionResponse updateCommission(@Valid CommissionRequest data, String token);

    CommissionResponse checkCommission(String  gameTypeCode, String token);

    CommissionResponse getCommissionByAr7Id(String  ar7Id, String token);

   CommissionResponse calculateCommission(String token);

    CommissionResponse confirmOrCancelCommission(String token, String agentAr7Id);

    CommissionResponse calculatedCommissionListForCompleteDownline(String token, Pageable pageable);

    CommissionResponse getDownLineCommission(String token, Pageable pageable);

    CommissionResponse calculateSpaceTechGameCommission(String token);

    CommissionResponse findById(int  id);
}
