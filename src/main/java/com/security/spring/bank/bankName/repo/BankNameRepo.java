package com.security.spring.bank.bankName.repo;

import com.security.spring.bank.bankName.entity.BankName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankNameRepo extends JpaRepository<BankName,Long> {
    List<BankName> findByBankTypeId(Integer bankTypeId);

    Optional<BankName> findByBankName(String bankName);

    List<BankName> findByBankType_bankTypeName(String mobileBanking);
}
