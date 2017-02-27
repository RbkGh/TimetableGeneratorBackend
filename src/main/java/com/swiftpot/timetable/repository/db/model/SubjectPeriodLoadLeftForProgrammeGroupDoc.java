package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 5:27 PM
 */
@Document(collection = "SubjectPeriodLoadLeftForProgrammeGroupDoc")
public class SubjectPeriodLoadLeftForProgrammeGroupDoc{

    @Id
    private String id;

    private String programmeCode;

    /**
     * the subject's {@link SubjectDoc#id id} in {@link SubjectDoc }
     */
    private String subjectUniqueIdInDb;

    private int periodLoadLeft;

    public SubjectPeriodLoadLeftForProgrammeGroupDoc() {
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public String getSubjectUniqueIdInDb() {
        return subjectUniqueIdInDb;
    }

    public void setSubjectUniqueIdInDb(String subjectUniqueIdInDb) {
        this.subjectUniqueIdInDb = subjectUniqueIdInDb;
    }

    public int getPeriodLoadLeft() {
        return periodLoadLeft;
    }

    public void setPeriodLoadLeft(int periodLoadLeft) {
        this.periodLoadLeft = periodLoadLeft;
    }
}
