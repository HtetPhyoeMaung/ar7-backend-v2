package com.security.spring.gamesoft.pullReportByWagerIDs.rest;//package com.security.spring.gamesoft.pullReportByWagerIDs.rest;
//
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsRequest;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsResponse;
//import com.security.spring.gamesoft.pullReportByWagerIDs.service.PullReportByWagerIDsServices;
//import jakarta.validation.Valid;
//import org.springframework.validation.ObjectError;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/v1/prbw")
//@RequiredArgsConstructor
//public class PullReportByWagerIDsController {
//
//    @Autowired
//    private PullReportByWagerIDsServices pullReportByWagerIDsServicesImpl;
//
//
//    @PostMapping("/PullReportByWagerIDs")
//    public ResponseEntity<PullReportByWagerIDsResponse> pullreportbywaggerIds(@RequestBody @Valid PullReportByWagerIDsRequest data, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            PullReportByWagerIDsResponse response = new PullReportByWagerIDsResponse();
//            response.setErrorCode(400);
//            response.setErrorMessage(
//                    bindingResult.getAllErrors()
//                            .stream()
//                            .map(ObjectError::getDefaultMessage)
//                            .collect(Collectors.joining(", "))
//            );
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        PullReportByWagerIDsResponse response = pullReportByWagerIDsServicesImpl.wagers(data);
//        return ResponseEntity.ok().body(response);
//    }
//}
