package com.security.spring.report.rest;

import com.security.spring.config.JWTService;
import com.security.spring.report.dto.UserDetailsResponse;
import com.security.spring.report.service.UserDetailReportService;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.DateUitls;
import com.security.spring.utils.UserPlayDetailTransitionGroupKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/rp/report-details")
@RequiredArgsConstructor
public class ReportController {

    private final UserDetailReportService userDetailReportService;
    private final JWTService jwtService;
    private final UserRepository userRepository;


    @GetMapping("/{ar7Id}")
    public ResponseEntity<UserDetailsResponse> getUserDetailReportByAr7Id(@PathVariable String  ar7Id, @RequestParam(name = "startDate", required = false) String  startDate,
                                                                    @RequestParam(name = "endDate", required = false)String endDate
                                                                   ){

       return userDetailReportService.getUserDetailReportByAr7Id(ar7Id, DateUitls.parseDateTime(startDate), DateUitls.parseDateTime(endDate));
    }

    @GetMapping("/detail")
    public ResponseEntity<UserDetailsResponse> getUserDetailReport(@RequestHeader("Authorization") String token,
                                                                   @RequestParam(name = "ar7Id") String ar7Id ,
                                                                   @RequestParam(name = "startDate") String startDate,
                                                                   @RequestParam(name = "endDate") String endDate,
                                                                   @RequestParam(name = "gameTypeId") int gameTypeId,
                                                                   @RequestParam(name = "page") int page,
                                                                   @RequestParam(name = "size") int size){
        UserPlayDetailTransitionGroupKey userPlayDetailTransitionGroupKey = UserPlayDetailTransitionGroupKey.builder()
                .ar7Id(ar7Id)
                .startDate(startDate)
                .endDate(endDate)
                .gameTypeId(gameTypeId)
                .build();
        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.DESC,"id");
        return userDetailReportService.getUserDetailReport(token, userPlayDetailTransitionGroupKey,pageable);
    }

    @GetMapping("/commission-report")
    public ResponseEntity<UserDetailsResponse> getCommissionConfirmReport(@RequestHeader("Authorization") String token,
                                                                          @RequestParam(name = "startDate", required = false) String  startDate,
                                                                          @RequestParam(name = "endDate", required = false) String endDate,
                                                                          @RequestParam(name = "role") String role,
                                                                          @RequestParam(name = "page") int page,
                                                                          @RequestParam(name = "size") int size){
        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.DESC,"id");
        return userDetailReportService.getCommissionConfirmReport(token, role, startDate, endDate, pageable);

    }







}


