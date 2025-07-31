package com.security.spring.bank.bankType.rest;

import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.bank.bankType.dto.BankTypeAuthRequest;
import com.security.spring.bank.bankType.dto.BankTypeAuthResponse;
import com.security.spring.bank.bankType.dto.BankTypeRequest;
import com.security.spring.bank.bankType.dto.BankTypeResponse;
import com.security.spring.bank.bankType.service.BankTypeService;
import com.security.spring.bank.bankType.service.BankTypeServiceImpl;
import com.security.spring.config.JWTService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bt")
@RequiredArgsConstructor
public class BankTypeController {
    @Autowired
    private BankTypeService bankTypeService;
    private final JWTService jwtService;

    @PostMapping("/bankType")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveBankType(@RequestBody @Valid BankTypeRequest data){
        BankTypeResponse responseObj = bankTypeService.saveBankType(data);
        return ResponseEntity.ok().body(responseObj);
    }

    @GetMapping("/bankType")
    public ResponseEntity<BankTypeResponse> getBankTypeAll(){
        BankTypeResponse responseObj = bankTypeService.getBankTypeAll();
        return ResponseEntity.ok(responseObj);
    }

    @GetMapping("/bankType/{bankTypeId}")
    public ResponseEntity<BankTypeResponse> getBankTypeById(@PathVariable Long bankTypeId){
        BankTypeResponse resObj = bankTypeService.findBankTypeById(bankTypeId);
        return ResponseEntity.ok(resObj);
    }

    @PutMapping("/bankType")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBankType(@RequestBody BankTypeRequest data){
        BankTypeResponse resObj = bankTypeService.updateBankType(data);

        return ResponseEntity.ok(resObj);
    }


    @PostMapping("/bankTypeAuth")
    public ResponseEntity<?> createBankTypeAuth(@RequestHeader("Authorization") String token,@RequestBody BankTypeAuthRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        BankTypeAuthResponse resObj = bankTypeService.saveBankTypeAuth(data,ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/bankTypeAuth")
    public ResponseEntity<?> updateBankTypeAuth(@RequestHeader("Authorization") String token,@RequestBody BankTypeAuthRequest data){
        String jwtToken = token.substring(7);
        String ar7id = jwtService.extractUsername(jwtToken);
        BankTypeAuthResponse resObj = bankTypeService.changeStatusBankTypeAuth(ar7id, data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/bankTypeAuth")
    public ResponseEntity<?> getBankTypeAuth(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String ar7id = jwtService.extractUsername(jwtToken);

        BankTypeAuthResponse resObj = bankTypeService.getBankTypeAuthAll(ar7id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/parent/auth")
    public ResponseEntity<BankTypeAuthResponse> getParentBankTypeAuthList(){
        BankTypeAuthResponse bankTypeResponse = bankTypeService.getParentBankTypeAuthList();
        return ResponseEntity.ok(bankTypeResponse);
    }
    
}
