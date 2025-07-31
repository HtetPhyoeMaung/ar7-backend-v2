package com.security.spring.unit.rest;

import com.security.spring.config.JWTService;
import com.security.spring.unit.dto.*;
import com.security.spring.unit.services.UnitService;
import com.security.spring.utils.DateUitls;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/un")
@RequiredArgsConstructor
public class UnitController {


    private final JWTService jwtService;

    private final UnitService unitService;

    @GetMapping("/checkUser/{ar7Id}")
    public ResponseEntity<AccountCheckResponse> checkUser(@PathVariable String ar7Id){
        AccountCheckResponse resObj = unitService.accountCheck(ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/unit")
    public ResponseEntity<UnitResponse> saveUnit(@RequestHeader("Authorization") String token,@RequestBody @Valid UnitRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        UnitResponse resObj = unitService.createUnit(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/unit")
    public ResponseEntity<UnitResponse> getOwnUnit(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        UnitResponse resObj = unitService.getOwnUnit(ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/unit/{ar7Id}")
    public ResponseEntity<?> getUnitById(@PathVariable String ar7Id){
        UnitResponse resObj = unitService.getOwnUnit(ar7Id);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/unitTransferPlus")
    public ResponseEntity<?> unitTransferPlus(@RequestHeader("Authorization") String token,@RequestBody @Valid TransitionRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        TransitionResponse resObj = unitService.unitTransferPlus(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }

    @PutMapping("/fastTransferPlus")
    public ResponseEntity<?> fastTransferUnit(@RequestBody @Valid TransitionRequest data){
        TransitionResponse resObj = unitService.fastTransferPlus(data);
        return ResponseEntity.ok().body(resObj);
    }



    @PutMapping("/unitTransferMinus")
    public ResponseEntity<?> unitTransferMinus(@RequestHeader("Authorization") String token,@RequestBody @Valid TransitionRequest data){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);
        TransitionResponse resObj = unitService.unitTransferMinus(ar7Id,data);
        return ResponseEntity.ok().body(resObj);
    }

    @GetMapping("/unitCreateHistory")
    public ResponseEntity<?> getCreateUnitHistoryAll(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate", required = false) String startDate,
            @RequestParam (name = "endDate", required = false) String endDate,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){
        String jwtToken = token.substring(7);
        String ar7Id = jwtService.extractUsername(jwtToken);


        TransitionResponse transitionResponse = unitService.getAllCreateUnitHistory(ar7Id, page, size,
                DateUitls.parseDateTime(startDate), DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok()
                .body(transitionResponse);
    }


    @GetMapping("/transitionHistoryAll")
    public ResponseEntity<TransitionResponse> transitionHistoryAll(
            @RequestParam (name = "startDate", required = false) String startDate,
            @RequestParam (name = "endDate", required = false) String endDate,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC,"id");
        TransitionResponse transitionResponse  = unitService.getAllTransitionHistory(pageable,DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok()
                .body(transitionResponse);
    }

    @GetMapping("/transitionHistoryOwn")
    public ResponseEntity<TransitionResponse> transitionHistoryOwn(
            @RequestHeader("Authorization") String token,
            @RequestParam (name = "startDate", required = false) String startDate,
            @RequestParam (name = "endDate", required = false) String endDate,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){


        TransitionResponse transitionHistoryPage = unitService.getAllTransitionHistoryOwn(token,page,size, DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok()
                .body(transitionHistoryPage);
    }

    @GetMapping("/transitionHistory/{ar7Id}")
    public ResponseEntity<TransitionResponse> transitionHistoryByAr7Id(
            @PathVariable String ar7Id,
            @RequestParam (name = "startDate", required = false) String startDate,
            @RequestParam (name = "endDate", required = false) String endDate,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){

        TransitionResponse transitionResponse = unitService.getAllTransitionHistoryByUserId(ar7Id,page,size, DateUitls.parseDateTime(startDate),
                DateUitls.parseDateTime(endDate));
        return ResponseEntity.ok()
                .body(transitionResponse);
    }
}
