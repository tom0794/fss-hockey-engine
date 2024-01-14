package io.github.tom0794;

import io.github.tom0794.objects.Skater;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Skater s1 = new Skater();
        System.out.println("Created player: " + s1 + " age " + s1.getAge());
    }
}