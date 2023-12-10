package io.github.tom0794;

import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDate;

public abstract class Player {
    private int id;
    private int teamId;
    private int number;
    private int nationalityId;
    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;
    private int height;

    protected enum positions {
        CENTER, RIGHT_WINGER, LEFT_WINGER, DEFENSEMAN, GOALTENDER
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(int nationalityId) {
        this.nationalityId = nationalityId;
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

    private int weight;
    public Player(
            int number,
            int nationalityId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            int height,
            int weight
    ) {
        setNumber(number);
        setNationalityId(nationalityId);
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setHeight(height);
        setWeight(weight);
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
