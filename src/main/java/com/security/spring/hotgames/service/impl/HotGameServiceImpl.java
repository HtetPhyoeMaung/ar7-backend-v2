package com.security.spring.hotgames.service.impl;

import com.security.spring.hotgames.entity.HotGame;
import com.security.spring.hotgames.repository.HotGameRepository;
import com.security.spring.hotgames.service.HotGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotGameServiceImpl implements HotGameService {
    private final HotGameRepository hotGameRepository;
    @Override
    public String getHotGames() {
        HotGame hotGame = hotGameRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Hot game not found"));
        return hotGame.getGames();
    }
}
