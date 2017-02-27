package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Feb-17 @ 1:15 PM
 */
public class TutorSubjectIdAndProgrammeCodesListObj {

    /**
     * the subjectId ie {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id} of the {@link com.swiftpot.timetable.repository.db.model.SubjectDoc}
     */
    private String tutorSubjectId;

    /**
     * Equivalent to {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#programmeCode} property
     */
    private List<String> tutorProgrammeCodesList;

    public TutorSubjectIdAndProgrammeCodesListObj() {
    }

    /**
     * @return
     * @see TutorSubjectIdAndProgrammeCodesListObj#tutorSubjectId for documentation
     */
    public String getTutorSubjectId() {
        return tutorSubjectId;
    }

    public void setTutorSubjectId(String tutorSubjectId) {
        this.tutorSubjectId = tutorSubjectId;
    }

    /**
     * Equivalent to {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#programmeCode}'s programmeCode property
     */
    public List<String> getTutorProgrammeCodesList() {
        return tutorProgrammeCodesList;
    }

    public void setTutorProgrammeCodesList(List<String> tutorProgrammeCodesList) {
        this.tutorProgrammeCodesList = tutorProgrammeCodesList;
    }
}
