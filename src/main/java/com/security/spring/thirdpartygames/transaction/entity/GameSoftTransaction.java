package com.security.spring.thirdpartygames.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.spring.thirdpartygames.callback.dto.Currency;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;
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
@Table(
        name = "gamesoft_transaction",
        indexes = {
                @Index(name = "uk_gst_transaction_id", columnList = "transaction_id", unique = true),
                @Index(name = "uk_gst_spin_id", columnList = "spin_id", unique = true),
                @Index(name = "idx_gst_created_status", columnList = "created_on,status"),
                @Index(name = "idx_gst_user_created", columnList = "user_id,created_on"),
                @Index(name = "idx_gst_user_game_status_created", columnList = "user_id,game_type_id,status,created_on")
        }
)
public class GameSoftTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    @ToString.Exclude
    private User gameSoftTransitionUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wagerId")
    @JsonIgnore
    @ToString.Exclude
    private GameSoftWager gameSoftWager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_provider_id")
    @JsonIgnore
    @ToString.Exclude
    private GameSoftGameProvider productID;

    private Currency currency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_type_id")
    @JsonIgnore
    @ToString.Exclude
    private GameType gameType;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "spin_id")
    private String spinId;
    private boolean isCommissionCalculate;
    private String action;
    private double amount;
    private double betAmount;
    private String channelCode;
    private String roundId;
    private String wagerCode;
    @Column(name = "wager_status")
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
    @Column(name = "status", nullable = false)
    private String  status;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;
}