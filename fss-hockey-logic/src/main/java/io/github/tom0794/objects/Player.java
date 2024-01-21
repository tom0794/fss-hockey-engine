package io.github.tom0794.objects;

import java.time.Duration;
import java.time.LocalDate;

public abstract class Player {
    private int playerId;
    private int teamId;
    private int positionPrimaryId;
    private int countryId;
    private String firstName;
    private String lastName;
    private int height;
    private int weight;
    private int number;
    private LocalDate dateOfBirth;

    public Player(
            int teamId,
            int positionPrimaryId,
            int nationalityId,
            String firstName,
            String lastName,
            int height,
            int weight,
            int number,
            LocalDate dateOfBirth) {
        setTeamId(teamId);
        setPositionPrimaryId(positionPrimaryId);
        setNumber(number);
        setCountryId(nationalityId);
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setHeight(height);
        setWeight(weight);
    }

    public Player() {
        setTeamId(0);
        setPositionPrimaryId(1);
        setCountryId(1);
        setFirstName("John");
        setLastName("Johnson");
        setHeight(180);
        setWeight(170);
        setNumber(10);
        setDateOfBirth(LocalDate.of(1994, 7, 19));
    }

    protected enum positions {
        CENTER, RIGHT_WINGER, LEFT_WINGER, DEFENSEMAN, GOALTENDER
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPositionPrimaryId() {
        return positionPrimaryId;
    }

    public void setPositionPrimaryId(int positionPrimaryId) {
        this.positionPrimaryId = positionPrimaryId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns the age of the player.
     */
    public long getAge() {
        LocalDate today = LocalDate.now();
        Duration diff = Duration.between(this.dateOfBirth.atStartOfDay(), today.atStartOfDay());
        return diff.toDays() / 365;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
