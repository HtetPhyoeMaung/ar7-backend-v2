package com.security.spring.report.rest;

import com.security.spring.report.dto.SpaceTechReportObj;
import com.security.spring.report.dto.SpaceTechGameReportResponse;
import com.security.spring.report.service.ShanReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/srp/spacetech-report")
@RequiredArgsConstructor
public class SpaceTechReportController {

    private final ShanReportService shanReportService;


    @PostMapping
    public ResponseEntity<SpaceTechGameReportResponse> shanReport(@RequestBody SpaceTechReportObj spaceTechReportObj){
        SpaceTechGameReportResponse response = shanReportService.saveSpaceTechGameReport(spaceTechReportObj);
        return ResponseEntity.ok(response);
    }


}
