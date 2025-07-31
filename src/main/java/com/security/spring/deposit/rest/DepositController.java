package com.security.spring.deposit.rest;

import com.security.spring.config.JWTService;
import com.security.spring.deposit.dto.DepositRequest;
import com.security.spring.deposit.dto.DepositResponse;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.deposit.service.DepositService;
import com.security.spring.utils.DateUitls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dp")
public class DepositController {

    private final JWTService jwtService;

    private final DepositService depositServiceImpl;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestHeader("Authorization") String token, @RequestBody @Valid DepositRequest data){

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);

        DepositResponse resObj = depositServiceImpl.saveDeposit(data,ar7Id);

        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/deposit")
    public ResponseEntity<DepositResponse> getAllDepositByOwn(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate",required = false)String startDate,
            @RequestParam (name = "endDate",required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam (name = "depositStatus", required = false)DepositStatus depositStatus ){

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        DepositResponse depositPage = depositServiceImpl.getDepositForMe(ar7Id, page, size, DateUitls.parseDateTime(startDate),DateUitls.parseDateTime(endDate),depositStatus);
        return ResponseEntity.ok().body(depositPage);
    }

    @GetMapping("/depositTo")
    public ResponseEntity<DepositResponse> getAllDepositTo(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate",required = false)String startDate,
            @RequestParam (name = "endDate",required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        DepositResponse depositPage = depositServiceImpl.getDepositTo(ar7Id, page, size,  DateUitls.parseDateTime(startDate),DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok().body(depositPage);
    }

    @GetMapping("/depositFrom")
    public ResponseEntity<DepositResponse> getAllDepositFrom(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        DepositResponse depositPage = depositServiceImpl.getDepositFrom(ar7Id, page, size);
        return ResponseEntity.ok().body(depositPage);
    }

    @GetMapping("/depositAll")
    public ResponseEntity<DepositResponse> getAllDepositStatusComplete(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate",required = false)String startDate,
            @RequestParam (name = "endDate",required = false)String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        DepositResponse depositPage = depositServiceImpl.getDepositAll(token,page, size,DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok().body(depositPage);
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> updateDeposit(@RequestHeader("Authorization") String token,@RequestBody DepositRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        DepositResponse resObj = depositServiceImpl.updateDeposit(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }

}
