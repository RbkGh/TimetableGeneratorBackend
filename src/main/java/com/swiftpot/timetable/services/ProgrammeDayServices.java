package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * This will be used to retrieve helper methods for retrieving info about a {@link com.swiftpot.timetable.model.ProgrammeDay} or list of {@link com.swiftpot.timetable.model.ProgrammeDay}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 9:28 PM
 */
@Service
public class ProgrammeDayServices {

    /**
     * get the indexlocation of a {@link ProgrammeDay} in a {@link List} of {@link ProgrammeDay} list based on the @param programmeDayNameToFind passed in
     *
     * @param programmeDaysList      the list of ProgrammeDays used to search for the @param programmeDayNameToFind
     * @param programmeDayNameToFind the actual programmeDayName to find,eg search for "MONDAY" in list of days passed in
     * @return int => the index location where the programmeDayNameToFind was found
     */
    public int getProgrammeDayIndexLocation(List<ProgrammeDay> programmeDaysList, String programmeDayNameToFind) {
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
     * get the {@link ProgrammeDay} to set the incoming Periods and TutorId to on the timetable object.
     *
     * @param programmeDaysList      the {@link List} of {@link ProgrammeDay} to search the {@param programmeDayNameToFind} on.
     * @param programmeDayNameToFind the actual programmeDayName to find,eg search for "MONDAY" in list of days passed in
     * @return {@link ProgrammeDay}
     */
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

    /**
     * set periods and tutor of subject for the affected periods on a particular programmeDay.
     *
     * @param programmeDay                      the {@link ProgrammeDay} to set the periods on.
     * @param tutorUniqueIdInDb                 the tutor's unique {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id}
     * @param subjectUniqueIdInDb               the subject's unique {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id}
     * @param periodNumberToStartSettingSubject the period number to start setting the subject and tutor ids from .
     * @param periodNumberToStopSettingSubject  the periodd number to stop setting the subject and ids.
     * @return {@link ProgrammeDay} with the periods set to the subject and tutor.
     */
    ProgrammeDay setPeriodsOnProgrammeDayTimetable(ProgrammeDay programmeDay,
                                                   String tutorUniqueIdInDb,
                                                   String subjectUniqueIdInDb,
                                                   int periodNumberToStartSettingSubject,
                                                   int periodNumberToStopSettingSubject) {
        //setPeriodsOnProgrammeDayTimetable
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

    /**
     * get the total number of {@link PeriodOrLecture} with {@link PeriodOrLecture#isAllocated} == false in the passed in {@link ProgrammeDay}
     *
     * @param programmeDay the {@link ProgrammeDay} to check for the number of unallocated periods
     * @return the number of {@link PeriodOrLecture} with {@link PeriodOrLecture#isAllocated} == false
     */
    public int getNumberOfUnallocatedPeriodsInDay(ProgrammeDay programmeDay) {
        int numberOfUnallocatedPeriodsInTheDay = 0;
        List<PeriodOrLecture> periodOrLecturesInDay = programmeDay.getPeriodList();
        for (PeriodOrLecture periodOrLecture : periodOrLecturesInDay) {
            if (!periodOrLecture.getIsAllocated()) {
                numberOfUnallocatedPeriodsInTheDay += 1;
            }
        }
        return numberOfUnallocatedPeriodsInTheDay;
    }

    /**
     * check if {@link ProgrammeDay}'s {@link List} of {@link PeriodOrLecture} are all allocated
     *
     * @param programmeDay
     * @return true if {@link ProgrammeDay}'s {@link List} of {@link PeriodOrLecture} are all {@link PeriodOrLecture#isAllocated} ==true,false if otherwise
     */
    public boolean isProgrammeDayFullyAllocated(ProgrammeDay programmeDay) {
        if (this.getNumberOfUnallocatedPeriodsInDay(programmeDay) == 0) {
            return true;
        } else {
            return false;
        }
    }

    //public getNextPeriodAllocationAvailableForDay()
}
