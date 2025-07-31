package com.security.spring.gamesoft.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.spring.gamesoft.callback.dto.Currency;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="gamesoft_transaction")
public class GameSoftTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @JsonIgnore
    @ToString.Exclude
    private User gameSoftTransitionUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="wagerId")
    @JsonIgnore
    @ToString.Exclude
    private GameSoftWager gameSoftWager;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_provider_id")
    @JsonIgnore
    @ToString.Exclude
    private GameSoftGameProvider productID;

    private Currency currency;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_type_id")
    @JsonIgnore
    @ToString.Exclude
    private GameType gameType;
    private String transactionId;
    private boolean isCommissionCalculate;
    private String action;
    private double amount;
    private double betAmount;
    private String channelCode;
    private String roundId;
    private String wagerCode;
    private String wagerStatus;
    private double commisionAmount;
    private double beforeBalance;
    private double afterBalance;
    private double validBetAmount;
    private double prizeAmount;
    private double tipAmount;
    private String payload;
    private Long settleAt;
    private String gameCode;
    @Column(nullable = false)
    private String  status;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    private LocalDateTime modifiedOn;
}