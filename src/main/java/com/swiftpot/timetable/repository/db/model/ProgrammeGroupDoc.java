package com.swiftpot.timetable.repository.db.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 3:09 PM
 */
@Document(collection = "ProgrammeGroupDoc")
public class ProgrammeGroupDoc {

    @Id
    private String id;

    private String programmeFullName;

    private String programmeInitials;

    /*
    The current yearGroup of the students offering the course
     */
    private int yearGroup;

    /**
     * programmeCode generation algorithm = programmeInitials+yearGroup+A,B,C,D,E in that order
     */
    private String programmeCode;

    /**
     * default Classroomid  for programeGroup,ie eg. the classroom for Science 1
     */
    private String defaultClassRoomId;

    /**
     * @param yearGroupList
     * <p>The specific years partaking in that course</p>
     * eg. Electronic Engineering may be a new course,hence
     * only Year 1 Students may be part initially not year 2 or 3 students
     */
    private List<String> yearGroupList;

    List<String> programmeSubjectsCodeList;

    private boolean technicalWorkshopOrLabRequired;

    public ProgrammeGroupDoc() {
    }

    public String getProgrammeFullName() {
        return programmeFullName;
    }

    public void setProgrammeFullName(String programmeFullName) {
        this.programmeFullName = programmeFullName;
    }

    public String getProgrammeInitials() {
        return programmeInitials;
    }

    public void setProgrammeInitials(String programmeInitials) {
        this.programmeInitials = programmeInitials;
    }

    public int getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(int yearGroup) {
        this.yearGroup = yearGroup;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public String getDefaultClassRoomId() {
        return defaultClassRoomId;
    }

    public void setDefaultClassRoomId(String defaultClassRoomId) {
        this.defaultClassRoomId = defaultClassRoomId;
    }

    public List<String> getYearGroupList() {
        return yearGroupList;
    }

    public void setYearGroupList(List<String> yearGroupList) {
        this.yearGroupList = yearGroupList;
    }

    public List<String> getProgrammeSubjectsCodeList() {
        return programmeSubjectsCodeList;
    }

    public void setProgrammeSubjectsCodeList(List<String> programmeSubjectsCodeList) {
        this.programmeSubjectsCodeList = programmeSubjectsCodeList;
    }

    public boolean getIsTechnicalWorkshopOrLabRequired() {
        return technicalWorkshopOrLabRequired;
    }

    public void setTechnicalWorkshopOrLabRequired(boolean technicalWorkshopOrLabRequired) {
        this.technicalWorkshopOrLabRequired = technicalWorkshopOrLabRequired;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
