package com.security.spring.bank.bankAcc.rest;

import com.security.spring.bank.bankAcc.dto.BankAccRequest;
import com.security.spring.bank.bankAcc.dto.BankAccResponse;
import com.security.spring.bank.bankAcc.dto.BankAccStatusRequest;
import com.security.spring.bank.bankAcc.service.BankAccService;
import com.security.spring.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ba")
@RequiredArgsConstructor
public class BankAccountControllers {


    private final JWTService jwtService;


    private final BankAccService bankAccService;


    @PostMapping(value = "/bankAcc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveBankAcc(@RequestHeader("Authorization") String token,@ModelAttribute BankAccRequest request){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.saveBankAcc(request,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/bankAcc")
    public ResponseEntity<BankAccResponse> getBankAccByOwn(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.getBankAccByOwn(ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/bankAcc/{bankAccId}")
    public ResponseEntity<BankAccResponse> getBankAccById(@RequestHeader("Authorization") String token,@PathVariable Integer bankAccId){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.getBankAccById(bankAccId,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

//    @GetMapping("/bankAcc/byName/{bankNameId}")
//    public ResponseEntity<BankAccResponse> getBankAccByName(@RequestHeader("Authorization") String token,@PathVariable Integer bankNameId){
//        String jwtToken = token.substring(7);
//        String ar7Id = jwtService.extractUsername(jwtToken);
//        BankAccResponse resObj = bankAccService.getBankAccByName(bankNameId,ar7Id);
//        return ResponseEntity.ok().body(resObj);
//    }

//    @GetMapping("/bankAccParent")
//    public ResponseEntity<BankAccResponse> getBankAccByParent(@RequestHeader("Authorization") String token,@PathVariable Integer bankNameId){
//        String jwtToken = token.substring(7);
//        String ar7Id = jwtService.extractUsername(jwtToken);
//        BankAccResponse resObj = bankAccService.getBankAccByParent(bankNameId,ar7Id);
//        return ResponseEntity.ok().body(resObj);
//    }

    @GetMapping("/bankAccParent")
    public ResponseEntity<BankAccResponse> getBankAccAllByParent(@RequestHeader("Authorization") String token,
                                                                 @RequestParam(name = "bankNameId") int bankNameId){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.getBankAccAllByParent(ar7Id, bankNameId);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping(value = "/bankAcc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBankAcc(@RequestHeader("Authorization") String token,@ModelAttribute BankAccRequest request){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.updateBankAcc(ar7Id,request);
        return ResponseEntity.ok().body(resObj);
    }



    @PutMapping(value = "/bankAccStatus")
    public ResponseEntity<?> changeBankAccStatus(@RequestHeader("Authorization") String token, @RequestBody BankAccStatusRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankAccResponse resObj = bankAccService.changeBankAccStatus(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }
}
