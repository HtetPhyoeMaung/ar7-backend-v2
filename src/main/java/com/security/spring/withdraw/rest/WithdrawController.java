package com.security.spring.withdraw.rest;

import com.security.spring.config.JWTService;
import com.security.spring.deposit.dto.DepositResponse;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.utils.DateUitls;
import com.security.spring.withdraw.dto.WithdrawRequest;
import com.security.spring.withdraw.dto.WithdrawResponse;
import com.security.spring.withdraw.dto.WithdrawStatusRequest;
import com.security.spring.withdraw.service.WithdrawService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wd")
@RequiredArgsConstructor
public class WithdrawController {


    private final JWTService jwtService;


    private final WithdrawService withdrawServiceImpl;


    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> saveWithdraw(@RequestHeader("Authorization") String token, @RequestBody @Valid WithdrawRequest data){

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        WithdrawResponse resObj = withdrawServiceImpl.saveWithdraw(data,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> getWithdraw(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate", required = false)String startDate,
            @RequestParam (name = "endDate", required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "withdrawStatus",required = false)DepositStatus withdrawStatus ){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        WithdrawResponse withdrawPage = withdrawServiceImpl.getWithdrawOwn(ar7Id,page, size, DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate),withdrawStatus);
        return ResponseEntity.ok().body(withdrawPage);
    }

    @GetMapping("/withdrawAll")
    public ResponseEntity<WithdrawResponse> getAllWithdrawStatusComplete(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate",required = false)String startDate,
            @RequestParam (name = "endDate",required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        WithdrawResponse depositPage = withdrawServiceImpl.getDepositAll(token,page, size,DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok().body(depositPage);
    }

    @GetMapping("/withdrawTo")
    public ResponseEntity<WithdrawResponse> getWithdrawTo(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate", required = false)String startDate,
            @RequestParam (name = "endDate", required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        WithdrawResponse withdrawResponse = withdrawServiceImpl.getWithdrawTo(token,page, size, DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok().body(withdrawResponse);
    }

    @GetMapping("/withdrawFrom")
    public ResponseEntity<WithdrawResponse> getWithdrawFrom(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        WithdrawResponse withdrawResponse = withdrawServiceImpl.getWithdrawFrom(ar7Id,page, size);
        return ResponseEntity.ok().body(withdrawResponse);
    }

    @GetMapping("/withdraw/{withdrawId}")
    public ResponseEntity<WithdrawResponse> getWithdraw(@RequestHeader("Authorization") String token,@PathVariable Long withdrawId){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        WithdrawResponse resObj = withdrawServiceImpl.getWithdrawById(withdrawId,ar7Id);

        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> updateWithdraw(@RequestHeader("Authorization") String token,@RequestBody WithdrawStatusRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        WithdrawResponse resObj = withdrawServiceImpl.updateWithdraw(data,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }
}
