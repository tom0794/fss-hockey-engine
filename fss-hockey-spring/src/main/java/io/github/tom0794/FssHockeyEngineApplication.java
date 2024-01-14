package io.github.tom0794;

import io.github.tom0794.objects.Skater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.logging.Logger;


@SpringBootApplication
public class FssHockeyEngineApplication {

	public static void main(String[] args) {
		LocalDate dob = LocalDate.of(1994, 7, 19);
		Skater s1 = new Skater(
				1, 1,1, "John", "Johnson", 180, 180, 19, dob
		);
		Logger logger = Logger.getLogger("io.github.tom0794");
		logger.info(s1.toString());
		SpringApplication.run(FssHockeyEngineApplication.class, args);
	}

}
