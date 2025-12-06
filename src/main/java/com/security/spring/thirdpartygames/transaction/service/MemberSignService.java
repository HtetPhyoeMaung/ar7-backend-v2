package com.security.spring.thirdpartygames.transaction.service;

import com.security.spring.thirdpartygames.transaction.entity.MemberSign;

import java.util.Optional;

public interface MemberSignService {
    Optional<MemberSign> findByMemberName(String memberName);

    MemberSign save(MemberSign createSign);
}
