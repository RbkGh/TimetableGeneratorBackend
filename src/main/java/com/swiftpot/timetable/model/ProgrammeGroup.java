package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 2:21 PM
 */
public class ProgrammeGroup {
    private String programmeCode;
    private List<ProgrammeDays> programmeDaysList;

    public ProgrammeGroup() {
    }

    public ProgrammeGroup(String programmeCode, List<ProgrammeDays> programmeDaysList) {
        this.programmeCode = programmeCode;
        this.programmeDaysList = programmeDaysList;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public List<ProgrammeDays> getProgrammeDaysList() {
        return programmeDaysList;
    }

    public void setProgrammeDaysList(List<ProgrammeDays> programmeDaysList) {
        this.programmeDaysList = programmeDaysList;
    }
}
