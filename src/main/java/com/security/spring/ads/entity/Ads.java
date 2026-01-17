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
        @Index(name = "idx_ads_type", columnList = "adsType")
})
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AdsType adsType;
    private String imageName;
    private String text;

    public enum AdsType{
        Carousal,
        RunningText

    }

}
