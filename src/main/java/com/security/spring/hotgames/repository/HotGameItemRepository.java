package com.security.spring.hotgames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.spring.hotgames.entity.HotGameItem;

public interface HotGameItemRepository extends JpaRepository<HotGameItem, Integer> {
    List<HotGameItem> findAllByOrderBySortOrderAsc();
    List<HotGameItem> findByCategoryOrderBySortOrderAsc(String category);
    boolean existsByCategoryAndGameCode(String category, String gameCode);
    java.util.Optional<HotGameItem> findByCategoryAndGameCode(String category, String gameCode);
}
