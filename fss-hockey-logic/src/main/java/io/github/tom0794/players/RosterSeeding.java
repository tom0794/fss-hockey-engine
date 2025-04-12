package io.github.tom0794.players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RosterSeeding {
    private static final Logger logger = LoggerFactory.getLogger("roster-seeding");
    private static final Faker faker = new Faker(new Locale("en-CA"));
    private static final int FORWARD_LINES = 4;
    private static final int DEFENSE_PAIRS = 3;
    private static final int GOALTENDERS = 2;
    public static void seedRosters() throws JsonProcessingException {
        logger.info("Name: {} {}", faker.name().firstName(), faker.name().lastName());

        List<Team> teams = Team.retrieveAllTeams();
        List<Skater> centers = createCenters(FORWARD_LINES * teams.size());
        System.out.println("stop");
//        Skater s = new Skater(1, 1, f.name().firstName(), f.name().lastName(), height, weight, number, dob, secondpos, thirdpos, skating, shooting, passing, phys, faceoff, def, puchandle, forward)
//        Skater s = new Skater(
//            1,
//            1,
//            f.name().firstName(),
//            f.name().lastName(),
//            height,
//            weight,
//            number,
//            dob,
//            secondpos,
//            thirdpos,
//            skating, shooting, passing, phys, faceoff, def, puchandle,
//            forward
//        );
    }

    // method to create pool of (centers, left wingers, right wingers, defensemen, goalies)
    public static List<Skater> createCenters(int quantity) {
        List<Skater> centers = new ArrayList<Skater>();
        for (int i = 0; i < quantity; i++) {
            Skater skater = new Skater(
                    null, // Team Id: starts as null, updated when drafted
                    -1, // primary position id
                    -1, // country id
                    faker.name().firstName(),
                    faker.name().lastName(),
                    -1, // height
                    -1, // weight
                    -1, // number
                    LocalDate.of(1994, 7, 19), // dob
                    null, // second position
                    null, // third position
                    -1, // skating
                    -1, // shooting
                    -1, // passing
                    -1, // physicality
                    -1, // faceoff
                    -1, // defense
                    -1, // puckhandling
                    true // is forward
            );
            centers.add(skater);
        }
        return centers;
    }

    abstract static class PlayerArchetype {
        String name;
        int skatingMax;
        int skatingMin;
        int shootingMax;
        int shootingMin;
        int passingMax;
        int passingMin;
        int physicalityMax;
        int physicalityMin;
        int defenseMax;
        int defenseMin;
        int puckHandlingMax;
        int puckHandlingMin;
        int faceoffMax;
        int faceoffMin;
    }

    static class Playmaker extends PlayerArchetype {
        public Playmaker() {
            skatingMin = 75;
            skatingMax = 85;
            shootingMin = 60;
            shootingMax = 65;
            passingMin = 80;
            passingMax = 90;
            physicalityMin = 30;
            physicalityMax = 40;
            faceoffMin = 75;
            faceoffMax = 85;
            defenseMin = 55;
            defenseMax = 65;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class Sniper extends PlayerArchetype {
        public Sniper() {
            skatingMin = 70;
            skatingMax = 80;
            shootingMin = 80;
            shootingMax = 85;
            passingMin = 55;
            passingMax = 65;
            physicalityMin = 45;
            physicalityMax = 55;
            faceoffMin = 65;
            faceoffMax = 75;
            defenseMin = 50;
            defenseMax = 60;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class TwoWayForward extends PlayerArchetype {
        public TwoWayForward() {
            skatingMin = 75;
            skatingMax = 85;
            shootingMin = 50;
            shootingMax = 60;
            passingMin = 50;
            passingMax = 60;
            physicalityMin = 55;
            physicalityMax = 65;
            faceoffMin = 80;
            faceoffMax = 85;
            defenseMin = 80;
            defenseMax = 85;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class PowerForward extends PlayerArchetype {
        public PowerForward() {
            skatingMin = 60;
            skatingMax = 65;
            shootingMin = 65;
            shootingMax = 75;
            passingMin = 50;
            passingMax = 55;
            physicalityMin = 65;
            physicalityMax = 75;
            faceoffMin = 50;
            faceoffMax = 60;
            defenseMin = 65;
            defenseMax = 75;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class Grinder extends PlayerArchetype {
        public Grinder() {
            skatingMin = 50;
            skatingMax = 60;
            shootingMin = 30;
            shootingMax = 40;
            passingMin = 35;
            passingMax = 45;
            physicalityMin = 80;
            physicalityMax = 85;
            faceoffMin = 75;
            faceoffMax = 85;
            defenseMin = 65;
            defenseMax = 75;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class StayAtHomeD extends PlayerArchetype {
        public StayAtHomeD() {
            skatingMin = 60;
            skatingMax = 70;
            shootingMin = 20;
            shootingMax = 30;
            passingMin = 65;
            passingMax = 75;
            physicalityMin = 80;
            physicalityMax = 85;
            faceoffMin = 0;
            faceoffMax = 0;
            defenseMin = 85;
            defenseMax = 95;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class OffensiveD extends PlayerArchetype {
        public OffensiveD() {
            skatingMin = 70;
            skatingMax = 80;
            shootingMin = 40;
            shootingMax = 50;
            passingMin = 75;
            passingMax = 85;
            physicalityMin = 45;
            physicalityMax = 55;
            faceoffMin = 0;
            faceoffMax = 0;
            defenseMin = 70;
            defenseMax = 75;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }

    static class TwoWayD extends PlayerArchetype {
        public TwoWayD() {
            skatingMin = 65;
            skatingMax = 75;
            shootingMin = 30;
            shootingMax = 40;
            passingMin = 70;
            passingMax = 80;
            physicalityMin = 60;
            physicalityMax = 70;
            faceoffMin = 0;
            faceoffMax = 0;
            defenseMin = 75;
            defenseMax = 80;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }
}
