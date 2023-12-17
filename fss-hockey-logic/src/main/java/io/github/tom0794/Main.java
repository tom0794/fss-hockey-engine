package io.github.tom0794;

import io.github.tom0794.objects.Skater;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        LocalDate dob = LocalDate.of(1994, 7, 19);
        Skater p1 = new Skater(
                19, 1, "John", "Johnson", dob, 180, 180
        );
        System.out.println("Created player: " + p1 + " age " + p1.getAge());
    }
}