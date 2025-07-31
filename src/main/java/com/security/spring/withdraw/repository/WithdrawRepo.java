package com.security.spring.withdraw.repository;

import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.user.entity.User;
import com.security.spring.withdraw.entity.Withdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRepo extends JpaRepository<Withdraw,Long> {
    Withdraw findByIdAndWithdrawUserOrParentUser(Long id,User withdrawUser, User parentUser);
    Withdraw findByIdAndParentUserAndWithdrawStatus(Long id, User parentUser, DepositStatus withdrawStatus);
    Page<Withdraw> findByWithdrawUserOrParentUser(User fromAcc, User toAcc, Pageable pageable);
    Page<Withdraw> findByWithdrawUserAndWithdrawStatus(User fromAcc,DepositStatus withdrawStatus, Pageable pageable);
    Page<Withdraw> findByActionTimeBetweenAndWithdrawUserOrParentUser(LocalDateTime startDate, LocalDateTime endDate, User currentUserGet, User currentUserGet1, Pageable paging);
    Page<Withdraw> findByActionTimeBetweenAndParentUser(LocalDateTime startDate, LocalDateTime endDate, User user, Pageable paging);

    Page<Withdraw> findByParentUserAndWithdrawStatus(User user, DepositStatus depositStatus, Pageable paging);

    Page<Withdraw> findByActionTimeBetweenAndParentUserAndWithdrawStatus(LocalDateTime startDate, LocalDateTime endDate, User user, DepositStatus depositStatus, Pageable paging);

    Page<Withdraw> findByWithdrawUserAndWithdrawStatusOrWithdrawStatus(User currentUser, DepositStatus depositStatus, DepositStatus depositStatus1, Pageable paging);

    Page<Withdraw> findByActionTimeBetweenAndWithdrawUserAndWithdrawStatus(LocalDateTime startDate, LocalDateTime endDate, User currentUser, DepositStatus withdrawStatus, Pageable paging);

    Page<Withdraw> findByActionTimeBetweenAndWithdrawUserAndWithdrawStatusOrWithdrawStatus(LocalDateTime startDate, LocalDateTime endDate, User currentUser, DepositStatus depositStatus, DepositStatus depositStatus1, Pageable paging);

    Page<Withdraw> findByActionTimeBetweenAndWithdrawUser(LocalDateTime startDate, LocalDateTime endDate, User currentUser, Pageable paging);

    Page<Withdraw> findByWithdrawUser(User currentUser, Pageable paging);

    Page<Withdraw> findByActionTimeBetweenAndWithdrawUserAndWithdrawStatusIn(LocalDateTime startDate, LocalDateTime endDate, User currentUser, List<DepositStatus> complete, Pageable paging);

    Page<Withdraw> findByWithdrawUserAndWithdrawStatusIn(User currentUser, List<DepositStatus> complete, Pageable paging);
}
