package io.github.tom0794.objects;

import java.time.LocalDate;

public class Goaltender extends Player {
    private static final int POSITION_ID = 6;

    private int reflexes;
    private int rebounds;
    private int agility;
    private int puckHandling;

    public Goaltender(
            int teamId,
            int countryId,
            String firstName,
            String lastName,
            int height,
            int weight,
            int number,
            LocalDate dateOfBirth,
            int reflexes,
            int rebounds,
            int agility,
            int puckHandling) {
        super(teamId, POSITION_ID, countryId, firstName, lastName, height, weight, number, dateOfBirth);
        setReflexes(reflexes);
        setRebounds(rebounds);
        setAgility(agility);
        setPuckHandling(puckHandling);
    }

    public int getReflexes() {
        return reflexes;
    }

    public void setReflexes(int reflexes) {
        this.reflexes = reflexes;
    }

    public int getRebounds() {
        return rebounds;
    }

    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getPuckHandling() {
        return puckHandling;
    }

    public void setPuckHandling(int puckHandling) {
        this.puckHandling = puckHandling;
    }
}
