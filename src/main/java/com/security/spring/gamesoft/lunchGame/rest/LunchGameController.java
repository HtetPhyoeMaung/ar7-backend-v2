package com.security.spring.gamesoft.lunchGame.rest;

import com.security.spring.config.JWTService;
import com.security.spring.gamesoft.lunchGame.dto.LunchGameRequest;
import com.security.spring.gamesoft.lunchGame.dto.LunchGameResponse;
import com.security.spring.gamesoft.lunchGame.service.LunchGameService;
import com.security.spring.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lg")
@RequiredArgsConstructor
public class LunchGameController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LunchGameService lunchGameService;

    @PostMapping("/lunchgame")
    public LunchGameResponse requestLunchGameToThirdParty(@RequestBody LunchGameRequest data){
        String ar7Id = ContextUtils.getAr7IdFromContext();

        return lunchGameService.lunchGameService(data,ar7Id);
    }
}
