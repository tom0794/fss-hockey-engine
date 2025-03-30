package io.github.tom0794.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.tom0794.schedule.ScheduleUtils.incrementIndices;

class ScheduleUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger("fss-hockey-logic");

    @Test
    void incrementIndices_IncrementFinalIndex_Test() {
        int[] input = new int[]{0, 1, 2, 3, 4};
        int[] expected = new int[]{0, 1, 2, 3, 5};
        logger.info("Verify {} increments to {}", input, expected);
        int[] actual = incrementIndices(input, 10);
        Assertions.assertArrayEquals(expected, actual, "Actual array does not match expected");
    }

    @Test
    void incrementIndices_IncrementPenultimateIndex_Test() {
        int[] input = new int[]{0, 1, 2, 3, 9};
        int[] expected = new int[]{0, 1, 2, 4, 5};
        logger.info("Verify {} increments to {}", input, expected);
        int[] actual = incrementIndices(input, 10);
        Assertions.assertArrayEquals(expected, actual, "Actual array does not match expected");
    }

    @Test
    void incrementIndices_IncrementSecondIndex_Test() {
        int[] input = new int[]{0, 1, 7, 8, 9};
        int[] expected = new int[]{0, 2, 3, 4, 5};
        logger.info("Verify {} increments to {}", input, expected);
        int[] actual = incrementIndices(input, 10);
        Assertions.assertArrayEquals(expected, actual, "Actual array does not match expected");
    }

    @Test
    void incrementIndices_IncrementFirstIndex_Test() {
        int[] input = new int[]{0, 6, 7, 8, 9};
        int[] expected = new int[]{1, 2, 3, 4, 5};
        logger.info("Verify {} increments to {}", input, expected);
        int[] actual = incrementIndices(input, 10);
        Assertions.assertArrayEquals(expected, actual, "Actual array does not match expected");
    }

    @Test
    void incrementIndices_NoIncrementIndex_Test() {
        int[] input = new int[]{5, 6, 7, 8, 9};
        logger.info("Verify {} increments to null", input);
        int[] actual = incrementIndices(input, 10);
        Assertions.assertArrayEquals(null, actual, "Actual array does not match expected");
    }

    @Test
    void incrementIndices_LargeCollectionIncrementIndex_Test() {
        int[] input = new int[]{0, 1, 508, 900, 1312};
        int[] expected = new int[]{0, 1, 508, 901, 902};
        logger.info("Verify {} increments to {}", input, expected);
        int[] actual = incrementIndices(input, 1312);
        Assertions.assertArrayEquals(expected, actual, "Actual array does not match expected");
    }
}