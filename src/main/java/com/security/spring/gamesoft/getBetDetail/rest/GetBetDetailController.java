package com.security.spring.gamesoft.getBetDetail.rest;//package com.security.spring.gamesoft.getBetDetail.rest;
//
//import com.security.spring.gamesoft.getBetDetail.service.GetBetDetailService;
//import com.security.spring.gamesoft.wagger.entity.GameSoftWagger;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/gbd")
//@RequiredArgsConstructor
//public class GetBetDetailController {
//
//    @Autowired
//    private GetBetDetailService getBetDetailServiceImpl;
//
//    @GetMapping("Report/BetDetail/{pathWaggerId}")
//    public List<GameSoftWagger> getbetdetail(@PathVariable String pathWaggerId){
//        String waggerId = pathWaggerId;
//        List<GameSoftWagger> waggerList = getBetDetailServiceImpl.getBetDetail(waggerId);
//        return waggerList;
//    }
//}
