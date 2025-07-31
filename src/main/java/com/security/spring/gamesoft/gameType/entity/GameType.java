package com.security.spring.gamesoft.gameType.entity;

import com.security.spring.commission.entity.UserCommission;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.report.entity.SpaceTechGameReport;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "game_type")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  code;
    private String description;

    @OneToMany(mappedBy = "gameType", orphanRemoval = true)
    private List<SpaceTechGame> spaceTechGameList;

    @OneToMany(mappedBy = "gameType", cascade = CascadeType.ALL)
    private List<SpaceTechGameReport> spaceTechGameReportList;

    @OneToMany(mappedBy = "gameType" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<GameSoftGameProvider> gameProviderList;

    @OneToMany(mappedBy = "gameType" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<GameSoftTransaction> gameSoftTransactionList ;

    @OneToMany(mappedBy = "gameType" , cascade =  CascadeType.ALL)
    @ToString.Exclude
    private List<UserCommission> userCommissionList;

    @OneToMany(mappedBy = "gameTypeId",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<GameSoftWager> gameSoftWagerList;

}
