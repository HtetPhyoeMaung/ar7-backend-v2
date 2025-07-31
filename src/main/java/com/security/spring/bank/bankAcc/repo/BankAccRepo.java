package com.security.spring.bank.bankAcc.repo;

import com.security.spring.bank.bankAcc.entity.BankAcc;
import com.security.spring.bank.bankName.entity.BankName;
import com.security.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccRepo extends JpaRepository<BankAcc,Long> {
    Optional<BankAcc> findByIdAndOwnerUser(Integer bankAccId, User user);
    List<BankAcc> findByOwnerUser(User user);
    List<BankAcc> findByOwnerUserAndBankName(User user, BankName bankName);
    List<BankAcc> findByOwnerUserAndBankNameAndAccountStatusIsTrue(User user, BankName bankName);
    List<BankAcc> findByOwnerUserAndAccountStatusIsTrue(User user);

    boolean existsByBankName_IdAndAccountNumber(Integer bankNameId, String accountNum);

    List<BankAcc> findByOwnerUser_Ar7IdAndBankName_IdAndAccountStatusIsTrue(String parentUserId, int bankNameId);
}
