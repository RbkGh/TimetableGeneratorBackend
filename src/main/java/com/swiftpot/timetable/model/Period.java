package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 11:13 AM
 */
public class Period {

    private boolean isAllocated;

    /**
     * @param timePeriodInDay
     * of the format HH:MM-HH:MM GMT
     */
    private String timePeriodInDay;

    public Period() {
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    public Period setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
        return this;
    }

    public String getTimePeriodInDay() {
        return timePeriodInDay;
    }

    public Period setTimePeriodInDay(String timePeriodInDay) {
        this.timePeriodInDay = timePeriodInDay;
        return this;
    }
}
