package com.security.spring.thirdpartygames.transaction.service;

import com.security.spring.thirdpartygames.transaction.entity.MemberSign;
import com.security.spring.thirdpartygames.transaction.repsitory.MemberSignRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSignServiceImpl implements MemberSignService{
    private final MemberSignRepo memberSignRepo;


    @Override
    public Optional<MemberSign> findByMemberName(String memberName) {
        return memberSignRepo.findByMemberName(memberName);
    }

    @Override
    public MemberSign save(MemberSign createSign) {
        return memberSignRepo.save(createSign);
    }
}
