package com.security.spring.bank.bankName.repo;

import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.bank.bankName.entity.BankNameAuth;
import com.security.spring.bank.bankType.entity.BankType;
import com.security.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankNameAuthRepo extends JpaRepository<BankNameAuth,Long> {
    BankNameAuth findByOwnerUserAndBankTypeAndBankNameAndBankNameStatusIsTrue(User user, BankType bankType, BankName bankName);
    BankNameAuth findByOwnerUserAndBankTypeAndBankName(User user,BankType bankType, BankName bankName);
    List<BankNameAuth> findByOwnerUserAndBankType(User user, BankType bankType);
    Optional<BankNameAuth> findByOwnerUserAndBankName(User user, BankName bankName);
    Optional<BankNameAuth> findByOwnerUserAndBankNameAndBankNameStatusIsTrue(User user, BankName bankName);
    List<BankNameAuth> findByOwnerUserAndBankNameStatusIsTrue(User user);
    List<BankNameAuth> findByOwnerUserAndBankTypeAndBankNameStatusIsTrue(User user, BankType bankType);
    List<BankNameAuth> findByOwnerUser(User user);

    List<BankNameAuth> findByOwnerUserAndBankNameStatusIsTrueAndInitialStatus(User parentUser, int i);

    List<BankNameAuth> findByOwnerUser_Ar7IdAndBankType_IdAndInitialStatusAndBankNameStatusIsTrue(String parentUserId, int bankTypeId, int i);
}
