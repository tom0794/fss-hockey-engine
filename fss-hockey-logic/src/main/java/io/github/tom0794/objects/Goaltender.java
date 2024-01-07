package io.github.tom0794.objects;

import java.time.LocalDate;

public class Goaltender extends Player {
    public Goaltender(int number, int nationalityId, String firstName, String lastName, LocalDate dateOfBirth, int height, int weight) {
        super(number, nationalityId, firstName, lastName, dateOfBirth, height, weight);
    }
}
