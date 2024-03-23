package io.github.tom0794.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.io.IOException;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Team {
    private Integer teamId;
    private int divisionId;
    private String city;
    private String name;
    private String abbreviation;
    private String primaryColour;
    private String secondaryColour;
    private String tertiaryColour = null;

    public Team(int divisionId, String city, String name, String abbreviation, String primaryColour, String secondaryColour, String tertiaryColour) {
        this.divisionId = divisionId;
        this.city = city;
        this.name = name;
        this.abbreviation = abbreviation;
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
        this.tertiaryColour = tertiaryColour;
    }

    public Team(int divisionId, String city, String name, String abbreviation, String primaryColour, String secondaryColour) {
        this.divisionId = divisionId;
        this.city = city;
        this.name = name;
        this.abbreviation = abbreviation;
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
    }

    // CRUD
    public void createTeam() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        this.setTeamId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Team retrieveTeam(int teamId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("team", "teamId", teamId);
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Team.class);
    }

    public void updateTeam() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        DbOperations.update("team", "teamId", mapObj);
    }

    public void deleteTeam() {
        deleteTeam(getTeamId());
    }

    public static void deleteTeam(int teamId) {
        DbOperations.delete("team", "teamId", teamId);
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
    }

    public String getSecondaryColour() {
        return secondaryColour;
    }

    public void setSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public String getTertiaryColour() {
        return tertiaryColour;
    }

    public void setTertiaryColour(String tertiaryColour) {
        this.tertiaryColour = tertiaryColour;
    }

    @Override
    public String toString() {
        return getAbbreviation();
    }
}
