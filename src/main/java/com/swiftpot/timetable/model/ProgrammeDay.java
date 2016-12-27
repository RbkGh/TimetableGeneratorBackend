package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 2:22 PM
 */
public class ProgrammeDay {
    private String dayName;
    private boolean isAllocated;
    private List<PeriodOrLecture> periodList;

    public ProgrammeDay() {
    }

    public ProgrammeDay(String dayName) {
        this.dayName = dayName;
    }

    public ProgrammeDay(String dayName, List<PeriodOrLecture> periodList) {
        this.dayName = dayName;
        this.periodList = periodList;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public boolean getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public List<PeriodOrLecture> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<PeriodOrLecture> periodList) {
        this.periodList = periodList;
    }

}