package io.github.tom0794.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.io.IOException;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Conference {
    private Integer conferenceId;
    private String name;
    private String abbreviation;

    public Conference(int conferenceId, String name, String abbreviation) {
        this.conferenceId = conferenceId;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    // CRUD
    public void createConference() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        this.setConferenceId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Conference retrieveConference(int conferenceId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("conference", "conferenceId", conferenceId);
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Conference.class);
    }

    public void updateConference() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        DbOperations.update("conference", "conference", mapObj);
    }

    public void deleteConference() {
        deleteConference(getConferenceId());
    }

    public static void deleteConference(int divisionId) {
        DbOperations.delete("conference", "conferenceId", divisionId);
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
