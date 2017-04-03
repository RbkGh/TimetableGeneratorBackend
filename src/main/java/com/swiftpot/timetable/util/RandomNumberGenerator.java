/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Generate Random numbers
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 2:08 PM
 */
@Component
public interface RandomNumberGenerator {

    /**
     * generate a random number within the range of the minimum and maximum value provided
     *
     * @param minimumValue the minimum value that randomly generated number should fall in
     * @param maxValue     the maximum value that randomy generated number should fall in,can not be more than 3 for this method.
     * @return {@link Integer}
     */
    default int generateRandomNumber(int minimumValue, int maxValue) throws UnsupportedOperationException {
        if (maxValue > 3) {
            throw new UnsupportedOperationException("Only a max of 3 is allowed for the max value for this project");
        } else if (minimumValue < 0) {
            throw new UnsupportedOperationException("minimum value must be >= 0");
        }
        Random random = new Random();
        return random.nextInt(maxValue - minimumValue + 1) + minimumValue;
    }

    /**
     * generate a random number within the range of the minimum and maximum value provided without any restriction of the max value like in {@link #generateRandomNumber(int, int)}
     *
     * @param minimumValue the minimum value that randomly generated number should fall in
     * @param maxValue     the maximum value that randomy generated number should fall in
     * @return {@link Integer}
     */
    default int generateRandomNumberWithoutAnyMaximumValueRestriction(int minimumValue, int maxValue) throws UnsupportedOperationException {
        if (minimumValue < 0) {
            throw new UnsupportedOperationException("minimum value must be at least greater than  or =0");
        }
        Random random = new Random();
        return random.nextInt(maxValue - minimumValue + 1) + minimumValue;
    }
}
