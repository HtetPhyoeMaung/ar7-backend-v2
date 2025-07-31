package com.security.spring.withdraw.repository;

import com.security.spring.user.entity.User;
import com.security.spring.withdraw.entity.TempoSaveWithdraw;
import com.security.spring.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TempoSaveWithdrawRepo extends JpaRepository<TempoSaveWithdraw,Long> {
    TempoSaveWithdraw findByTempoWithdrawUserAndAmountAndSaveDate(User withdrawUser, double amount, LocalDateTime withdrawDate);
}
