package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 2:20 PM
 */
public class YearGroup {
    private String yearName;
    private int yearNumber;
    private List<ProgrammeGroup> programmeGroupList;

    public YearGroup() {
    }

    public YearGroup(String yearName, int yearNumber, List<ProgrammeGroup> programmeGroupList) {
        this.yearName = yearName;
        this.yearNumber = yearNumber;
        this.programmeGroupList = programmeGroupList;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
    }

    public List<ProgrammeGroup> getProgrammeGroupList() {
        return programmeGroupList;
    }

    public void setProgrammeGroupList(List<ProgrammeGroup> programmeGroupList) {
        this.programmeGroupList = programmeGroupList;
    }
}
