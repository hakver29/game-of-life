package com.hakver29.game_of_life.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameController {

    // Simple in-memory grid state for demonstration (10x10)
    private boolean[][] grid = new boolean[10][10];

    /**
     * Renders the initial 10x10 grid page.
     */
    @GetMapping("/")
    public String showGrid(Model model) {
        model.addAttribute("grid", grid);
        // Returns the 'index.html' template
        return "index";
    }

    /**
     * Handles the HTMX POST request when a cell is clicked.
     * It toggles the cell's state (dead/alive) and returns *only* the new cell HTML.
     */
    @PostMapping("/click/{row}/{col}")
    public String toggleCell(@PathVariable int row, @PathVariable int col, Model model) {
        // Adjust indices for 0-based array (assuming 1-10 from frontend)
        int r = row - 1;
        int c = col - 1;

        // Toggle the state
        if (r >= 0 && r < 10 && c >= 0 && c < 10) {
            grid[r][c] = !grid[r][c];
        }

        // Add the updated cell state to the model for Thymeleaf to render
        model.addAttribute("row", row);
        model.addAttribute("col", col);
        model.addAttribute("isActive", grid[r][c]);

        return "fragments/cell";
    }
    
    @PostMapping("/reset")
    public String resetBoard(Model model) {
        this.grid = new boolean[10][10];
        model.addAttribute("grid", this.grid);

        return "index";
    }

    @PostMapping("/start")
    public String invertBoard(Model model) {
    
    // Invert the state of every cell in the current grid
    for (int r = 0; r < 10; r++) {
        for (int c = 0; c < 10; c++) {
            grid[r][c] = !grid[r][c];
        }
    }

    model.addAttribute("grid", this.grid);

    return "index";
    }
}
