package com.security.spring.spacetechmm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "game_table")
public class GameTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer miniBet;
    @Column(nullable = false)
    private Integer maxBet;
    @Column(nullable = false)
    private Integer bet;
    @Column(nullable = false)
    private Integer level;
    private LocalDateTime createDate;
    private LocalDateTime upDate;
    private String imageName;

    @ManyToOne (cascade = {CascadeType.PERSIST , CascadeType.REFRESH , CascadeType.MERGE , CascadeType.DETACH})
    @JoinColumn(name = "space_tech_game_id")
    private SpaceTechGame spaceTechGame;

}