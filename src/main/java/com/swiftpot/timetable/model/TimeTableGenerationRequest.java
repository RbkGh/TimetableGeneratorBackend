/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Mar-17 @ 3:02 PM
 */
public class TimeTableGenerationRequest {

    /**
     * timetable name eg. Term2, or term2A
     */
    private String timeTableName;

    /**
     * yearGroup to create timetable for eg 2016
     */
    private int yearGroup;

    public String getTimeTableName() {
        return timeTableName;
    }

    public TimeTableGenerationRequest setTimeTableName(String timeTableName) {
        this.timeTableName = timeTableName;
        return this;
    }

    public int getYearGroup() {
        return yearGroup;
    }

    public TimeTableGenerationRequest setYearGroup(int yearGroup) {
        this.yearGroup = yearGroup;
        return this;
    }
}
