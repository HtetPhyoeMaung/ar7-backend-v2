package com.security.spring.report.entity;

import com.security.spring.gamesoft.gameType.entity.GameType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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
