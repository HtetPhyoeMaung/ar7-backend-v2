package com.security.spring.report.service;

import com.security.spring.report.dto.UserDetailsResponse;
import com.security.spring.utils.UserPlayDetailTransitionGroupKey;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;


public interface UserDetailReportService {


    ResponseEntity<UserDetailsResponse> getUserDetailReportByAr7Id(String ar7Id, LocalDateTime startDate, LocalDateTime  endDate);

    ResponseEntity<UserDetailsResponse> getUserDetailReport(String token, UserPlayDetailTransitionGroupKey userPlayDetailTransitionGroupKey, Pageable pageable);

    ResponseEntity<UserDetailsResponse> getCommissionConfirmReport(String token, String role, String startDate, String endDate, Pageable pageable);
}
