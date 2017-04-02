/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Apr-17 @ 1:00 AM
 */
public class TutorPeriodsToSetForProgrammeDay {

    private int periodStartingNumber;

    private int periodEndingNumber;

    private ProgrammeDay programmeDayToSetPeriodsTo;

    public TutorPeriodsToSetForProgrammeDay(ProgrammeDay programmeDayToSetPeriodsTo, int periodStartingNumber, int periodEndingNumber) {
        this.periodStartingNumber = periodStartingNumber;
        this.periodEndingNumber = periodEndingNumber;
        this.programmeDayToSetPeriodsTo = programmeDayToSetPeriodsTo;
    }

    public int getPeriodStartingNumber() {
        return periodStartingNumber;
    }

    public void setPeriodStartingNumber(int periodStartingNumber) {
        this.periodStartingNumber = periodStartingNumber;
    }

    public int getPeriodEndingNumber() {
        return periodEndingNumber;
    }

    public void setPeriodEndingNumber(int periodEndingNumber) {
        this.periodEndingNumber = periodEndingNumber;
    }

    public ProgrammeDay getProgrammeDayToSetPeriodsTo() {
        return programmeDayToSetPeriodsTo;
    }

    public void setProgrammeDayToSetPeriodsTo(ProgrammeDay programmeDayToSetPeriodsTo) {
        this.programmeDayToSetPeriodsTo = programmeDayToSetPeriodsTo;
    }
}
