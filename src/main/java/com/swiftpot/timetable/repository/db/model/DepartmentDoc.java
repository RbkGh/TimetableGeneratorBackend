package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 6:39 PM
 */
@Document(collection = "DepartmentDoc")
public class DepartmentDoc {

    @Id
    private String id;

    private String deptName;

    /**
     * HOD tutorId,hod tutor id of hod
     */
    private String deptHODtutorId;

    /**
     * assistant HOD tutor id
     */
    private String deptHODdeputyTutorId;

    public DepartmentDoc() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptHODtutorId() {
        return deptHODtutorId;
    }

    public void setDeptHODtutorId(String deptHODtutorId) {
        this.deptHODtutorId = deptHODtutorId;
    }

    public String getDeptHODdeputyTutorId() {
        return deptHODdeputyTutorId;
    }

    public void setDeptHODdeputyTutorId(String deptHODdeputyTutorId) {
        this.deptHODdeputyTutorId = deptHODdeputyTutorId;
    }
}
