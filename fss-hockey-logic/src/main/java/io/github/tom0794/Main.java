package io.github.tom0794;

import io.github.tom0794.objects.Skater;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        LocalDate dob = LocalDate.of(1994, 7, 19);
        Skater s1 = new Skater(
                1, 1,1, "John", "Johnson", 180, 180, 19, dob
        );
        System.out.println("Created player: " + s1 + " age " + s1.getAge());
    }
}