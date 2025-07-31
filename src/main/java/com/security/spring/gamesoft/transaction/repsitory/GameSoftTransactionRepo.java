package com.security.spring.gamesoft.transaction.repsitory;

import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSoftTransactionRepo extends JpaRepository<GameSoftTransaction, Long> {
    Optional<GameSoftTransaction> findByTransactionId(String transactionID);

//    Optional<GameSoftTransaction> findByTransactionKey(String key);

//     jhhhhhhhhhhhhhhhhhhuyyyujyyyyyyyyyyyyyuujyyu

    List<GameSoftTransaction> findByGameSoftWager(GameSoftWager checkWager);

    List<GameSoftTransaction> findByGameSoftTransitionUser(User user);



    Page<GameSoftTransaction> findByGameSoftTransitionUser_Ar7Id(String ar7Id, Pageable pageable);

    List<GameSoftTransaction> findByCreatedOnBetweenAndWagerStatus(LocalDateTime startDate, LocalDateTime endDate, String  wagerStatus);

    List<GameSoftTransaction> findByStatus(String  wagerStatus);


    List<GameSoftTransaction> findByWagerStatusAndIsCommissionCalculate(String  wagerStatus, boolean b);

    List<GameSoftTransaction> findByGameSoftTransitionUserAndWagerStatusAndCreatedOnBetween(User user, String  wagerStatus, LocalDateTime startDate, LocalDateTime endDate);

    Page<GameSoftTransaction> findByCreatedOnBetweenAndGameSoftTransitionUser_Ar7IdAndGameType_IdAndStatus(LocalDateTime localDateTime, LocalDateTime localDateTime1, String ar7Id, int gameTypeId, String  wagerStatus, Pageable pageable);


}

