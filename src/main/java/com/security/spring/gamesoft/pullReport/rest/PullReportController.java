package com.security.spring.gamesoft.pullReport.rest;//package com.security.spring.gamesoft.pullReport.rest;
//
//import com.security.spring.gamesoft.pullReport.pojo.PullReportRequest;
//import com.security.spring.gamesoft.pullReport.pojo.PullReportResponse;
//import com.security.spring.gamesoft.pullReport.service.PullReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/pr")
//public class PullReportController {
//
//    @Autowired
//    private PullReportService pullReportServiceImpl;
//
//    @PostMapping("/pullreport")
//    public PullReportResponse pullreport(@RequestBody PullReportRequest data){
//        PullReportResponse responseObj = pullReportServiceImpl.pullReportConfig(data);
//        return responseObj;
//    }
//
//    @PostMapping("/pullreportbysettlementdate")
//    public PullReportResponse pullreportbysettlementdate(@RequestBody PullReportRequest data){
//        PullReportResponse responseObj = pullReportServiceImpl.pullReportConfig(data);
//        return responseObj;
//    }
//}
