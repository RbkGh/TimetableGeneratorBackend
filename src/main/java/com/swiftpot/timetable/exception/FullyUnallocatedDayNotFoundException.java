/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.exception;

/**
 * A {@link com.swiftpot.timetable.model.ProgrammeDay} that has no periods allocated in order to set {2,2,2,2,2} periods not found,then throw this exception
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Apr-17 @ 1:00 PM
 */
public class FullyUnallocatedDayNotFoundException extends Exception {
    public FullyUnallocatedDayNotFoundException(String s) {
        super(s);
    }
}
