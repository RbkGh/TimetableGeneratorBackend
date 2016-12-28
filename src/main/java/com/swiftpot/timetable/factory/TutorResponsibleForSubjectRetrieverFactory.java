package com.swiftpot.timetable.factory;

import com.swiftpot.timetable.base.TutorResponsibleForSubjectRetriever;
import com.swiftpot.timetable.base.impl.TutorResponsibleForSubjectRetrieverDefaultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 10:22 PM
 */
@Component
public class TutorResponsibleForSubjectRetrieverFactory {

    @Autowired
    TutorResponsibleForSubjectRetrieverDefaultImpl tutorResponsibleForSubjectRetrieverDefault;

    /**
     * @param type pass in "DEFAULT" to get default implementation
     * @return a class implementing {@link TutorResponsibleForSubjectRetriever}
     */
    public TutorResponsibleForSubjectRetriever getTutorResponsibleForSubjectRetrieverImpl(String type) {
        if (type.equalsIgnoreCase("DEFAULT")) {
            return tutorResponsibleForSubjectRetrieverDefault;
        } else
            return tutorResponsibleForSubjectRetrieverDefault;
    }

    /**
     * use this method to get default implementation without passing any string
     * @return
     */
    public TutorResponsibleForSubjectRetriever getTutorResponsibleForSubjectRetrieverImpl() {
        return tutorResponsibleForSubjectRetrieverDefault;
    }

}
