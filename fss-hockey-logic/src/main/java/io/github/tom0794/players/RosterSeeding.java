package io.github.tom0794.players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.github.tom0794.objects.Skater;
import io.github.tom0794.objects.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RosterSeeding {
    private static final Logger logger = LoggerFactory.getLogger("roster-seeding");
    private static final Faker faker = new Faker(new Locale("en-CA"));
    private static final int FORWARD_LINES = 4;
    private static final int DEFENSE_PAIRS = 3;
    private static final int GOALTENDERS = 2;
    public static void seedRosters() throws JsonProcessingException {
        logger.info("Name: {} {}", faker.name().firstName(), faker.name().lastName());

        List<Team> teams = Team.retrieveAllTeams();
        // get position IDs, call with center/left/right
        PlayerArchetype[] forwardArchetypes = {new Playmaker(), new Sniper(), new PowerForward(), new TwoWayForward(), new Grinder()};
        float[] lineMultipliers = {1.15f, 1.05f, 0.75f, 0.65f};
        List<Skater> centers = createSkaters(FORWARD_LINES * teams.size(), 1, forwardArchetypes, lineMultipliers, true);
        List<Skater> rightWingers = createSkaters(FORWARD_LINES * teams.size(), 2, forwardArchetypes, lineMultipliers, true);
        List<Skater> leftWingers = createSkaters(FORWARD_LINES * teams.size(), 3, forwardArchetypes, lineMultipliers, true);

        centers.sort(Comparator.comparingInt(Skater::getOverallStat).reversed());
        leftWingers.sort(Comparator.comparingInt(Skater::getOverallStat).reversed());
        rightWingers.sort(Comparator.comparingInt(Skater::getOverallStat).reversed());

        float[] pairingMultipliers = {1.15f, 1.05f, 0.75f};
        PlayerArchetype[] defenseArchetypes = {new StayAtHomeD(), new OffensiveD(), new TwoWayD()};
        List<Skater> leftDefense = createSkaters(DEFENSE_PAIRS * teams.size(), 4, defenseArchetypes, pairingMultipliers, false);
        List<Skater> rightDefense = createSkaters(DEFENSE_PAIRS * teams.size(), 5, defenseArchetypes, pairingMultipliers, false);

        leftDefense.sort(Comparator.comparingInt(Skater::getOverallStat).reversed());
        rightDefense.sort(Comparator.comparingInt(Skater::getOverallStat).reversed());

        // debug
        for (Skater center : centers) {
            logger.info("center: {}", center);
        }
        for (Skater rightWinger : rightWingers) {
            logger.info("rw {}", rightWinger);
        }
        for (Skater leftWinger : leftWingers) {
            logger.info("lw {}", leftWinger);
        }
        for (Skater ld : leftDefense) {
            logger.info("ld {}", ld);
        }
        for (Skater rd : rightDefense) {
            logger.info("rd {}", rd);
        }

    }

    // method to create pool of skaters
    private static List<Skater> createSkaters(
            int quantity,
            int position,
            PlayerArchetype[] archetypes,
            float[] lineMultipliers,
            boolean isForward
    ) {
        List<Skater> skaters = new ArrayList<Skater>();

        int typeQuantity = quantity / archetypes.length;
        int playerArchetypeIndex = 0;
        int lineMultiplierIndex = 0;

        for (int i = 0; i < quantity; i++) {
            Skater skater = new Skater(
                    null, // Team Id: starts as null, updated when drafted
                    position, // primary position id
                    1, // country id
                    faker.name().firstName(),
                    faker.name().lastName(),
                    180, // height
                    72, // weight
                    1, // number
                    LocalDate.of(1994, 7, 19), // dob
                    null, // second position
                    null, // third position
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].skatingMin,
                            archetypes[playerArchetypeIndex].skatingMax
                    )), // skating
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].shootingMin,
                            archetypes[playerArchetypeIndex].shootingMax
                    )), // shooting
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].passingMin,
                            archetypes[playerArchetypeIndex].passingMax
                    )), // passing
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].physicalityMin,
                            archetypes[playerArchetypeIndex].physicalityMax
                    )), // physicality
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].faceoffMin,
                            archetypes[playerArchetypeIndex].faceoffMax
                    )), // faceoff
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].defenseMin,
                            archetypes[playerArchetypeIndex].defenseMax
                    )), // defense
                    (int) (lineMultipliers[lineMultiplierIndex] * ThreadLocalRandom.current().nextInt(
                            archetypes[playerArchetypeIndex].puckHandlingMin,
                            archetypes[playerArchetypeIndex].puckHandlingMax
                    )), // puckhandling
                    isForward
            );
            skaters.add(skater);
            if (skaters.size() % typeQuantity == 0 && playerArchetypeIndex + 1 != archetypes.length) {
                playerArchetypeIndex++;
            }
            if (skaters.size() % (typeQuantity / archetypes.length) == 0 && lineMultiplierIndex + 1 != lineMultipliers.length) {
                lineMultiplierIndex++;
            } else if (skaters.size() % typeQuantity == 0) {
                lineMultiplierIndex = 0;
            }
        }
        return skaters;
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
            faceoffMin = -1;
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
            faceoffMin = -1;
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
            faceoffMin = -1;
            faceoffMax = 0;
            defenseMin = 75;
            defenseMax = 80;
            puckHandlingMin = 60;
            puckHandlingMax = 80;
        }
    }
}
