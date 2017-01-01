package com.swiftpot.timetable.base;

import com.swiftpot.timetable.model.YearGroup;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         01-Jan-17 @ 3:59 PM
 */
public abstract class TimeTableSuperDocBase {

    List<YearGroup> yearGroupsListBase;

    public TimeTableSuperDocBase() {
    }

    public void setYearGroupsListBase(List<YearGroup> yearGroupsListBase) {
        this.yearGroupsListBase = yearGroupsListBase;
    }

    /**
     * get YearGroup by yearGroupNumber
     * @param yearGroupNumber
     * @return {@link YearGroup}
     */
    public YearGroup getYearGroup(int yearGroupNumber) {
        return null;
    }

    /**
     * get YearGroup by YearGroupName
     * @param yearGroupName
     * @return
     */
    public YearGroup getYearGroup(String yearGroupName) {
        return null;
    }

    /**
     * get a count of total Number of yearGroups
     * @return
     */
    public int getAllYearGroupsSize() {
        return yearGroupsListBase.size();
    }
}
