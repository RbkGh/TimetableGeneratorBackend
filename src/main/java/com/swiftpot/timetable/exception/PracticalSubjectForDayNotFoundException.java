/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.exception;

import com.swiftpot.timetable.model.ProgrammeDay;

/**
 * When a practical subject is expected in a day and it is not found,this exception will be thrown.
 * One Use Case is in {@link com.swiftpot.timetable.services.ProgrammeDayPeriodSetService#getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb(ProgrammeDay)}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         26-Mar-17 @ 6:54 PM
 */
public class PracticalSubjectForDayNotFoundException extends Exception {
    public PracticalSubjectForDayNotFoundException(String s) {
        super(s);
    }
}
