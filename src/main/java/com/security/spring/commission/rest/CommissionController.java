package com.security.spring.commission.rest;

import com.security.spring.commission.dto.CommissionRequest;
import com.security.spring.commission.dto.CommissionResponse;
import com.security.spring.commission.entity.ActionEnum;
import com.security.spring.commission.service.CommissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/commission")
@RequiredArgsConstructor
public class CommissionController {
    private final CommissionService commissionService;

    @PostMapping
    public ResponseEntity<CommissionResponse> saveCommission(@RequestBody @Valid CommissionRequest data ,
                                                             @RequestHeader("Authorization") String token){
        CommissionResponse responseFormat = commissionService.saveCommission(data,token);
        return ResponseEntity.ok().body(responseFormat);
    }

    @PutMapping
    public ResponseEntity<CommissionResponse> updateCommission(@RequestBody @Valid CommissionRequest data ,
                                                               @RequestHeader("Authorization") String token){
        CommissionResponse response = commissionService.updateCommission(data, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionResponse> findCommissionById(@PathVariable(name = "id") int id){
        CommissionResponse response = commissionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("gameType/{gameTypeCode}")
    public ResponseEntity<CommissionResponse> getMyCommission(@PathVariable String  gameTypeCode,
                                                              @RequestHeader("Authorization") String token){
        CommissionResponse response = commissionService.checkCommission(gameTypeCode, token);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/down-line")
    public ResponseEntity<CommissionResponse> getDownLineCommission(@RequestHeader("Authorization") String  token,
                                                                    @RequestParam(name = "page",required = false)Integer page,
                                                                    @RequestParam(name = "size",required = false)Integer size
    ){
        Pageable pageable = PageRequest.of(page,size);
        CommissionResponse response = commissionService.getDownLineCommission(token,pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/down-line/{ar7Id}")
    public ResponseEntity<CommissionResponse> getCommissionByAr7Id(@PathVariable String ar7Id,
                                                                   @RequestHeader("Authorization") String token,
                                                                   @RequestParam(name = "page",required = false)Integer page,
                                                                   @RequestParam(name = "size",required = false)Integer size

    ){

        CommissionResponse response = commissionService.getCommissionByAr7Id(ar7Id,token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/calculate")
    public ResponseEntity<CommissionResponse> calculateCommission(@RequestHeader("Authorization") String token){
        var result= commissionService.calculateCommission(token);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/agent/{agentAr7Id}")
    public ResponseEntity<CommissionResponse> confirmOrCancelCommission(@RequestHeader("Authorization") String token,
                                                                        @PathVariable(name = "agentAr7Id") String agentAr7Id){
        var result = commissionService.confirmOrCancelCommission(token,agentAr7Id);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/pending")
    public ResponseEntity<CommissionResponse> getPendingCommissionList(@RequestHeader("Authorization") String token,
                                                                       @RequestParam(name = "page")int page,
                                                                       @RequestParam(name = "size")int size){
        Pageable pageable = PageRequest.of(page,size).withSort(Sort.Direction.DESC,"id");
        CommissionResponse response = commissionService.calculatedCommissionListForCompleteDownline(token,pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/spaceTech/calculate")
    public ResponseEntity<CommissionResponse> calculateSpaceTechGameCommission(@RequestHeader("Authorization") String token){
        CommissionResponse commissionResponse = commissionService.calculateSpaceTechGameCommission(token);
return null;
    }






}
