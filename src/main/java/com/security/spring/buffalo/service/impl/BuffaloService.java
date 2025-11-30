package com.security.spring.buffalo.service.impl;

import com.security.spring.buffalo.dto.BuffaloBalanceDto;
import com.security.spring.buffalo.model.BuffaloSetting;
import com.security.spring.buffalo.param.BuffaloBalanceParam;
import com.security.spring.buffalo.repo.BuffaloSettingRepo;
import com.security.spring.buffalo.service.IBuffaloService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.report.dto.UserReportObj;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuffaloService implements IBuffaloService {

    private final UserRepository userRepository;
    private final BuffaloSettingRepo buffaloSettingRepo;


    @Override
    @Transactional
    public ResponseEntity<BuffaloBalanceDto> getBalance(BuffaloBalanceParam buffaloBalanceParam) {

        // retrieve User by playerId
        User user = userRepository.findByAr7Id(buffaloBalanceParam.getPlayerId())
                .orElseThrow(()->
                        new DataNotFoundException("User Not Found By Player Id : "
                                +buffaloBalanceParam.getPlayerId()));

        BuffaloSetting buffaloSetting = buffaloSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(()-> new DataNotFoundException("Default Buffalo Setting Not Found!"));

        if (!buffaloBalanceParam.getAgentCode().equals(buffaloSetting.getAgentCode()) ||
        !buffaloBalanceParam.getPassword().equals(buffaloSetting.getPassword())){
            throw new UnauthorizedException("You're Unauthorized!");
        }

        double balance = user.getUserUnits().getMainUnit();

        var responseDto = BuffaloBalanceDto.
                of(user.getAr7Id(),buffaloSetting.getAgentCode(),balance);

        return ResponseEntity.ok(responseDto);
    }
}
