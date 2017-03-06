package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @return the number of {@link PeriodOrLecture} with {@link PeriodOrLecture#isAllocated} == false,returns 0 if none is found
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
     * get the first index of the location where getIsAllocated is equal to false
     *
     * @param periodOrLecturesList {@link List<PeriodOrLecture>}
     * @return int
     */
    public int getFirstIndexPositionoFPeriodWhereAllocationIsFalseInListOfPeriods(List<PeriodOrLecture> periodOrLecturesList) {
        int totalPeriodsToIterateThrough = periodOrLecturesList.size();
        List<Integer> listOfIndexesWhereFalseWasSeen = new ArrayList<>();
        for (int i = 0; i < totalPeriodsToIterateThrough; i++) {
            if (periodOrLecturesList.get(i).getIsAllocated() == false) {
                listOfIndexesWhereFalseWasSeen.add(i);
            }
        }
        //first will be first element in list
        int indexWhereFalseWasFirstSeen = listOfIndexesWhereFalseWasSeen.get(0);
        return indexWhereFalseWasFirstSeen;
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

    public List<UnallocatedPeriodSet> getListOfUnallocatedPeriodSetsInDay(ProgrammeDay programmeDay) {
        List<UnallocatedPeriodSet> finalUnallocatedPeriodSetsList = new ArrayList<>();
        if (this.isProgrammeDayFullyAllocated(programmeDay)) {
            //nothing to check for ,thus return 0
        } else {

        }
        return finalUnallocatedPeriodSetsList;
    }

    protected List<UnallocatedPeriodSet> getListOfUnallocatedPeriodSetsInDayAfterCheckingProgrammeDayIsNotFullyAllocated(ProgrammeDay programmeDay) {
        List<UnallocatedPeriodSet> finalUnallocatedPeriodSetsList = new ArrayList<>();

        return null;
    }

    /**
     * this will return a period set that is unallocated,
     * for eg. period 3&4 may be unallocated whilst all other periods are allocated in a day.
     */
    public class UnallocatedPeriodSet {
        /**
         * the period where the unallocated period starts ie {@link PeriodOrLecture#periodNumber},not the index of the period!!Nnote that!!
         * the period may start in periodNumber 3 and end in period number 4,meaning that {@link UnallocatedPeriodSet#totalNumberOfPeriodsForSet} will be 2
         */
        private int periodStartingNumber;
        /**
         * the period where the unallocated period ends ie {@link PeriodOrLecture#periodNumber},not the index of the period!!Nnote that!!
         * the period may end in periodNumber 5 if it started in period 3.
         */
        private int periodEndingNumber;
        /**
         * the total number of periods for this set,if the {@linkplain UnallocatedPeriodSet#periodStartingNumber} = 3 and <br>
         * {@linkplain UnallocatedPeriodSet#periodEndingNumber} =4,automatically,{@linkplain UnallocatedPeriodSet#totalNumberOfPeriodsForSet} =2periods
         *
         * @see UnallocatedPeriodSet#periodStartingNumber
         */
        private int totalNumberOfPeriodsForSet;

        public int getPeriodStartingNumber() {
            return periodStartingNumber;
        }

        /**
         * @return int
         * @see UnallocatedPeriodSet#periodStartingNumber
         */
        public void setPeriodStartingNumber(int periodStartingNumber) {
            this.periodStartingNumber = periodStartingNumber;
        }

        /**
         * @return int
         * @see UnallocatedPeriodSet#periodEndingNumber
         */
        public int getPeriodEndingNumber() {
            return periodEndingNumber;
        }

        public void setPeriodEndingNumber(int periodEndingNumber) {
            this.periodEndingNumber = periodEndingNumber;
        }

        /**
         * @return int
         * @see UnallocatedPeriodSet#totalNumberOfPeriodsForSet
         */
        public int getTotalNumberOfPeriodsForSet() {
            return totalNumberOfPeriodsForSet;
        }

        public void setTotalNumberOfPeriodsForSet(int totalNumberOfPeriodsForSet) {
            this.totalNumberOfPeriodsForSet = totalNumberOfPeriodsForSet;
        }
    }
}
