package com.security.spring.gamesoft.pullPendingRecords.rest;//package com.security.spring.gamesoft.pullPendingRecords.rest;
//
//import com.security.spring.gamesoft.pullPendingRecords.pojo.PullPendingRecordsRequest;
//import com.security.spring.gamesoft.pullPendingRecords.pojo.PullPendingRecordsResponse;
//import com.security.spring.gamesoft.pullPendingRecords.service.PullPendingRecordsService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/v1/pprc")
//@RequiredArgsConstructor
//public class PullPendingRecordsController {
//
//    @Autowired
//    private PullPendingRecordsService pullPendingRecordsServiceImpl;
//
//    @PostMapping("/PullPendingRecords")
//    public ResponseEntity<PullPendingRecordsResponse> pullPendingRecords(@RequestBody @Valid PullPendingRecordsRequest data, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            var errorResponse =  PullPendingRecordsResponse
//                    .builder()
//                    .errorCode(404)
//                    .errorMessage(bindingResult.getAllErrors()
//                            .stream()
//                            .map(ObjectError::getDefaultMessage)
//                            .collect(Collectors.joining(", ")))
//                    .build();
//
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//        PullPendingRecordsResponse ressponse = pullPendingRecordsServiceImpl.pullPendingRecordConfig(data);
//        return ResponseEntity.ok().body(ressponse);
//    }
//}
