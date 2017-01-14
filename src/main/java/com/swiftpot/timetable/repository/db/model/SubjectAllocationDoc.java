package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         23-Dec-16 @ 2:16 PM
 */
@Document(collection = "SubjectAllocationDoc")
public class SubjectAllocationDoc {

    @Id
    private String id;

    private String subjectCode;

    private int totalSubjectAllocation;

    private int yearGroup;

    public SubjectAllocationDoc() {
    }

    /**
     * we set only the subjectCode and yearGroup on first creation and totalSubjectAllocation will be updated later
     * @param subjectCode
     * @param yearGroup
     */
    public SubjectAllocationDoc(String subjectCode, int yearGroup) {
        this.subjectCode = subjectCode;
        this.yearGroup = yearGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getTotalSubjectAllocation() {
        return totalSubjectAllocation;
    }

    public void setTotalSubjectAllocation(int totalSubjectAllocation) {
        this.totalSubjectAllocation = totalSubjectAllocation;
    }

    public int getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(int yearGroup) {
        this.yearGroup = yearGroup;
    }
}
