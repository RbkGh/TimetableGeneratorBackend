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

    private String subjectCode;

    private int totalPeriodsForYearGroup;


    /**
     *@param subjectYearGroupList
     * YearGroups offering this Subject =>1,2,3 meaning all 3 year groups offer the subject
     */
    private List<Integer> subjectYearGroupList;

    /**
     * "CORE" OR "ELECTIVE" only
     */
    private String subjectType;


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

    public String getSubjectCode() {
        return subjectCode;
    }

    public SubjectDoc setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }

    public int getTotalPeriodsForYearGroup() {
        return totalPeriodsForYearGroup;
    }

    public SubjectDoc setTotalPeriodsForYearGroup(int totalPeriodsForYearGroup) {
        this.totalPeriodsForYearGroup = totalPeriodsForYearGroup;
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
}
