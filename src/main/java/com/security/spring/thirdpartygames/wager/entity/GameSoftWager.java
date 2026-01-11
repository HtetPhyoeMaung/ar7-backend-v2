package com.security.spring.thirdpartygames.wager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.spring.thirdpartygames.callback.dto.Currency;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="wager", indexes = {
        @Index(name = "idx_wager_wager_id", columnList = "wagerID"),
        @Index(name = "idx_wager_member_id", columnList = "member_id"),
        @Index(name = "idx_wager_product_id", columnList = "product_id"),
        @Index(name = "idx_wager_game_type_id", columnList = "game_type_id"),
        @Index(name = "idx_wager_status", columnList = "status"),
        @Index(name = "idx_wager_created_on", columnList = "createdOn"),
        @Index(name = "idx_wager_member_status", columnList = "member_id,status")
})
public class GameSoftWager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String  wagerID;

    private String wagerCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    @ToString.Exclude
    private User gameSoftWagerUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    private GameSoftGameProvider provider;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_type_id")
    @ToString.Exclude
    private GameType gameTypeId;
    private Currency currencyID;
    private String gameCode;
    private String gameRoundID;
    private double validBetAmount;
    private double betAmount;
    private String wagerType;
    private double prizeAmount;
    private String tipAmount;
    private String agentCode;
    private String  payload;
    private double commisionAmount;
    private long createdAt;
    private long updatedAt;


    private Long settlementDate;
    private String  status;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "gameSoftWager", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GameSoftTransaction> gameSoftTransactionList;
}
