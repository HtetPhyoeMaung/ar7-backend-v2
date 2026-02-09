package com.security.spring.ads.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(indexes = {
        @Index(name = "idx_ads_type", columnList = "adsType"),
        @Index(name = "idx_ads_view_type", columnList = "viewType")
})
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AdsType adsType;
    @Enumerated(EnumType.STRING)
    private ViewType viewType;
    private String imageName;
    private String text;

    public enum AdsType {
        Carousal,
        RunningText
    }

    public enum ViewType {
        Mobile,
        Web
    }
}
