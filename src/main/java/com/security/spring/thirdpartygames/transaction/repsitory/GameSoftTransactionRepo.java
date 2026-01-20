package com.security.spring.thirdpartygames.transaction.repsitory;

import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;
import com.security.spring.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSoftTransactionRepo extends JpaRepository<GameSoftTransaction, Long> {
    Optional<GameSoftTransaction> findByTransactionId(String transactionID);
    Optional<GameSoftTransaction> findBySpinId(String spinId);

//    Optional<GameSoftTransaction> findByTransactionKey(String key);

//     jhhhhhhhhhhhhhhhhhhuyyyujyyyyyyyyyyyyyuujyyu

    List<GameSoftTransaction> findByGameSoftWager(GameSoftWager checkWager);

    List<GameSoftTransaction> findByGameSoftTransitionUser(User user);



    Page<GameSoftTransaction> findByGameSoftTransitionUser_Ar7Id(String ar7Id, Pageable pageable);

    List<GameSoftTransaction> findByCreatedOnBetweenAndWagerStatus(LocalDateTime startDate, LocalDateTime endDate, String  wagerStatus);

    List<GameSoftTransaction> findByStatus(String  wagerStatus);


    List<GameSoftTransaction> findByStatusAndIsCommissionCalculate(String  wagerStatus, boolean b);

    List<GameSoftTransaction> findByGameSoftTransitionUserAndStatusAndCreatedOnBetween(User user, String  wagerStatus, LocalDateTime startDate, LocalDateTime endDate);

    Page<GameSoftTransaction> findByCreatedOnBetweenAndGameSoftTransitionUser_Ar7IdAndGameType_IdAndStatus(LocalDateTime localDateTime, LocalDateTime localDateTime1, String ar7Id, int gameTypeId, String  wagerStatus, Pageable pageable);

    @Query("""
    	    select new com.security.spring.report.dto.UserReportObj(
    	        t.gameSoftTransitionUser.ar7Id,
    	        coalesce(t.gameType.id, 1),
    	        coalesce(t.gameType.description, 'စလော့'),
    	        count(t.id),
    	        sum(t.betAmount),
    	        sum(t.amount)
    	    )
    	    from GameSoftTransaction t
    	    where (:userId is null or t.gameSoftTransitionUser.id = :userId)
    	      and (:status is null or t.status = :status)
    	      and (cast(:start as LocalDateTime) is null or t.createdOn >= :start)
    	      and (cast(:end as LocalDateTime) is null or t.createdOn <= :end)
    	    group by t.gameSoftTransitionUser.ar7Id, t.gameType.id, t.gameType.description
    	    """)
    	List<com.security.spring.report.dto.UserReportObj> aggregateUserReport(
    	        @Param("userId") Integer userId, // User Object အစား ID (Long) ကို သုံးပါ
    	        @Param("status") String status,
    	        @Param("start") LocalDateTime start,
    	        @Param("end") LocalDateTime end
    	);
}

