package io.github.tom0794.objects;

import io.github.tom0794.schedule.Day;

import java.util.List;

public class Season {
    private Integer seasonId;
    private String year;

    private List<Day> days;

    public Season(String year) {
        this.year = year;
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
