package io.github.tom0794.schedule;

import io.github.tom0794.objects.Game;

import java.time.LocalDate;
import java.util.*;

public class Day {

    public Integer dayId;
    private LocalDate date;
    private List<Game> games;

    public Day(LocalDate date) {
        this.date = date;
        this.games = new ArrayList<Game>();
    }

    public Integer getDayId() {
        return dayId;
    }

    public Day() {
        this.games = new ArrayList<Game>();
    }

    public Day(Day day) {
        this.date = day.getDate();
        this.games = day.getGames();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }
}
