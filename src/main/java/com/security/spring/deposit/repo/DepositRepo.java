package com.security.spring.deposit.repo;

import com.security.spring.deposit.entity.Deposit;
import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DepositRepo extends JpaRepository<Deposit,Long> {
    Page<Deposit> getDepositByFromAccOrToAcc(User fromAcc, User toAcc, Pageable pageable);
    Page<Deposit> getDepositByToAccAndStatus(User toAcc, DepositStatus status, Pageable pageable);
    Page<Deposit> getDepositByFromAccAndStatus(User fromAcc, DepositStatus status, Pageable pageable);
    Page<Deposit> getDepositByTransferTimeBetweenAndFromAccOrToAcc(LocalDateTime startDate, LocalDateTime endDate, User user, User user1, Pageable paging);

    Page<Deposit> getDepositByTransferTimeBetweenAndToAccAndStatus(LocalDateTime startDate, LocalDateTime endDate, User user, DepositStatus depositStatus, Pageable paging);

    Page<Deposit> findByTransferTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable paging);

    Page<Deposit> findByToAccAndStatus(User user, DepositStatus depositStatus, Pageable paging);

    Page<Deposit> findByActionTimeBetweenAndToAcc(LocalDateTime startDate, LocalDateTime endDate, User user, Pageable paging);

    Page<Deposit> findByTransferTimeBetweenAndFromAccAndStatusOrStatus(LocalDateTime startDate, LocalDateTime endDate, User currentUser, DepositStatus depositStatus, DepositStatus depositStatus1, Pageable paging);

    Page<Deposit> findByTransferTimeBetweenAndFromAccAndStatus(LocalDateTime startDate, LocalDateTime endDate, User currentUser, DepositStatus depositStatus, Pageable paging);

    Page<Deposit> findByFromAccAndStatus(User currentUser, DepositStatus depositStatus, Pageable paging);

    Page<Deposit> findByFromAccAndStatusOrStatus(User currentUser, DepositStatus depositStatus, DepositStatus depositStatus1, Pageable paging);

    boolean existsByUserTransitionId(String userTransitionId);

    Page<Deposit> findByTransferTimeBetweenAndFromAcc(LocalDateTime startDate, LocalDateTime endDate, User currentUser, Pageable paging);

    Page<Deposit> findByFromAcc(User currentUser, Pageable paging);

    Page<Deposit> findByTransferTimeBetweenAndFromAccAndStatusIn(LocalDateTime startDate, LocalDateTime endDate, User currentUser, List<DepositStatus> complete, Pageable paging);

    Page<Deposit> findByFromAccAndStatusIn(User currentUser, List<DepositStatus> complete, Pageable paging);
}
