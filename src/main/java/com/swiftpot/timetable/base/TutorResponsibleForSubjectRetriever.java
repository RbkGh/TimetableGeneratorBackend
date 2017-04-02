/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base;

import com.swiftpot.timetable.exception.PracticalTutorNotFoundException;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 10:20 PM
 */
public interface TutorResponsibleForSubjectRetriever {
    /**
     * get uniqueId {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id} of tutor that is responsible <br>
     * or assigned to Subject <br>
     *
     * @param subjectUniqueIdInDb type {@linkplain com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param programmeCode       the programmeCode of the subject in whic we are searching for the tutor assigned.
     * @return {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id} of tutor that is responsible <br>
     * or assigned to Subject <br>
     */
    String getTutorObjectUniqueIdResponsibleForSubject(String subjectUniqueIdInDb, String programmeCode) throws PracticalTutorNotFoundException;
}
