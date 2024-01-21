package io.github.tom0794.objects;

import io.github.tom0794.database.DbOperations;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static io.github.tom0794.ObjectMapperUtils.getObjectMapper;

public class Skater extends Player {
    @Nullable
    private Integer positionSecondaryId = null;
    @Nullable
    private Integer positionTertiaryId = null;
    private int skating;
    private int shooting;
    private int passing;
    private int physicality;
    private int faceoffs;
    private int defense;
    private int puckHandling;
    private boolean isForward;

    public Skater(
            int teamId,
            int positionPrimaryId,
            int countryId,
            String firstName,
            String lastName,
            int height,
            int weight,
            int number,
            LocalDate dateOfBirth,
            int positionSecondaryId,
            int positionTertiaryId,
            int skating,
            int shooting,
            int passing,
            int physicality,
            int faceoffs,
            int defense,
            int puckHandling,
            boolean isForward) {
        super(teamId, positionPrimaryId, countryId, firstName, lastName, height, weight, number, dateOfBirth);
        setPositionSecondaryId(positionSecondaryId);
        setPositionTertiaryId(positionTertiaryId);
        setSkating(skating);
        setShooting(shooting);
        setPassing(passing);
        setPhysicality(physicality);
        setFaceoffs(faceoffs);
        setDefense(defense);
        setPuckHandling(puckHandling);
        setIsForward(isForward);
    }

    public Skater() {
        super();
        setSkating(50);
        setShooting(50);
        setPassing(50);
        setPhysicality(50);
        setFaceoffs(50);
        setDefense(50);
        setPuckHandling(50);
        setIsForward(true);
    }

    // CRUD
    public void createSkater() {
        HashMap<String, Object> mapObj = getObjectMapper().convertValue(this, HashMap.class);
        mapObj.put("dateOfBirth", this.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        DbOperations.createSkater(mapObj);
    }

    public Integer getPositionSecondaryId() {
        return positionSecondaryId;
    }

    public void setPositionSecondaryId(Integer positionSecondaryId) {
        this.positionSecondaryId = positionSecondaryId;
    }

    public Integer getPositionTertiaryId() {
        return positionTertiaryId;
    }

    public void setPositionTertiaryId(Integer positionTertiaryId) {
        this.positionTertiaryId = positionTertiaryId;
    }

    public int getSkating() {
        return skating;
    }

    public void setSkating(int skating) {
        this.skating = skating;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getPhysicality() {
        return physicality;
    }

    public void setPhysicality(int physicality) {
        this.physicality = physicality;
    }

    public int getFaceoffs() {
        return faceoffs;
    }

    public void setFaceoffs(int faceoffs) {
        this.faceoffs = faceoffs;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getPuckHandling() {
        return puckHandling;
    }

    public void setPuckHandling(int puckHandling) {
        this.puckHandling = puckHandling;
    }

    public boolean isForward() {
        return isForward;
    }

    public void setIsForward(boolean forward) {
        this.isForward = forward;
    }

}
