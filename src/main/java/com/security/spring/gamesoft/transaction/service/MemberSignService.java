package com.security.spring.gamesoft.transaction.service;

import com.security.spring.gamesoft.transaction.entity.MemberSign;

import java.util.Optional;

public interface MemberSignService {
    Optional<MemberSign> findByMemberName(String memberName);

    MemberSign save(MemberSign createSign);
}
