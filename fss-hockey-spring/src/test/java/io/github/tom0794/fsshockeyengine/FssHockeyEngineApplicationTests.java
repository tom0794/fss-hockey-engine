package io.github.tom0794.fsshockeyengine;

import io.github.tom0794.objects.Goaltender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class FssHockeyEngineApplicationTests {

	private final static LocalDate DOB = LocalDate.of(1994, 7, 19);

	@Test
	void contextLoads() {
	}

	@Test
	void testGoaltender() {
		Goaltender g = new Goaltender(
				1, 1, "Mike", "Tender", 180, 180, 35, DOB, 50, 50, 50, 50
		);
	}

}
