package com.security.spring.bank.bankType.repo;

import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.bank.bankType.entity.BankTypeAuth;
import com.security.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankTypeAuthRepo extends JpaRepository<BankTypeAuth , Long> {
    Optional<BankTypeAuth> findByOwnerUserAndBankType(User user, BankType bankType);
    Optional<BankTypeAuth> findByOwnerUserAndBankTypeAndBankTypeStatusIsTrue(User user,BankType bankType);
    List<BankTypeAuth> findByOwnerUser(User user);
    List<BankTypeAuth> findByOwnerUserAndBankTypeStatusIsTrue(User user);

    boolean existsByOwnerUser_Ar7IdAndBankType_Id(String ar7Id, Integer id);

    List<BankTypeAuth> findByOwnerUserAndBankTypeStatusIsTrueAndInitStatus(User parentUser, int i);

    List<BankTypeAuth> findByOwnerUser_Ar7IdAndInitStatusAndBankTypeStatusIsTrue(String parentUserId, int i);
}
