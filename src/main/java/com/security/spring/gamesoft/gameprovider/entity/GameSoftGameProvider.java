package com.security.spring.gamesoft.gameprovider.entity;

import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.transaction.entity.GameSoftTransaction;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="game_provider")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSoftGameProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long product;
    @Column(name="product_code")
    private String productCode;
    @Column(name="currency_code")
    private String currencyCode;
    @Column(name="conversion_rate")
    private double conversionRate;
    @Column(name = "image_name")
    private String imageName;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name ="game_type_id")
    @ToString.Exclude
    private GameType gameType;

    @OneToMany(mappedBy = "productID")
    @ToString.Exclude
    private List<GameSoftTransaction> gameSoftTransactionList;

    @OneToMany(mappedBy = "provider")
    @ToString.Exclude
    private List<GameSoftWager> gameSoftWagerList;
}
