/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.servicemodels.UnallocatedPeriodSet;
import com.swiftpot.timetable.util.PrettyJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         13-Mar-17 @ 3:04 PM
 */
public class TutorSubjectAndProgrammeGroupCombinationDocAllocatorTests {

    private static final Logger logger = LogManager.getLogger();

    TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl tutorSubjectAndProgrammeGroupCombinationDocAllocator;
    List<UnallocatedPeriodSet> unallocatedPeriodSetList;
    ProgrammeDay programmeDay;

    @Before
    public void setUpData() {
        tutorSubjectAndProgrammeGroupCombinationDocAllocator =
                new TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl();

        unallocatedPeriodSetList = new ArrayList<>();

        UnallocatedPeriodSet unallocatedPeriodSet1 = new UnallocatedPeriodSet();
        unallocatedPeriodSet1.setPeriodStartingNumber(1);
        unallocatedPeriodSet1.setPeriodEndingNumber(2);
        unallocatedPeriodSet1.setTotalNumberOfPeriodsForSet(2);

        UnallocatedPeriodSet unallocatedPeriodSet2 = new UnallocatedPeriodSet();
        unallocatedPeriodSet2.setPeriodStartingNumber(8);
        unallocatedPeriodSet2.setPeriodEndingNumber(10);
        unallocatedPeriodSet2.setTotalNumberOfPeriodsForSet(3);

        unallocatedPeriodSetList.addAll(new ArrayList<>(Arrays.asList(unallocatedPeriodSet1, unallocatedPeriodSet2)));

        List<PeriodOrLecture> periodOrLecturesForDay = new ArrayList<>();

        List<Integer> periodNumbersToSetToTrue = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture();
            periodOrLecture.setPeriodNumber(i);
            if (periodNumbersToSetToTrue.contains(i)) {
                periodOrLecture.setIsAllocated(true);
            } else {
                periodOrLecture.setIsAllocated(false);
            }
            periodOrLecturesForDay.add(periodOrLecture);
        }

        programmeDay = new ProgrammeDay();
        programmeDay.setPeriodList(periodOrLecturesForDay);

    }

    @Test
    public void testGetUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTable() {

        logger.info("ProgrammeDay Before anything ==>{}", PrettyJSON.toPrettyFormat(new Gson().toJson(programmeDay)));

        List<UnallocatedPeriodSet> finalUnallocatedPeriodSets =
                tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                        getUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTable
                                (unallocatedPeriodSetList, programmeDay);

        logger.info("finalUnallocatedPeriodSets==> {}", PrettyJSON.toListPrettyFormat(finalUnallocatedPeriodSets));

        assertThat(finalUnallocatedPeriodSets.size(), equalTo(1));
    }

    @Test
    public void testGetUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTableExpectEmpty() {

        List<PeriodOrLecture> periodOrLecturesForDay2 = new ArrayList<>();
        List<Integer> periodNumbersToSetToTrue = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 8));
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture();
            periodOrLecture.setPeriodNumber(i);
            if (periodNumbersToSetToTrue.contains(i)) {
                periodOrLecture.setIsAllocated(true);
            } else {
                periodOrLecture.setIsAllocated(false);
            }
            periodOrLecturesForDay2.add(periodOrLecture);
        }

        programmeDay.setPeriodList(periodOrLecturesForDay2);

        logger.info("ProgrammeDay Before anything ==>{}", PrettyJSON.toPrettyFormat(new Gson().toJson(programmeDay)));

        List<UnallocatedPeriodSet> finalUnallocatedPeriodSets =
                tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                        getUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTable
                                (unallocatedPeriodSetList, programmeDay);

        logger.info("finalUnallocatedPeriodSets==> expect empty list {}", PrettyJSON.toListPrettyFormat(finalUnallocatedPeriodSets));

        assertThat(finalUnallocatedPeriodSets.size(), equalTo(0));
    }


    @Test
    public void isSubjectAllocatedEqualToFourFiveOrSixTimesInProgrammeDay() {

        List<PeriodOrLecture> periodOrLecturesForDay3 = new ArrayList<>();
        List<Integer> periodNumbersToSetToTrue = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 8));
        String subjectDummy = "SubjectDUMMY";
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture();
            periodOrLecture.setPeriodNumber(i);
            if (periodNumbersToSetToTrue.contains(i)) {
                periodOrLecture.setIsAllocated(true);
                periodOrLecture.setSubjectUniqueIdInDb(subjectDummy);
            }
            periodOrLecturesForDay3.add(periodOrLecture);
        }

        programmeDay.setPeriodList(periodOrLecturesForDay3);

        logger.info("ProgrammeDay Before anything ==>{}", PrettyJSON.toPrettyFormat(new Gson().toJson(programmeDay)));

        boolean resultOfComputation = tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(subjectDummy, programmeDay);
        System.out.println("isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay ===>" + resultOfComputation);

        assertTrue(resultOfComputation);

        programmeDay.getPeriodList().get(9).setIsAllocated(true);
        programmeDay.getPeriodList().get(9).setSubjectUniqueIdInDb(subjectDummy);

        boolean resultOfComputation2 = tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(subjectDummy, programmeDay);
        System.out.println("isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay ===>when true is six times,expect true again " + resultOfComputation2);

        assertTrue(resultOfComputation2);

        periodOrLecturesForDay3 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture();
            periodOrLecture.setPeriodNumber(i);
            if (periodNumbersToSetToTrue.contains(i)) {
                periodOrLecture.setIsAllocated(false);
            }
            periodOrLecturesForDay3.add(periodOrLecture);
        }

        programmeDay.setPeriodList(periodOrLecturesForDay3);

        boolean resultOfComputation3 = tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(subjectDummy, programmeDay);
        System.out.println("isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay ===>when false all day,expect false here ==>" + resultOfComputation3);

        assertFalse(resultOfComputation3);

        List<Integer> periodNumbersToSetToTrue2 = new ArrayList<>(Arrays.asList(2, 3, 4));
        periodOrLecturesForDay3 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture();
            periodOrLecture.setPeriodNumber(i);
            if (periodNumbersToSetToTrue2.contains(i)) {
                periodOrLecture.setIsAllocated(true);
                periodOrLecture.setSubjectUniqueIdInDb(subjectDummy);
            }
            periodOrLecturesForDay3.add(periodOrLecture);
        }

        programmeDay.setPeriodList(periodOrLecturesForDay3);

        boolean resultOfComputation4 = tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(subjectDummy, programmeDay);
        System.out.println("isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay ===>when number of times subject is seen is less than 4,5 and 6 ==>" + resultOfComputation4);

        assertFalse(resultOfComputation4);

    }
}
