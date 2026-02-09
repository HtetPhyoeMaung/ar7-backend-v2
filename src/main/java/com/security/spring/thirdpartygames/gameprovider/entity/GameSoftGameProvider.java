package com.security.spring.thirdpartygames.gameprovider.entity;

import com.security.spring.thirdpartygames.callback.dto.Currency;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.getProviderList.dto.ProviderResponse;
import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "game_provider")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSoftGameProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long product;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "currency_code")
    private String currencyCode;
    @Column(name = "conversion_rate")
    private double conversionRate;
    @Column(name = "image_name")
    private String imageName;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "game_type_id")
    @ToString.Exclude
    private GameType gameType;

    @Column(name = "sort_number")
    private Integer sortNumber;

    private boolean deleted;

    @OneToMany(mappedBy = "productID", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @ToString.Exclude
    private List<GameSoftTransaction> gameSoftTransactionList;

    @OneToMany(mappedBy = "provider", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @ToString.Exclude
    private List<GameSoftWager> gameSoftWagerList;

    public static GameSoftGameProvider of(ProviderResponse.ProviderData newProvider, GameType gameType) {
        var gameProvider = new GameSoftGameProvider();
        try {
            Currency c = Currency.valueOf(newProvider.getCurrency());
            gameProvider.setConversionRate(c.getRate().doubleValue());
        } catch (IllegalArgumentException e) {
            gameProvider.setConversionRate(1); // default rate
        }
        gameProvider.setGameType(gameType);
        gameProvider.setProduct(newProvider.getProductCode());
        gameProvider.setCurrencyCode(newProvider.getCurrency());
        gameProvider.setProductCode(newProvider.getProductName());
        return gameProvider;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
