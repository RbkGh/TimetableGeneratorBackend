/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.exception;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Apr-17 @ 5:01 AM
 */
public class PracticalTutorNotFoundException extends Exception {

    public static final String DEFAULT_MESSAGE = "pRACTICAL TUTOR NOT FOUND FOR SUBJECT";

    public PracticalTutorNotFoundException(String s) {
        super(s);
    }
}
