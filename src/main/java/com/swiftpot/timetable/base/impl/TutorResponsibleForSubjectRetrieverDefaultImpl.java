package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorResponsibleForSubjectRetriever;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 10:39 PM
 */
@Component
public class TutorResponsibleForSubjectRetrieverDefaultImpl implements TutorResponsibleForSubjectRetriever {
    @Override
    public String getTutorCodeResponsibleForSubject(String subjectCode) {
        return null;
    }
}
