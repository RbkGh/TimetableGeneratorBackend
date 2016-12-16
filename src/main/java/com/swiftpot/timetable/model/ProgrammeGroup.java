package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 3:09 PM
 */
public class ProgrammeGroup {

    private String programmeFullName;

    private String programmeCode;

    /**
     * @param yearGroupList
     * <p>The specific years partaking in that course</p>
     * eg. Electronic Engineering may be a new course,hence
     * only Year 1 Students may be part initially not year 2 or 3 students
     */
    private List<String> yearGroupList;

    private boolean technicalWorkshopOrLabRequired;

    public ProgrammeGroup() {
    }

    public String getProgrammeFullName() {
        return programmeFullName;
    }

    public ProgrammeGroup setProgrammeFullName(String programmeFullName) {
        this.programmeFullName = programmeFullName;
        return this;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public ProgrammeGroup setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
        return this;
    }

    public List<String> getYearGroupList() {
        return yearGroupList;
    }

    public ProgrammeGroup setYearGroupList(List<String> yearGroupList) {
        this.yearGroupList = yearGroupList;
        return this;
    }

    public boolean isTechnicalWorkshopOrLabRequired() {
        return technicalWorkshopOrLabRequired;
    }

    public ProgrammeGroup setTechnicalWorkshopOrLabRequired(boolean technicalWorkshopOrLabRequired) {
        this.technicalWorkshopOrLabRequired = technicalWorkshopOrLabRequired;
        return this;
    }
}
