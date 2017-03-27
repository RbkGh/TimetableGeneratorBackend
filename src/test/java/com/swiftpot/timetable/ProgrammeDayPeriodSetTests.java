/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.ProgrammeDayPeriodSetService;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.util.PrettyJSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         26-Mar-17 @ 3:56 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgrammeDayPeriodSetTests {

    private ProgrammeDay programmeDay = new ProgrammeDay("Monday");
    @Autowired
    ProgrammeDayPeriodSetService programmeDayPeriodSetService;

    @Before
    public void setUpMockAndInitialData() {
        //MockitoAnnotations.initMocks(this);

        List<PeriodOrLecture> periodOrLectureList = new ArrayList<>();
        List<Integer> periodNumbersToSetToTrue = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture("", i, "Period+" + i);
            if (periodNumbersToSetToTrue.contains(i)) {
                periodOrLecture.setIsAllocated(true);
                periodOrLecture.setSubjectUniqueIdInDb("1");
            } else {
                periodOrLecture.setIsAllocated(false);
                periodOrLecture.setSubjectUniqueIdInDb("1");
            }

            periodOrLectureList.add(periodOrLecture);
        }

        programmeDay.setPeriodList(periodOrLectureList);
    }

    @Test
    public void isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay() {

        PeriodSetForProgrammeDay periodSetForProgrammeDay1 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay1.setPeriodStartingNumber(1);
        periodSetForProgrammeDay1.setPeriodEndingNumber(2);
        periodSetForProgrammeDay1.setTotalNumberOfPeriodsForSet(2);

        PeriodSetForProgrammeDay periodSetForProgrammeDay2 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay2.setPeriodStartingNumber(3);
        periodSetForProgrammeDay2.setPeriodEndingNumber(5);
        periodSetForProgrammeDay2.setTotalNumberOfPeriodsForSet(3);

        PeriodSetForProgrammeDay periodSetForProgrammeDay3 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay3.setPeriodStartingNumber(6);
        periodSetForProgrammeDay3.setPeriodEndingNumber(7);
        periodSetForProgrammeDay3.setTotalNumberOfPeriodsForSet(2);

        PeriodSetForProgrammeDay periodSetForProgrammeDay4 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay4.setPeriodStartingNumber(8);
        periodSetForProgrammeDay4.setPeriodEndingNumber(10);
        periodSetForProgrammeDay4.setTotalNumberOfPeriodsForSet(3);

        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList =
                new ArrayList<>(
                        Arrays.asList(periodSetForProgrammeDay1,
                                periodSetForProgrammeDay2,
                                periodSetForProgrammeDay3,
                                periodSetForProgrammeDay4));

        System.out.println("ProgrammeDay ===>" + PrettyJSON.toPrettyFormat(new Gson().toJson(programmeDay)) + "\n\n\n PeriodSetListForProgrammeDay ==>" + PrettyJSON.toListPrettyFormat(periodSetForProgrammeDayList));
        boolean isThereAnyViolation = programmeDayPeriodSetService.
                isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay(programmeDay, periodSetForProgrammeDayList);

        assertTrue(isThereAnyViolation);

    }

    @Test
    public void getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb() throws PracticalSubjectForDayNotFoundException {
        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList =
                programmeDayPeriodSetService.getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb(programmeDay);
        System.out.println("PeriodSetForProgrammeDaysList ===>" + PrettyJSON.toListPrettyFormat(periodSetForProgrammeDayList));

        assertThat(periodSetForProgrammeDayList.get(0).getPeriodStartingNumber() == 1 && periodSetForProgrammeDayList.get(0).getPeriodEndingNumber() == 6
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(1).getPeriodStartingNumber() == 7 && periodSetForProgrammeDayList.get(1).getPeriodEndingNumber() == 8
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(2).getPeriodStartingNumber() == 9 && periodSetForProgrammeDayList.get(2).getPeriodEndingNumber() == 10
                , equalTo(true));
    }

    @Test(expected = PracticalSubjectForDayNotFoundException.class)
    public void getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDbExpectException() throws PracticalSubjectForDayNotFoundException {
        programmeDay.getPeriodList().get(0).setSubjectUniqueIdInDb(null);
        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList =
                programmeDayPeriodSetService.getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb(programmeDay);
        System.out.println("PeriodSetForProgrammeDaysList ===>" + PrettyJSON.toListPrettyFormat(periodSetForProgrammeDayList));

        assertThat(periodSetForProgrammeDayList.get(0).getPeriodStartingNumber() == 1 && periodSetForProgrammeDayList.get(0).getPeriodEndingNumber() == 6
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(1).getPeriodStartingNumber() == 7 && periodSetForProgrammeDayList.get(1).getPeriodEndingNumber() == 8
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(2).getPeriodStartingNumber() == 9 && periodSetForProgrammeDayList.get(2).getPeriodEndingNumber() == 10
                , equalTo(true));
    }

    @Test(expected = PracticalSubjectForDayNotFoundException.class)
    public void getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDbExpectException2() throws PracticalSubjectForDayNotFoundException {
        programmeDay.getPeriodList().get(4).setSubjectUniqueIdInDb(null);
        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList =
                programmeDayPeriodSetService.getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb(programmeDay);
        System.out.println("PeriodSetForProgrammeDaysList ===>" + PrettyJSON.toListPrettyFormat(periodSetForProgrammeDayList));

        assertThat(periodSetForProgrammeDayList.get(0).getPeriodStartingNumber() == 1 && periodSetForProgrammeDayList.get(0).getPeriodEndingNumber() == 6
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(1).getPeriodStartingNumber() == 7 && periodSetForProgrammeDayList.get(1).getPeriodEndingNumber() == 8
                , equalTo(true));
        assertThat(periodSetForProgrammeDayList.get(2).getPeriodStartingNumber() == 9 && periodSetForProgrammeDayList.get(2).getPeriodEndingNumber() == 10
                , equalTo(true));
    }
}
