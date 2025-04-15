package io.github.tom0794.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;
import io.github.tom0794.objects.Game;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Day {

    public Integer dayId;
    public Integer seasonId;
    private LocalDate date;
    @JsonIgnore
    private List<Game> games;

    public Day(LocalDate date) {
        this.date = date;
        this.games = new ArrayList<Game>();
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Day() {
        this.games = new ArrayList<Game>();
    }

    public Day(Day day) {
        this.date = day.getDate();
        this.games = day.getGames();
    }

    public void createDay() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        mapObj.put("date", this.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.setDayId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Game retrieveDay(Integer dayId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("day", "dayId", String.valueOf(dayId));
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Game.class);
    }

    public static Game retrieveDayWithColumn(String column, String value) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("day", column, value);
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Game.class);
    }

    public void updateDay() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        mapObj.put("date", this.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        DbOperations.update("day", "dayId", mapObj);
    }

    public void deleteDay() {
        deleteDay(getDayId());
    }

    public static void deleteDay(Integer dayId) {
        DbOperations.delete("day", "dayId", dayId);
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getSeasonId() {
        return seasonId;
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
