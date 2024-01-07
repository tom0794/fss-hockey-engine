package io.github.tom0794.simulation;

import io.github.tom0794.objects.Goaltender;
import io.github.tom0794.objects.Skater;

import java.util.List;

public class GameState {
    // Players
    private static List<Line> homeLines;
    private static List<Pairing> homePairings;
    private static List<Goaltender> homeGoaltenders;
    private static Line homeActiveLine;
    private static Pairing homeActivePairing;
    private static Goaltender homeActiveGoaltender;

    private static List<Line> awayLines;
    private static List<Pairing> awayPairings;
    private static List<Goaltender> awayGoaltenders;
    private static Line awayActiveLine;
    private static Pairing awayActivePairing;
    private static Goaltender awayActiveGoaltender;

    // Puck location
    private static Skater puckCarrier;
    private enum zone {
        homeEnd,
        neutral,
        awayEnd
    }

    // Time
    private static int period;
    private static int secondsRemaining;
    private static int secondsElapsed;
}
