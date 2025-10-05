package com.hakver29.game_of_life.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameController {
    private int GRID_SIZE = 10;
    private boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];

    private int countLiveNeighbors(int r, int c) {
        int count = 0;
    
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
            
                if (i == 0 && j == 0) continue; 

                int neighborRow = r + i;
                int neighborCol = c + j;

                if (neighborRow >= 0 && neighborRow < GRID_SIZE && 
                    neighborCol >= 0 && neighborCol < GRID_SIZE) {
                
                    if (grid[neighborRow][neighborCol]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static boolean determineNextState(boolean isCurrentlyAlive, int liveNeighbors) { 
        if (isCurrentlyAlive) {
            if (liveNeighbors < 2) {
                return false;
            } else if (liveNeighbors == 2 || liveNeighbors == 3) {
                return true;
            } else {
                return false;
            }
        } else {
            if (liveNeighbors == 3) {
                return true;
            } else {
                return false;
            }
        }
    }

    @GetMapping("/")
    public String showGrid(Model model) {
        model.addAttribute("grid", grid);
        return "index";
    }

    @PostMapping("/click/{row}/{col}")
    public String toggleCell(@PathVariable int row, @PathVariable int col, Model model) {
        int r = row - 1;
        int c = col - 1;

        // Toggle the state
        if (r >= 0 && r < GRID_SIZE && c >= 0 && c < GRID_SIZE) {
            grid[r][c] = !grid[r][c];
        }

        model.addAttribute("row", row);
        model.addAttribute("col", col);
        model.addAttribute("isActive", grid[r][c]);

        return "fragments/cell";
    }
    
    @PostMapping("/reset")
    public String resetBoard(Model model) {
        this.grid = new boolean[GRID_SIZE][GRID_SIZE];
        model.addAttribute("grid", this.grid);

        return "index";
    }

    @PostMapping("/start")
    public String invertBoard(Model model) {

    boolean[][] nextGrid = new boolean[GRID_SIZE][GRID_SIZE];

    
    for (int r = 0; r < GRID_SIZE; r++) {
        for (int c = 0; c < GRID_SIZE; c++) {
            int liveNeighbors = countLiveNeighbors(r, c);
            boolean isCurrentlyAlive = grid[r][c];

            nextGrid[r][c] = determineNextState(isCurrentlyAlive, liveNeighbors);
        }
    }

    model.addAttribute("grid", nextGrid);
    this.grid = nextGrid;

    return "index";
    }
}
