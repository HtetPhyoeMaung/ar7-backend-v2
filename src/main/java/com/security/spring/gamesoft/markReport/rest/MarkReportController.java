package com.security.spring.gamesoft.markReport.rest;

import com.security.spring.gamesoft.markReport.pojo.MarkReportRequest;
import com.security.spring.gamesoft.markReport.pojo.MarkReportResponse;
import com.security.spring.gamesoft.markReport.service.MarkReportServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mr")
public class MarkReportController {
    @Autowired
    private MarkReportServiceImpl markReportService;

    @PostMapping("/MarkReport")
    public ResponseEntity<MarkReportResponse> markReport(@RequestBody @Valid MarkReportRequest data, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            MarkReportResponse bedResponse = new MarkReportResponse();
            bedResponse.setErrorCode("400");
            bedResponse.setErrorMessage(
                    bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining())
            );

            return ResponseEntity.badRequest().body(bedResponse);
        }
        MarkReportResponse response = markReportService.marReportConfig(data);
        return ResponseEntity.badRequest().body(response);
    }
}
