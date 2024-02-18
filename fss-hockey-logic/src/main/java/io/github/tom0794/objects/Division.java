package io.github.tom0794.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.io.IOException;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Division {
    private Integer divisionId;
    private int conferenceId;
    private String name;
    private String abbreviation;

    public Division(int divisionId, int conferenceId, String name, String abbreviation) {
        this.divisionId = divisionId;
        this.conferenceId = conferenceId;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    // CRUD
    public void createDivision() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        this.setDivisionId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Division retrieveDivision(int divisionId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("division", "divisionId", divisionId);
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Division.class);
    }

    public void updateDivision() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        DbOperations.update("division", "divisionId", mapObj);
    }

    public void deleteDivision() {
        deleteDivision(getDivisionId());
    }

    public static void deleteDivision(int divisionId) {
        DbOperations.delete("division", "divisionId", divisionId);
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
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
}
