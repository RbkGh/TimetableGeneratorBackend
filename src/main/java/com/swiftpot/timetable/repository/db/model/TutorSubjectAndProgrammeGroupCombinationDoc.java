
package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TutorSubjectAndProgrammeGroupCombinationDoc")
public class TutorSubjectAndProgrammeGroupCombinationDoc {

    /**
     * the {@link SubjectDoc#id} of the {@link SubjectDoc}
     */
    private String subjectUniqueId;

    /**
     * the {@link ProgrammeGroupDoc#programmeCode} of the {@link ProgrammeGroupDoc}
     */
    private String programmeCode;

    /**
     * the total periods left to be allocated for the subject and programmeGroup combination
     */
    private int totalPeriodLeftToBeAllocated;

    public TutorSubjectAndProgrammeGroupCombinationDoc(String subjectUniqueId, String programmeCode, int totalPeriodLeftToBeAllocated) {
        this.subjectUniqueId = subjectUniqueId;
        this.programmeCode = programmeCode;
        this.totalPeriodLeftToBeAllocated = totalPeriodLeftToBeAllocated;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        programmeCode = programmeCode;
    }

    public String getSubjectUniqueId() {
        return subjectUniqueId;
    }

    public void setSubjectUniqueId(String subjectUniqueId) {
        subjectUniqueId = subjectUniqueId;
    }

    public int getTotalPeriodLeftToBeAllocated() {
        return totalPeriodLeftToBeAllocated;
    }

    public void setTotalPeriodLeftToBeAllocated(int totalPeriodLeftToBeAllocated) {
        totalPeriodLeftToBeAllocated = totalPeriodLeftToBeAllocated;
    }

}
