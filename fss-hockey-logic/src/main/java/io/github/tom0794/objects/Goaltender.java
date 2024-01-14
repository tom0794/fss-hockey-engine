package io.github.tom0794.objects;

import java.time.LocalDate;

public class Goaltender extends Player {
    public Goaltender(int teamId, int positionPrimaryId, int nationalityId, String firstName, String lastName, int height, int weight, int number, LocalDate dateOfBirth) {
        super(teamId, positionPrimaryId, nationalityId, firstName, lastName, height, weight, number, dateOfBirth);
    }
}
