package io.github.tom0794.objects;

public class Game {
    private Integer gameId;
    private Team homeTeam;
    private Team roadTeam;

    public Game(Team homeTeam, Team roadTeam) {
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
    }
}
