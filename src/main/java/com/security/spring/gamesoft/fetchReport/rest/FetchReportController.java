package com.security.spring.gamesoft.fetchReport.rest;//package com.security.spring.gamesoft.fetchReport.rest;
//
//import com.security.spring.gamesoft.fetchReport.pojo.FetchReportResponse;
//import com.security.spring.gamesoft.fetchReport.service.FetchReportServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/frc")
//@RequiredArgsConstructor
//public class FetchReportController {
//
//    @Autowired
//    private FetchReportServiceImpl fetchReportService;
//
//    @GetMapping("/FetchReport")
//    public ResponseEntity<FetchReportResponse> fetchReport(){
//        FetchReportResponse response = fetchReportService.fetchReportServiceConfig();
//        return ResponseEntity.ok().body(response);
//    }
//}
