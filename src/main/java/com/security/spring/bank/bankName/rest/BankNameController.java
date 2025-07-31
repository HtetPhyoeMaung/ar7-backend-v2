package com.security.spring.bank.bankName.rest;

import com.security.spring.bank.bankName.dto.*;
import com.security.spring.bank.bankName.service.BankNameServiceImpl;
import com.security.spring.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bn")
@RequiredArgsConstructor
public class BankNameController {

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private BankNameServiceImpl bankNameService;

    @PostMapping(value = "/bankName", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBankName(@ModelAttribute BankNameRequest data){
        BankNameResponse resObj = bankNameService.saveBankName(data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/bankName")
    public ResponseEntity<?> getBankNameAll(@RequestHeader("Authorization") String token){
        BankNameResponse resObj = bankNameService.getBankNameAll(token);
        return ResponseEntity.ok().body(resObj);
    }
    @GetMapping("/bankName/{id}")
    public ResponseEntity<?> getBankNameById(@PathVariable(name = "id") Long id){
        BankNameResponse resObj = bankNameService.getBankNameById(id);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping(value = "/bankName", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBankName(@ModelAttribute BankNameRequest request){
        BankNameResponse bankNameResponse = bankNameService.updateBankName(request);
        return ResponseEntity.ok().body(bankNameResponse);
    }

    @PostMapping("/bankNameAuth")
    public ResponseEntity<?> createBankNameAuth(@RequestHeader("Authorization") String token, @RequestBody BankNameAuthRequest data){

        String jwtToken = token.substring(7);
        String ar7id = jwtService.extractUsername(jwtToken);

        BankNameAuthResponse resObj = bankNameService.saveBankNameAuth(data,ar7id);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/bankNameAuth")
    public ResponseEntity<?> updateBankNameAuth(@RequestHeader("Authorization") String token,@RequestBody BankNameAuthRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankNameAuthResponse resObj = bankNameService.updateBankNameAuth(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/bankNameAuth")
    public ResponseEntity<?> getBankNameAuthObj(@RequestHeader("Authorization") String token){

        String jwtToken = token.substring(7);
        String ar7id = jwtService.extractUsername(jwtToken);

        BankNameAuthResponse resObj = bankNameService.getBankNameAuth(ar7id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/auth")
    public ResponseEntity<BankNameAuthResponse> getAuthenticatedBankNameList(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String ar7id = jwtService.extractUsername(jwtToken);

        BankNameAuthResponse bankNameAuthResponse = bankNameService.getAuthenticatedBankNameList(ar7id);
        return ResponseEntity.ok(bankNameAuthResponse);
    }

    @GetMapping("/parent/auth")
    public ResponseEntity<BankNameAuthResponse> getParentAuthenticatedBankNameList(@RequestParam(name = "bankTypeId") int bankTypeId){
        BankNameAuthResponse bankNameAuthResponse = bankNameService.getParentAuthenticatedBankNameList(bankTypeId);
        return ResponseEntity.ok(bankNameAuthResponse);
    }
}
