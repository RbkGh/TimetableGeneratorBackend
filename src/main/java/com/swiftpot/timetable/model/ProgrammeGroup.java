package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 2:21 PM
 */
public class ProgrammeGroup {
    private String programmeCode;
    List<String> programmeSubjectsUniqueIdInDbList;
    private boolean isProgrammeRequiringPracticalsClassroom;
    private List<ProgrammeDay> programmeDaysList;

    public ProgrammeGroup() {
    }

    public ProgrammeGroup(String programmeCode, List<ProgrammeDay> programmeDaysList,boolean isProgrammeRequiringPracticalsClassroom) {
        this.programmeCode = programmeCode;
        this.programmeDaysList = programmeDaysList;
        this.isProgrammeRequiringPracticalsClassroom = isProgrammeRequiringPracticalsClassroom;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public boolean getIsProgrammeRequiringPracticalsClassroom() {
        return isProgrammeRequiringPracticalsClassroom;
    }

    public void setIsProgrammeRequiringPracticalsClassroom(boolean isProgrammeRequiringPracticalsClassroom) {
        this.isProgrammeRequiringPracticalsClassroom = isProgrammeRequiringPracticalsClassroom;
    }

    public List<String> getProgrammeSubjectsUniqueIdInDbList() {
        return programmeSubjectsUniqueIdInDbList;
    }

    public void setProgrammeSubjectsUniqueIdInDbList(List<String> programmeSubjectsUniqueIdInDbList) {
        this.programmeSubjectsUniqueIdInDbList = programmeSubjectsUniqueIdInDbList;
    }

    public List<ProgrammeDay> getProgrammeDaysList() {
        return programmeDaysList;
    }

    public void setProgrammeDaysList(List<ProgrammeDay> programmeDaysList) {
        this.programmeDaysList = programmeDaysList;
    }
}
