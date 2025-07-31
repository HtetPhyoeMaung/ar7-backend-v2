package com.security.spring.term_and_condition.controller;

import com.security.spring.term_and_condition.dto.TermAndConditionRequest;
import com.security.spring.term_and_condition.dto.TermAndConditionResponse;
import com.security.spring.term_and_condition.dto.TermAndConditionType;
import com.security.spring.term_and_condition.service.TermAndConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/term&condition")
@RequiredArgsConstructor
public class TermAndConditionController {

    private final TermAndConditionService termAndConditionService;

    @PostMapping
    public ResponseEntity<TermAndConditionResponse> saveTermAndCondition(@RequestHeader("Authorization") String token,
                                                                         @RequestBody TermAndConditionRequest termAndConditionRequest){
        TermAndConditionResponse response = termAndConditionService.saveTermAndCondition(token,termAndConditionRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TermAndConditionResponse> updateTermAndCondition(@RequestHeader("Authorization") String token,
                                                                          @PathVariable("id") Long id,
                                                                          @RequestBody TermAndConditionRequest termAndConditionRequest){
        TermAndConditionResponse response = termAndConditionService.updateTermAndCondition(token,id,termAndConditionRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermAndConditionResponse> getTermAndConditionById(@RequestHeader("Authorization") String token,
                                                                            @PathVariable("id") Long id){
        TermAndConditionResponse response = termAndConditionService.getTermAndConditionById(token,id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TermAndConditionResponse> deleteTermAndConditionById(@RequestHeader("Authorization") String token,
                                                                            @PathVariable("id") Long id){
        TermAndConditionResponse response = termAndConditionService.deleteTermAndConditionById(token,id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<TermAndConditionResponse> getAllTermAndCondition(@RequestHeader("Authorization") String token){
        TermAndConditionResponse response = termAndConditionService.getAllTermAndCondition(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type")
    public ResponseEntity<TermAndConditionResponse> getTermAndConditionByType(@RequestHeader("Authorization") String token,
                                                                              @RequestParam("type")TermAndConditionType termAndConditionType){
        TermAndConditionResponse response = termAndConditionService.getByType(token, termAndConditionType);
        return ResponseEntity.ok(response);
    }
}
