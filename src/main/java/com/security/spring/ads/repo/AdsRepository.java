package com.security.spring.ads.repo;

import com.security.spring.ads.entity.Ads;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads,Long> {
    List<Ads> findByAdsType(Ads.AdsType type, Sort id);
}
