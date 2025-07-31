package com.security.spring.gamesoft.transaction.repsitory;

import com.security.spring.gamesoft.transaction.entity.MemberSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberSignRepo extends JpaRepository<MemberSign,Long> {
    Optional<MemberSign> findByMemberName(String memberName);
}
