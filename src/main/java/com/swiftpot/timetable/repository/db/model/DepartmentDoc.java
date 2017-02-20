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

    public static final String DEPARTMENT_TYPE_CORE = "CORE";
    public static final String DEPARTMENT_TYPE_ELECTIVE = "ELECTIVE";

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
    /**
     * TODO DONE!!! Ensure that programmeInitials is never duplicated during creation and updating of this Doc
     */
    private String deptProgrammeInitials;

    /**
     * "CORE" OR "ELECTIVE" only
     */
    private String deptType;

    /**
     * Disallow Editing of ProgrammeSubjectsDocIdList during updating of doc if possible
     */
    public List<String> programmeSubjectsDocIdList;

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

    public String getDeptProgrammeInitials() {
        return deptProgrammeInitials;
    }

    public void setDeptProgrammeInitials(String deptProgrammeInitials) {
        this.deptProgrammeInitials = deptProgrammeInitials;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public List<String> getProgrammeSubjectsDocIdList() {
        return programmeSubjectsDocIdList;
    }

    public void setProgrammeSubjectsDocIdList(List<String> programmeSubjectsDocIdList) {
        this.programmeSubjectsDocIdList = programmeSubjectsDocIdList;
    }
}
