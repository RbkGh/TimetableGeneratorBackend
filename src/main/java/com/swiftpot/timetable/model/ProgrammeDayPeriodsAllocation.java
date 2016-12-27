package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 4:30 PM
 */
public class ProgrammeDayPeriodsAllocation extends ProgrammeDay {

    /**
     * this list must add up to 10 periods,hence
     * 2,2,3,3 or 3,2,2,3 is valid
     */
    List<Integer> periodsAllocationForDay;

    public ProgrammeDayPeriodsAllocation() {
    }

    public ProgrammeDayPeriodsAllocation(String dayName, List<Integer> periodsAllocationForDay) {
        super(dayName);
        this.periodsAllocationForDay = periodsAllocationForDay;
    }

    public List<Integer> getPeriodsAllocationForDay() {
        return periodsAllocationForDay;
    }

    public void setPeriodsAllocationForDay(List<Integer> periodsAllocationForDay) {
        this.periodsAllocationForDay = periodsAllocationForDay;
    }
}
