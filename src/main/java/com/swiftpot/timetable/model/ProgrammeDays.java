package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 2:22 PM
 */
public class ProgrammeDays {
    private String dayName;
    private String isAllocated;
    private List<PeriodOrLecture> periodList;

    public ProgrammeDays() {
    }

    public ProgrammeDays(String dayName, List<PeriodOrLecture> periodList) {
        this.dayName = dayName;
        this.periodList = periodList;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(String isAllocated) {
        this.isAllocated = isAllocated;
    }

    public List<PeriodOrLecture> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<PeriodOrLecture> periodList) {
        this.periodList = periodList;
    }

}