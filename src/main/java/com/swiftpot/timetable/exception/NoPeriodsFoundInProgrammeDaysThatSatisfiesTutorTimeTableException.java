/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.exception;

/**
 * When there were no periods that satisfies tutor's personal timetable in such a way that there will be no collision,
 * this exception is thrown.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Apr-17 @ 1:48 AM
 */
public class NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException extends Exception {

    public static final String DEFAULT_MESSAGE = "No periods found in all the programmeDays that satisfies the constraints on the tutor's personal timetable";

    public NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException() {
    }

    public NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException(String s) {
        super(s);
    }
}
