package com.security.spring.hotgames.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.security.spring.hotgames.entity.HotGameItem;
import com.security.spring.hotgames.service.HotGameService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games-list")
public class HotGameViewController {

    private final HotGameService hotGameService;

    @GetMapping
    public String getAllGames(@org.springframework.web.bind.annotation.RequestParam(name = "all", required = false, defaultValue = "false") boolean fetchAll, Model model) {
        List<HotGameItem> games;
        if (fetchAll) {
            games = hotGameService.fetchAllAvailableGames();
            model.addAttribute("isLive", true);
        } else {
            games = hotGameService.getHotGameItems();
            // Optional: apply the same provider-level sorting here if requested for consistency
            model.addAttribute("isLive", false);
        }
        model.addAttribute("games", games);
        return "hot-game-list";
    }
}
