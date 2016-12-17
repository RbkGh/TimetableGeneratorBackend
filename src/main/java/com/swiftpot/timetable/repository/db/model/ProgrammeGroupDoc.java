package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 3:09 PM
 */
@Document
public class ProgrammeGroupDoc {

    @Id
    private String id;

    private String programmeFullName;

    private String programmeCode;

    /**
     * @param yearGroupList
     * <p>The specific years partaking in that course</p>
     * eg. Electronic Engineering may be a new course,hence
     * only Year 1 Students may be part initially not year 2 or 3 students
     */
    private List<String> yearGroupList;

    private List<String> electiveSubjectsCodeList;

    private boolean technicalWorkshopOrLabRequired;

    public ProgrammeGroupDoc() {
    }

    public String getProgrammeFullName() {
        return programmeFullName;
    }

    public ProgrammeGroupDoc setProgrammeFullName(String programmeFullName) {
        this.programmeFullName = programmeFullName;
        return this;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public ProgrammeGroupDoc setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
        return this;
    }

    public List<String> getYearGroupList() {
        return yearGroupList;
    }

    public ProgrammeGroupDoc setYearGroupList(List<String> yearGroupList) {
        this.yearGroupList = yearGroupList;
        return this;
    }

    public List<String> getElectiveSubjectsCodeList() {
        return electiveSubjectsCodeList;
    }

    public ProgrammeGroupDoc setElectiveSubjectsCodeList(List<String> electiveSubjectsCodeList) {
        this.electiveSubjectsCodeList = electiveSubjectsCodeList;
        return this;
    }

    public boolean isTechnicalWorkshopOrLabRequired() {
        return technicalWorkshopOrLabRequired;
    }

    public ProgrammeGroupDoc setTechnicalWorkshopOrLabRequired(boolean technicalWorkshopOrLabRequired) {
        this.technicalWorkshopOrLabRequired = technicalWorkshopOrLabRequired;
        return this;
    }
}
