package io.github.tom0794.objects;

public class Game {
    private Integer gameId;
    private Team homeTeam;
    private Team roadTeam;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
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

    @Override
    public String toString() {
        return getRoadTeam() + " @ " + getHomeTeam();
    }
}
