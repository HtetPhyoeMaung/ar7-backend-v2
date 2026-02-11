package com.security.spring.hotgames.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.security.spring.hotgames.dto.AddHotGameRequest;
import com.security.spring.hotgames.entity.HotGameItem;
import com.security.spring.hotgames.service.HotGameService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games-list")
public class HotGameViewController {

    private final HotGameService hotGameService;

    @GetMapping
    public String getAllGames(@RequestParam(name = "all", required = false, defaultValue = "false") boolean fetchAll, Model model) {
        List<HotGameItem> games;
        if (fetchAll) {
            games = hotGameService.fetchAllAvailableGames();
            model.addAttribute("isLive", true);
        } else {
            games = hotGameService.getHotGameItems();
            model.addAttribute("isLive", false);
        }
        model.addAttribute("games", games);
        return "hot-game-list";
    }

    @PostMapping("/add")
    public String addHotGame(@ModelAttribute AddHotGameRequest request, RedirectAttributes redirectAttributes) {
        try {
            hotGameService.addHotGame(request);
            redirectAttributes.addFlashAttribute("success", "Game added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding game: " + e.getMessage());
        }
        return "redirect:/games-list";
    }

    @PostMapping("/update/{id}")
    public String updateHotGame(@PathVariable("id") Integer id, @ModelAttribute AddHotGameRequest request, RedirectAttributes redirectAttributes) {
        try {
            hotGameService.updateHotGame(id, request);
            redirectAttributes.addFlashAttribute("success", "Game updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating game: " + e.getMessage());
        }
        return "redirect:/games-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteHotGame(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            hotGameService.removeHotGame(id);
            redirectAttributes.addFlashAttribute("success", "Game removed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error removing game: " + e.getMessage());
        }
        return "redirect:/games-list";
    }

    @GetMapping("/refresh")
    public String refreshHotGames(RedirectAttributes redirectAttributes) {
        try {
            hotGameService.refreshHotGames();
            redirectAttributes.addFlashAttribute("success", "Games refreshed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error refreshing games: " + e.getMessage());
        }
        return "redirect:/games-list";
    }
}
