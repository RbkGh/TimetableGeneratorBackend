package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

    private List<String> tutorCodeList;

    /**
     * HOD tutorCode
     */
    private String deptHODtutorCode;

    /**
     * assistant HOD tutorCode
     */
    private String deptHODdeputyTutorCode;

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

    public List<String> getTutorCodeList() {
        return tutorCodeList;
    }

    public void setTutorCodeList(List<String> tutorCodeList) {
        this.tutorCodeList = tutorCodeList;
    }

    public String getDeptHODtutorCode() {
        return deptHODtutorCode;
    }

    public void setDeptHODtutorCode(String deptHODtutorCode) {
        this.deptHODtutorCode = deptHODtutorCode;
    }

    public String getDeptHODdeputyTutorCode() {
        return deptHODdeputyTutorCode;
    }

    public void setDeptHODdeputyTutorCode(String deptHODdeputyTutorCode) {
        this.deptHODdeputyTutorCode = deptHODdeputyTutorCode;
    }
}
