package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Feb-17 @ 2:56 PM
 */
@Service
public class GeneralAbstractServices {

    @Autowired
    TutorDocRepository tutorDocRepository;

    /**
     * during every update and deletion of subject,& during every programmeGroup update and deletion,reset the tutor property references
     */
    public void resetAllTutorsSubjectIdAndProgrammeCodeListProperty() {
        new Thread(() -> {
            List<TutorDoc> allTutorDocsInDb = tutorDocRepository.findAll();
            if (!allTutorDocsInDb.isEmpty()) {
                allTutorDocsInDb.forEach(tutorDoc -> tutorDoc.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)));//set to zero or empty list
                tutorDocRepository.save(allTutorDocsInDb);
            }
        }).start();
    }
}
