/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         03-Mar-17 @ 11:47 AM
 */
@Service
public class TutorPersonalTimeTableDocServices {

    @Autowired
    TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    ProgrammeDayServices programmeDayServices;

    /**
     * This will retrieve the {@link com.swiftpot.timetable.repository.db.model.TutorDoc}'s personal <br>
     * {@link com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc} and update the incoming <br>
     * periodDayNumber,where to start setting the subject from,and the tutorUniqueIdInDb itself and other necessary info.
     * into database
     *
     * @param tutorUniqueIdInDb                 the tutor's uniqueId in database ie. {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id}
     * @param subjectUniqueIdInDb               the subject to be set 's unique id in database ie. {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param programmeDayName                  the programme day name to set ie. {@link com.swiftpot.timetable.model.ProgrammeDay#dayName} .eg Monday could have "Monday" as the programme day name
     * @param periodNumberToStartSettingSubject the index to start setting the periods from ie. {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @param periodNumberToStopSettingSubject  the period number to stop setting the periods ie {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @return TutorPersonalTimeTableDoc
     */
    public synchronized TutorPersonalTimeTableDoc updateTutorPersonalTimeTableDocWithPeriodsAndSaveInDb(String tutorUniqueIdInDb,
                                                                                                        String subjectUniqueIdInDb,
                                                                                                        String programmeDayName,
                                                                                                        int periodNumberToStartSettingSubject,
                                                                                                        int periodNumberToStopSettingSubject) {
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc =
                this.getTutorPersonalTimeTableWithIncomingPeriodsSet(tutorUniqueIdInDb,
                        subjectUniqueIdInDb,
                        programmeDayName,
                        periodNumberToStartSettingSubject,
                        periodNumberToStopSettingSubject);
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDocSaved = tutorPersonalTimeTableDocRepository.save(tutorPersonalTimeTableDoc);
        return tutorPersonalTimeTableDocSaved;
    }

    /**
     * This will retrieve the {@link com.swiftpot.timetable.repository.db.model.TutorDoc}'s personal <br>
     * {@link com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc} and update the incoming <br>
     * periodDayNumber,where to start setting the subject from,and the tutorUniqueIdInDb itself and other necessary info.
     * <b>WITHOUT SAVING INTO DATABASE!!!</b>
     *
     * @param tutorUniqueIdInDb                 the tutor's uniqueId in database ie. {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id}
     * @param subjectUniqueIdInDb               the subject to be set 's unique id in database ie. {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param programmeDayName                  the programme day name to set ie. {@link com.swiftpot.timetable.model.ProgrammeDay#dayName} .eg Monday could have "Monday" as the programme day name
     * @param periodNumberToStartSettingSubject the index to start setting the periods from ie. {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @param periodNumberToStopSettingSubject  the period number to stop setting the periods ie {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @return TutorPersonalTimeTableDoc
     */
    public synchronized TutorPersonalTimeTableDoc getTutorPersonalTimeTableWithIncomingPeriodsSet(String tutorUniqueIdInDb,
                                                                                                  String subjectUniqueIdInDb,
                                                                                                  String programmeDayName,
                                                                                                  int periodNumberToStartSettingSubject,
                                                                                                  int periodNumberToStopSettingSubject) {
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc =
                tutorPersonalTimeTableDocRepository.findByTutorUniqueIdInDb(tutorUniqueIdInDb);
        List<ProgrammeDay> tutorProgrammeDaysList = tutorPersonalTimeTableDoc.getProgrammeDaysList();

        int indexLocationOfProgrammeDayName = programmeDayServices.getProgrammeDayIndexLocation(tutorProgrammeDaysList, programmeDayName);
        ProgrammeDay programmeDayToSetThePeriodsAndTutorIdTo =
                programmeDayServices.getProgrammeDayToSetTheIncomingPeriodsAndTutoridTo(tutorProgrammeDaysList, programmeDayName);

        ProgrammeDay programmeDayWithEverythingSet =
                programmeDayServices.setPeriodsOnProgrammeDayTimetable(programmeDayToSetThePeriodsAndTutorIdTo,
                        tutorUniqueIdInDb,
                        subjectUniqueIdInDb,
                        periodNumberToStartSettingSubject,
                        periodNumberToStopSettingSubject);
        tutorPersonalTimeTableDoc.getProgrammeDaysList().set(indexLocationOfProgrammeDayName, programmeDayWithEverythingSet);
        return tutorPersonalTimeTableDoc;
    }



}
