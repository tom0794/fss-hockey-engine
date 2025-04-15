package io.github.tom0794.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Game {
    private Integer gameId;
    private Integer dayId;
    private Integer homeTeamId;
    private Integer roadTeamId;
    @JsonIgnore
    private Team homeTeam;
    @JsonIgnore
    private Team roadTeam;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }
    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setRoadTeamId(Integer roadTeamId) {
        this.roadTeamId = roadTeamId;
    }
    public Integer getRoadTeamId() {
        return roadTeamId;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(Team roadTeam) {
        this.roadTeam = roadTeam;
    }

    public Game(Team homeTeam, Team roadTeam) {
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
    }

    public Game(Team homeTeam, Integer homeTeamId, Team roadTeam, Integer roadTeamId) {
        this.homeTeam = homeTeam;
        this.homeTeamId = homeTeamId;
        this.roadTeam = roadTeam;
        this.roadTeamId = roadTeamId;
    }

    public void createGame() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        this.setGameId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Game retrieveGame(Integer gameId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("game", "gameId", String.valueOf(gameId));
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Game.class);
    }

    public void updateGame() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        DbOperations.update("game", "gameId", mapObj);
    }

    public void deleteGame() {
        deleteGame(getGameId());
    }

    public static void deleteGame(Integer gameId) {
        DbOperations.delete("game", "gameId", gameId);
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getDayId() {
        return  this.dayId;
    }

    @Override
    public String toString() {
        return getRoadTeam() + " @ " + getHomeTeam();
    }
}
