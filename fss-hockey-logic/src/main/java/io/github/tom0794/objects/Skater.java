package io.github.tom0794.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Skater extends Player {
    private int positionSecondaryId;
    private int positionTertiaryId;
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
            int nationalityId,
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
        super(teamId, positionPrimaryId, nationalityId, firstName, lastName, height, weight, number, dateOfBirth);
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
//        HashMap<String, Object> skaterValues = new HashMap<>();
//        skaterValues.put("team_id", this.getTeamId());
//        skaterValues.put("position_primary_id", this.getPositionPrimaryId());
//        skaterValues.put("country_id", this.getNationalityId());
//        skaterValues.put("first_name", this.getFirstName());
//        skaterValues.put("last_name", this.getLastName());
//        skaterValues.put("height", this.getHeight());
//        skaterValues.put("weight", this.getWeight());
//        skaterValues.put("number", this.getNumber());
//        skaterValues.put("dob", this.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        skaterValues.put("skating", this.getSkating());
//        skaterValues.put("shooting", this.getShooting());
//        skaterValues.put("passing", this.getPassing());
//        skaterValues.put("physicality", this.getPhysicality());
//        skaterValues.put("faceoffs", this.getFaceoffs());
//        skaterValues.put("defense", this.getDefense());
//        skaterValues.put("puck_handling", this.getPuckHandling());
//        skaterValues.put("is_forward", this.isForward());
        ObjectMapper mapObject = new ObjectMapper();
        mapObject.findAndRegisterModules();
        HashMap<String, Object> mapObj = mapObject.convertValue(this, HashMap.class);
        mapObj.put("dateOfBirth", this.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mapObj.remove("age");
        DbOperations.createSkater(mapObj);
    }

    public int getPositionSecondaryId() {
        return positionSecondaryId;
    }

    public void setPositionSecondaryId(int positionSecondaryId) {
        this.positionSecondaryId = positionSecondaryId;
    }

    public int getPositionTertiaryId() {
        return positionTertiaryId;
    }

    public void setPositionTertiaryId(int positionTertiaryId) {
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
