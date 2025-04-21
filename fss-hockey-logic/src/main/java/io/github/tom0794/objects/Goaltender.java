package io.github.tom0794.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

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

    // CRUD
    public void createGoaltender() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        mapObj.put("dateOfBirth", this.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.setPlayerId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Skater retrieveGoaltender(Integer playerId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("goaltender", "playerId", String.valueOf(playerId));
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Skater.class);
    }

    public void updateGoaltender() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        mapObj.put("dateOfBirth", this.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        DbOperations.update("goaltender", "playerId", mapObj);
    }

    public void deleteGoaltender() {
        deleteGoaltender(getPlayerId());
    }

    public static void deleteGoaltender(Integer playerId) {
        DbOperations.delete("goaltender", "playerId", playerId);
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
