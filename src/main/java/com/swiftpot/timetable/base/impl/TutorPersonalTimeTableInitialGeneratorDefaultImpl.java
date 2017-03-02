package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorPersonalTimeTableInitialGenerator;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Mar-17 @ 8:29 PM
 */
@Component
public class TutorPersonalTimeTableInitialGeneratorDefaultImpl implements TutorPersonalTimeTableInitialGenerator {

    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;


    @Override
    public void generatePersonalTimeTableForAllTutorsInDbAndSaveIntoDb() throws Exception {
        List<TutorDoc> allTutorDocsInDb = tutorDocRepository.findAll();
        List<TutorPersonalTimeTableDoc> allTutorPersonalTimeTableDocs = this.generatePersonalTimeTableForAllTutorsInDb(allTutorDocsInDb);
        tutorPersonalTimeTableDocRepository.save(allTutorPersonalTimeTableDocs);
    }

    @Override
    public List<TutorPersonalTimeTableDoc> generatePersonalTimeTableForAllTutorsInDb(List<TutorDoc> tutorDocs) throws Exception {
        List<TutorPersonalTimeTableDoc> allTutorPersonalTimeTableDocs = new ArrayList<>();
        for (TutorDoc tutorDoc : tutorDocs) {
            List<ProgrammeDay> programmeDaysListWithPeriodsForTutorTimetable = timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
            TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = new TutorPersonalTimeTableDoc();
            tutorPersonalTimeTableDoc.setTutorUniqueIdInDb(tutorDoc.getId());
            tutorPersonalTimeTableDoc.setProgrammeDaysList(programmeDaysListWithPeriodsForTutorTimetable);
            allTutorPersonalTimeTableDocs.add(tutorPersonalTimeTableDoc);
        }
        return allTutorPersonalTimeTableDocs;
    }
}
