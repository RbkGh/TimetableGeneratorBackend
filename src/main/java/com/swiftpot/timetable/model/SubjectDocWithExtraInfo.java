package com.swiftpot.timetable.model;

import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         24-Jan-17 @ 2:16 PM
 */
public class SubjectDocWithExtraInfo {

    List<SubjectAllocationDoc> subjectAllocationDocs;
    SubjectDoc subjectDoc;


    public SubjectDocWithExtraInfo() {
    }

    public SubjectDocWithExtraInfo(SubjectDoc subjectDoc, List<SubjectAllocationDoc> subjectAllocationDocs) {
        this.subjectDoc = subjectDoc;
        this.subjectAllocationDocs = subjectAllocationDocs;
    }

    public List<SubjectAllocationDoc> getSubjectAllocationDocs() {
        return subjectAllocationDocs;
    }

    public void setSubjectAllocationDocs(List<SubjectAllocationDoc> subjectAllocationDocs) {
        this.subjectAllocationDocs = subjectAllocationDocs;
    }

    public SubjectDoc getSubjectDoc() {
        return subjectDoc;
    }

    public void setSubjectDoc(SubjectDoc subjectDoc) {
        this.subjectDoc = subjectDoc;
    }
}
