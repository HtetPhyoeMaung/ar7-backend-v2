package com.security.spring.hotgames.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "hot_game_items")
public class HotGameItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category", nullable = false)
    private String category; // e.g., "hotBuffalo", "hotSlot"

    @Column(name = "game_code", nullable = false)
    private String gameCode;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "game_type")
    private String gameType;

    @Column(name = "product_code")
    private Integer productCode;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "support_currency")
    private String supportCurrency;

    @Column(name = "status")
    private String status;

    @Column(name = "platform")
    private String platform;

    @Column(name = "game_url")
    private String gameUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "item_type")
    private String itemType; // "GAME" or "PROVIDER"

    @Column(name = "game_type_id")
    private Integer gameTypeId;

    @Column(name = "game_type_name")
    private String gameTypeName;

    @Column(name = "conversion_rate")
    private Double conversionRate;
}
