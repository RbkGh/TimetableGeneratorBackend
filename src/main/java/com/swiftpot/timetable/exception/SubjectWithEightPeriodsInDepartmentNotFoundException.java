/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.exception;

/**
 * if no subject in the specified department has 8 periods,throw this exception
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Apr-17 @ 9:41 AM
 */
public class SubjectWithEightPeriodsInDepartmentNotFoundException extends Exception {
    public SubjectWithEightPeriodsInDepartmentNotFoundException(String s) {
        super(s);
    }
}
