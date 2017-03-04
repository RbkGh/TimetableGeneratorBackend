package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.ProgrammeGroupPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 9:06 PM
 */
@Service
public class ProgrammeGroupPersonalTimeTableDocServices {

    @Autowired
    ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    ProgrammeDayServices programmeDayServices;


    /**
     * This will retrieve the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc}'s personal <br>
     * {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc} and update the incoming <br>
     * periodDayNumber,where to start setting the subject from,and the tutorUniqueIdInDb itself and other necessary info.
     * into database
     *
     * @param tutorUniqueIdInDb                 the {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id} of the tutor to set.
     * @param programmeCode                     the {@link com.swiftpot.timetable.model.ProgrammeGroup}'s {@link com.swiftpot.timetable.model.ProgrammeGroup#programmeCode} in database
     * @param subjectUniqueIdInDb               the subject to be set 's unique id in database ie. {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param programmeDayName                  the programme day name to set ie. {@link com.swiftpot.timetable.model.ProgrammeDay#dayName} .eg Monday could have "Monday" as the programme day name
     * @param periodNumberToStartSettingSubject the index to start setting the periods from ie. {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @param periodNumberToStopSettingSubject
     * @return ProgrammeGroupPersonalTimeTableDoc
     */
    public synchronized ProgrammeGroupPersonalTimeTableDoc updateProgrammeGroupPersonalTimeTableDocWithPeriodsAndSaveInDb(String tutorUniqueIdInDb,
                                                                                                                          String programmeCode,
                                                                                                                          String subjectUniqueIdInDb,
                                                                                                                          String programmeDayName,
                                                                                                                          int periodNumberToStartSettingSubject,
                                                                                                                          int periodNumberToStopSettingSubject) {
        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc =
                this.getTutorPersonalTimeTableWithIncomingPeriodsSet(tutorUniqueIdInDb,
                        programmeCode,
                        subjectUniqueIdInDb,
                        programmeDayName,
                        periodNumberToStartSettingSubject,
                        periodNumberToStopSettingSubject);
        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDocSaved = programmeGroupPersonalTimeTableDocRepository.save(programmeGroupPersonalTimeTableDoc);
        return programmeGroupPersonalTimeTableDocSaved;
    }

    /**
     * This will retrieve the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc}'s personal <br>
     * {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc} and update the incoming <br>
     * periodDayNumber,where to start setting the subject from,and the tutorUniqueIdInDb itself and other necessary info.
     * <b>WITHOUT SAVING INTO DATABASE!!!</b>
     *
     * @param tutorUniqueIdInDb                 the {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id} of the tutor to set.*
     * @param programmeCode                     the {@link com.swiftpot.timetable.model.ProgrammeGroup}'s {@link com.swiftpot.timetable.model.ProgrammeGroup#programmeCode} in database
     * @param subjectUniqueIdInDb               the subject to be set 's unique id in database ie. {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param programmeDayName                  the programme day name to set ie. {@link com.swiftpot.timetable.model.ProgrammeDay#dayName} .eg Monday could have "Monday" as the programme day name
     * @param periodNumberToStartSettingSubject the index to start setting the periods from ie. {@link com.swiftpot.timetable.model.PeriodOrLecture#periodNumber} eg. period 3,
     * @param periodNumberToStopSettingSubject
     * @return ProgrammeGroupPersonalTimeTableDoc
     */
    public synchronized ProgrammeGroupPersonalTimeTableDoc getTutorPersonalTimeTableWithIncomingPeriodsSet(String tutorUniqueIdInDb,
                                                                                                           String programmeCode,
                                                                                                           String subjectUniqueIdInDb,
                                                                                                           String programmeDayName,
                                                                                                           int periodNumberToStartSettingSubject,
                                                                                                           int periodNumberToStopSettingSubject) {
        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoclTimeTableDoc =
                programmeGroupPersonalTimeTableDocRepository.findByProgrammeCodeIgnoreCase(programmeCode);
        List<ProgrammeDay> tutorProgrammeDaysList = programmeGroupPersonalTimeTableDoclTimeTableDoc.getProgrammeDaysList();

        int indexLocationOfProgrammeDayName = programmeDayServices.getProgrammeDayIndexLocation(tutorProgrammeDaysList, programmeDayName);
        ProgrammeDay programmeDayToSetThePeriodsAndTutorIdTo =
                programmeDayServices.getProgrammeDayToSetTheIncomingPeriodsAndTutoridTo(tutorProgrammeDaysList, programmeDayName);

        ProgrammeDay programmeDayWithEverythingSet =
                programmeDayServices.setPeriodsOnProgrammeDayTimetable(programmeDayToSetThePeriodsAndTutorIdTo,
                        tutorUniqueIdInDb,
                        subjectUniqueIdInDb,
                        periodNumberToStartSettingSubject,
                        periodNumberToStopSettingSubject);
        programmeGroupPersonalTimeTableDoclTimeTableDoc.getProgrammeDaysList().set(indexLocationOfProgrammeDayName, programmeDayWithEverythingSet);
        return programmeGroupPersonalTimeTableDoclTimeTableDoc;
    }

}
