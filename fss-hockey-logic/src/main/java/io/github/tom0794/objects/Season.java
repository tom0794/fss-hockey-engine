package io.github.tom0794.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;
import io.github.tom0794.schedule.Day;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Season {
    private Integer seasonId;
    private String year;

    @JsonIgnore
    private List<Day> days;

    public Season(String year) {
        this.year = year;
        this.days = new ArrayList<Day>();
    }

    public void createSeason() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        this.setSeasonId(DbOperations.insert(this.getClass().getSimpleName(), mapObj));
    }

    public static Game retrieveSeason(Integer seasonId) throws IOException {
        HashMap<String, Object> values = DbOperations.retrieve("season", "seasonId", seasonId);
        ObjectMapper mapObj = getObjectMapper();
        return mapObj.readValue(mapObj.writeValueAsString(values), Game.class);
    }

    public void updateSeason() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        DbOperations.update("season", "seasonId", mapObj);
    }

    public void deleteSeason() {
        deleteSeason(getSeasonId());
    }

    public static void deleteSeason(Integer seasonId) {
        DbOperations.delete("season", "seasonId", seasonId);
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public void addDay(Day day) {
        this.days.add(day);
    }
}
