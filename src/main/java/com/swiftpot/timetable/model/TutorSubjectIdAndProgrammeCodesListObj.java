package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Feb-17 @ 1:15 PM
 */
public class TutorSubjectIdAndProgrammeCodesListObj {

    private String tutorSubjectId;

    private List<String> tutorProgrammeCodesList;

    public TutorSubjectIdAndProgrammeCodesListObj() {
    }

    public String getTutorSubjectId() {
        return tutorSubjectId;
    }

    public void setTutorSubjectId(String tutorSubjectId) {
        this.tutorSubjectId = tutorSubjectId;
    }

    public List<String> getTutorProgrammeCodesList() {
        return tutorProgrammeCodesList;
    }

    public void setTutorProgrammeCodesList(List<String> tutorProgrammeCodesList) {
        this.tutorProgrammeCodesList = tutorProgrammeCodesList;
    }
}
