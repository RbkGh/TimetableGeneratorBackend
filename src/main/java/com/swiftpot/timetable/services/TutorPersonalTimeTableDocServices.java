package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         03-Mar-17 @ 11:47 AM
 */
@Service
public class TutorPersonalTimeTableDocServices {

    @Autowired
    TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;

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
     * @param periodNumberToStopSettingSubject
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
     * @param periodNumberToStopSettingSubject
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

        int indexLocationOfProgrammeDayName = this.getProgrammeDayIndexLocation(tutorProgrammeDaysList, programmeDayName);
        ProgrammeDay programmeDayToSetThePeriodsAndTutorIdTo =
                this.getProgrammeDayToSetTheIncomingPeriodsAndTutoridTo(tutorProgrammeDaysList, programmeDayName);

        ProgrammeDay programmeDayWithEverythingSet =
                this.setPeriodsOnTutorsProgrammeDayTimetable(programmeDayToSetThePeriodsAndTutorIdTo,
                        tutorUniqueIdInDb,
                        subjectUniqueIdInDb,
                        periodNumberToStartSettingSubject,
                        periodNumberToStopSettingSubject);
        tutorPersonalTimeTableDoc.getProgrammeDaysList().set(indexLocationOfProgrammeDayName, programmeDayWithEverythingSet);
        return tutorPersonalTimeTableDoc;
    }

    ProgrammeDay getProgrammeDayToSetTheIncomingPeriodsAndTutoridTo(List<ProgrammeDay> programmeDaysList, String programmeDayNameToFind) {
        ProgrammeDay programmeDayToSetThePeriodsAndTutorIdTo = null;
        for (int i = 0; i < programmeDaysList.size(); i++) {
            ProgrammeDay programmeDay = programmeDaysList.get(i);
            if (Objects.equals(programmeDay.getDayName(), programmeDayNameToFind)) {
                programmeDayToSetThePeriodsAndTutorIdTo = programmeDay;
                break;
            }
        }
        return programmeDayToSetThePeriodsAndTutorIdTo;
    }

    int getProgrammeDayIndexLocation(List<ProgrammeDay> programmeDaysList, String programmeDayNameToFind) {
        int indexOfProgrammeDay = 0;
        for (int i = 0; i < programmeDaysList.size(); i++) {
            ProgrammeDay programmeDay = programmeDaysList.get(i);
            if (Objects.equals(programmeDay.getDayName(), programmeDayNameToFind)) {
                indexOfProgrammeDay = i;
                break;
            }
        }
        return indexOfProgrammeDay;
    }

    /**
     * set periods and tutor of subject for the affected periods on a particular programmeDay.
     *
     * @param programmeDay                      the {@link ProgrammeDay} to set the periods on.
     * @param tutorUniqueIdInDb                 the tutor's unique {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id}
     * @param subjectUniqueIdInDb               the subject's unique {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param periodNumberToStartSettingSubject the period number to start setting the subject and tutor ids from .
     * @param periodNumberToStopSettingSubject  the periodd number to stop setting the subject and ids.
     * @return TutorPersonalTimeTableDoc
     */
    ProgrammeDay setPeriodsOnTutorsProgrammeDayTimetable(ProgrammeDay programmeDay,
                                                         String tutorUniqueIdInDb,
                                                         String subjectUniqueIdInDb,
                                                         int periodNumberToStartSettingSubject,
                                                         int periodNumberToStopSettingSubject) {
        List<PeriodOrLecture> periodOrLecturesInDay = programmeDay.getPeriodList();
        for (PeriodOrLecture periodOrLecture : periodOrLecturesInDay) {
            int currentPeriodNumber = periodOrLecture.getPeriodNumber();
            if ((currentPeriodNumber >= periodNumberToStartSettingSubject) && (currentPeriodNumber <= periodNumberToStopSettingSubject)) {
                periodOrLecture.setTutorUniqueId(tutorUniqueIdInDb);
                periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb);
                periodOrLecture.setIsAllocated(true);
            }
        }
        return programmeDay;
    }
}
