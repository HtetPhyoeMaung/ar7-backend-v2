package com.security.spring.report.entity;

import com.security.spring.thirdpartygames.gameType.entity.GameType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(indexes = {
        @Index(name = "idx_stgr_calculate_status", columnList = "calculateStatus"),
        @Index(name = "idx_stgr_game_type", columnList = "game_type_id"),
        @Index(name = "idx_stgr_user_id", columnList = "userId"),
        @Index(name = "idx_stgr_status", columnList = "status")
})
public class SpaceTechGameReport implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNo;
    private int matchNo;
    private String userId;
    private int betAmount;
    private int commission;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "game_type_id")
    private GameType gameType;
    private String status;
    private int winAmount;
    private String domain;
    private boolean calculateStatus;
}
