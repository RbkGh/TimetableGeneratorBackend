package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 3:28 PM
 */
@Document(collection = "SubjectDoc")
public class SubjectDoc {

    @Id
    private String id;

    private String subjectFullName;

    /**
     * use with {@link SubjectAllocationDoc#yearGroup} to get subject allocation doc only,and nothing else
     */
    private String subjectCode;

    /**
     * @param subjectYearGroupList
     * YearGroups offering this Subject =>1,2,3 meaning all 3 year groups offer the subject
     */
    private List<Integer> subjectYearGroupList;

    /**
     * "CORE" OR "ELECTIVE" only
     */
    private String subjectType;

    /**
     * set to true if subject is a practical subject,false if otherwise
     */
    private boolean isSubjectAPracticalSubject;

    /**
     * used only during check to find if subject is allocated to frontend,not used in backend operations
     */
    private boolean isAllSubjectYearGroupsAllocated;

    public SubjectDoc() {
    }

    public String getId() {
        return id;
    }

    public SubjectDoc setId(String id) {
        this.id = id;
        return this;
    }

    public String getSubjectFullName() {
        return subjectFullName;
    }

    public SubjectDoc setSubjectFullName(String subjectFullName) {
        this.subjectFullName = subjectFullName;
        return this;
    }

    /**
     *use with {@link SubjectAllocationDoc#yearGroup} to get subject allocation doc only,and nothing else
     */
    public String getSubjectCode() {
        return subjectCode;
    }

    /**
     * use with {@link SubjectAllocationDoc#yearGroup} to get subject allocation doc only,and nothing else
     */
    @Deprecated
    public SubjectDoc setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }

    public List<Integer> getSubjectYearGroupList() {
        return subjectYearGroupList;
    }

    public SubjectDoc setSubjectYearGroupList(List<Integer> subjectYearGroupList) {
        this.subjectYearGroupList = subjectYearGroupList;
        return this;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public SubjectDoc setSubjectType(String subjectType) {
        this.subjectType = subjectType;
        return this;
    }

    public boolean isSubjectAPracticalSubject() {
        return isSubjectAPracticalSubject;
    }

    public void setIsSubjectAPracticalSubject(boolean isSubjectAPracticalSubject) {
        this.isSubjectAPracticalSubject = isSubjectAPracticalSubject;
    }

    public boolean isAllSubjectYearGroupsAllocated() {
        return isAllSubjectYearGroupsAllocated;
    }

    public void setIsAllSubjectYearGroupsAllocated(boolean isAllSubjectYearGroupsAllocated) {
        this.isAllSubjectYearGroupsAllocated = isAllSubjectYearGroupsAllocated;
    }
}
