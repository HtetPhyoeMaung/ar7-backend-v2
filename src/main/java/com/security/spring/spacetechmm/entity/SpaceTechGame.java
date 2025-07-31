package com.security.spring.spacetechmm.entity;

import com.security.spring.gamesoft.gameType.entity.GameType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "space_tech_game")
public class SpaceTechGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameCode;
    private String gameName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "game_type_id")
    private GameType gameType;

    private String imageName;

    @OneToMany(mappedBy = "spaceTechGame" , cascade = CascadeType.ALL)
    private List<GameTable> gameTables;


}