package io.github.tom0794.fsshockeyengine;

import io.github.tom0794.Skater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class FssHockeyEngineApplication {

	public static void main(String[] args) {
		LocalDate dob = LocalDate.of(1994, 7, 19);
		Skater s1 = new Skater(
				19, 1, "John", "Johnson", dob, 180, 180
		);
		SpringApplication.run(FssHockeyEngineApplication.class, args);
	}

}
