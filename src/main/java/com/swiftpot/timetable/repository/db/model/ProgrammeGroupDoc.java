package com.swiftpot.timetable.repository.db.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    /**
     * programmeInitials,do not allow it to be editable on frontend,if it must be changed,then ProgrammeGroupDoc must be deleted.
     */
    private String programmeInitials;
    /**
     * The current yearGroup of the students offering the course
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
    private List<Integer> yearGroupList;

    /**
     * @deprecated use {@linkplain DepartmentDoc#programmeSubjectsDocIdList} instead
     */
    @Deprecated
    List<String> programmeSubjectsCodeList;

    private boolean technicalWorkshopOrLabRequired;

    public ProgrammeGroupDoc() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Integer> getYearGroupList() {
        return yearGroupList;
    }

    public void setYearGroupList(List<Integer> yearGroupList) {
        this.yearGroupList = yearGroupList;
    }

    /**
     * @deprecated use {@linkplain DepartmentDoc#programmeSubjectsDocIdList} instead
     */
    public List<String> getProgrammeSubjectsCodeList() {
        return programmeSubjectsCodeList;
    }

    /**
     * @deprecated use {@linkplain DepartmentDoc#programmeSubjectsDocIdList} instead
     */
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

    /**
     * overriding equals and hashcode because we'll be comparing objects of ProgrammeGroup
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof ProgrammeGroupDoc)) {
            return false;
        }

        ProgrammeGroupDoc programmeGroupDoc = (ProgrammeGroupDoc) o;

        return new EqualsBuilder()
                .append(id, programmeGroupDoc.id)
                .isEquals();
    }


    /**
     * overriding equals and hashcode because we'll be comparing objects of ProgrammeGroup
     *
     * @return
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

}
