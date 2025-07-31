package com.security.spring.bank.bankType.repo;

import com.security.spring.bank.bankType.entity.BankType;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTypeRepo extends JpaRepository<BankType,Long> {
    boolean existsByBankTypeName(@NotEmpty(message = "Please Enter Bank Type Name") String bankTypeName);
}
