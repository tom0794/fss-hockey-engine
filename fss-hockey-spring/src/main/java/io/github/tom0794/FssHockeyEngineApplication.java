package io.github.tom0794;

import io.github.tom0794.objects.Skater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.logging.Logger;


@SpringBootApplication
public class FssHockeyEngineApplication {

	public static void main(String[] args) {
		Skater s1 = new Skater();
		Logger logger = Logger.getLogger("io.github.tom0794");
		logger.info(s1.toString());
		SpringApplication.run(FssHockeyEngineApplication.class, args);
	}

}
