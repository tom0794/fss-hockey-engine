package io.github.tom0794.objects;

import java.time.LocalDate;

public class Skater extends Player {

    public Skater(
            int number,
            int nationalityId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            int height,
            int weight
    ) {

        super(number, nationalityId, firstName, lastName, dateOfBirth, height, weight);
    }
}
