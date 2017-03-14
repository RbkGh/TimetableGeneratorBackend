/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.swiftpot.timetable.util.RandomNumberGenerator;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 2:21 PM
 */
public class RandomNumberGeneratorTest {

    @Test
    public void generateRandomNumberTest() {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator() {

        };

        int min = 1;
        int max = 3;
        for (int i = 0; i < 100; i++) {
            int genNumber = randomNumberGenerator.generateRandomNumber(min, max);
            System.out.println("Generated number = " + genNumber + "\n");
            assertTrue(genNumber >= min && genNumber <= max);
        }
    }
}
