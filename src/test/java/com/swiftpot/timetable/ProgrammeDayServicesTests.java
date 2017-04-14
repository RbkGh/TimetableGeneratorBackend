/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import com.swiftpot.timetable.services.servicemodels.AllocatedPeriodSet;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.services.servicemodels.UnallocatedPeriodSet;
import com.swiftpot.timetable.util.PrettyJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Tests of {@link com.swiftpot.timetable.services.ProgrammeDayServices} methods
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         05-Mar-17 @ 2:50 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgrammeDayServicesTests {

    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;
    @Autowired
    ProgrammeDayServices programmeDayServices;

    List<ProgrammeDay> programmeDaysList;
    private static final Logger logger = LogManager.getLogger();

    @Before
    public void setupData() throws Exception {
        programmeDaysList = new ArrayList<>();
        programmeDaysList = timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
    }

    @Test
    public void testIfProgrammeDayIsAllocatedExpectTrue() {
        List<PeriodOrLecture> periodOrLectureList = programmeDaysList.get(3).getPeriodList();
        periodOrLectureList.forEach(periodOrLecture -> periodOrLecture.setIsAllocated(true));
        boolean isProgrammeDayFullyAllocated =
                programmeDayServices.isProgrammeDayFullyAllocated(programmeDaysList.get(3));
        assertThat(isProgrammeDayFullyAllocated, equalTo(true));
    }

    @Test
    public void testIfProgrammeDayIsFullyUnAllocatedExpectTrueAndFalse() {
        programmeDaysList.get(3).getPeriodList().forEach(periodOrLecture -> periodOrLecture.setIsAllocated(false));
        boolean isProgrammeDayFullyUnallocated =
                programmeDayServices.isProgrammeDayFullyUnAllocated(programmeDaysList.get(3));
        System.out.println("ProgrammeDay -->" + PrettyJSON.toListPrettyFormat(programmeDaysList.get(3).getPeriodList()) +
                "\n isProgrammeDayFullyUnallocated : expect true ,answer = "
                + isProgrammeDayFullyUnallocated);
        assertThat(isProgrammeDayFullyUnallocated, equalTo(true));


        programmeDaysList.get(3).getPeriodList().forEach(periodOrLecture -> {
            if (Objects.equals(periodOrLecture.getPeriodNumber(), 1))
                periodOrLecture.setIsAllocated(true);
        });
        System.out.println("ProgrammeDay -->" + PrettyJSON.toListPrettyFormat(programmeDaysList.get(3).getPeriodList()) +
                "\n isProgrammeDayFullyUnallocated : expect false ,answer = "
                + programmeDayServices.isProgrammeDayFullyUnAllocated(programmeDaysList.get(3)));
        assertThat(programmeDayServices.isProgrammeDayFullyUnAllocated(programmeDaysList.get(3)), equalTo(false));
    }

    @Test
    public void testIfProgrammeDayIsAllocatedExpectFalse() {
        boolean isProgrammeDayFullyAllocated =
                programmeDayServices.isProgrammeDayFullyAllocated(programmeDaysList.get(3));
        assertThat(isProgrammeDayFullyAllocated, equalTo(false));
    }

    @Test
    public void testGetPostUnallocatedPeriodSetListInAllocatedPeriodSet() {
        int periodStartingNumber = 2;
        int periodEndingNumber = 4;
        int totalNumberOfPeriodsForSet = 3;

        String subjectUniqueIdInDb = "SUBJ1";
        String tutorUniqueIdInDb = "TUTJ1";
        ProgrammeDay programmeDay =
                this.setUpProgrammeDay(programmeDaysList.get(0), subjectUniqueIdInDb, tutorUniqueIdInDb, periodStartingNumber, periodEndingNumber);


        logger.debug("ProgrammeDay Periods before everything =>  {}", PrettyJSON.toListPrettyFormat(programmeDay.getPeriodList()));
        AllocatedPeriodSet allocatedPeriodSet = new AllocatedPeriodSet();
        allocatedPeriodSet.setPeriodStartingNumber(periodStartingNumber);
        allocatedPeriodSet.setPeriodEndingNumber(periodEndingNumber);
        allocatedPeriodSet.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForSet);

        List<UnallocatedPeriodSet> unallocatedPeriodSetList = programmeDayServices.getPostUnallocatedPeriodSetListInAllocatedPeriodSet(allocatedPeriodSet, programmeDay);

        logger.debug("Post UnallocatedPeriodList =>{}", PrettyJSON.toListPrettyFormat(unallocatedPeriodSetList));
        //assertThat(((unallocatedPeriodSetList.size()==1) && (unallocatedPeriodSetList.get(0).getPeriodStartingNumber()==5)),equalTo(true));
        assertThat((unallocatedPeriodSetList.size() == 1), equalTo(true));
    }

    @Test
    public void testGetTotalUnallocatedPeriodSetListInAllocatedPeriodSetExpectTrue() {
        int periodStartingNumber = 2;
        int periodEndingNumber = 4;
        int totalNumberOfPeriodsForSet = 3;

        String subjectUniqueIdInDb = "SUBJ1";
        String tutorUniqueIdInDb = "TUTJ1";
        ProgrammeDay programmeDay =
                this.setUpProgrammeDay(programmeDaysList.get(0), subjectUniqueIdInDb, tutorUniqueIdInDb, periodStartingNumber, periodEndingNumber);


        int periodStartingNumber2 = 7;
        int periodEndingNumber2 = 8;
        String subjectUniqueIdInDb2 = "SUBJ2";
        String tutorUniqueIdInDb2 = "TUTJ2";
        programmeDay =
                this.setUpProgrammeDay(programmeDay, subjectUniqueIdInDb2, tutorUniqueIdInDb2, periodStartingNumber2, periodEndingNumber2);

        logger.debug("ProgrammeDay Periods before everything =>  {}", PrettyJSON.toListPrettyFormat(programmeDay.getPeriodList()));
        AllocatedPeriodSet allocatedPeriodSet = new AllocatedPeriodSet();
        allocatedPeriodSet.setPeriodStartingNumber(periodStartingNumber);
        allocatedPeriodSet.setPeriodEndingNumber(periodEndingNumber);
        allocatedPeriodSet.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForSet);

        List<UnallocatedPeriodSet> unallocatedPeriodSetList =
                programmeDayServices.getListOfUnallocatedPeriodSetsInDay(programmeDay);

        logger.debug("Post UnallocatedPeriodList =>{}", PrettyJSON.toListPrettyFormat(unallocatedPeriodSetList));
        //assertThat(((unallocatedPeriodSetList.size()==1) && (unallocatedPeriodSetList.get(0).getPeriodStartingNumber()==5)),equalTo(true));
        assertThat((unallocatedPeriodSetList.size() == 3), equalTo(true));
    }

    @Test
    public void testGetTotalUnallocatedPeriodSetListInAllocatedPeriodSetNewExpectation() {
        int periodStartingNumber = 2;
        int periodEndingNumber = 4;
        int totalNumberOfPeriodsForSet = 3;

        String subjectUniqueIdInDb = "SUBJ1";
        String tutorUniqueIdInDb = "TUTJ1";
        ProgrammeDay programmeDay =
                this.setUpProgrammeDay(programmeDaysList.get(0), subjectUniqueIdInDb, tutorUniqueIdInDb, periodStartingNumber, periodEndingNumber);


        int periodStartingNumber2 = 7;
        int periodEndingNumber2 = 8;
        String subjectUniqueIdInDb2 = "SUBJ2";
        String tutorUniqueIdInDb2 = "TUTJ2";
        programmeDay =
                this.setUpProgrammeDay(programmeDay, subjectUniqueIdInDb2, tutorUniqueIdInDb2, periodStartingNumber2, periodEndingNumber2);

        int periodStartingNumber3 = 10;
        int periodEndingNumber3 = 10;
        String subjectUniqueIdInDb3 = "SUBJ3";
        String tutorUniqueIdInDb3 = "TUTJ3";
        programmeDay =
                this.setUpProgrammeDay(programmeDay, subjectUniqueIdInDb3, tutorUniqueIdInDb3, periodStartingNumber3, periodEndingNumber3);
        logger.debug("ProgrammeDay Periods before everything =>  {}", PrettyJSON.toListPrettyFormat(programmeDay.getPeriodList()));
        AllocatedPeriodSet allocatedPeriodSet = new AllocatedPeriodSet();
        allocatedPeriodSet.setPeriodStartingNumber(periodStartingNumber);
        allocatedPeriodSet.setPeriodEndingNumber(periodEndingNumber);
        allocatedPeriodSet.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForSet);

        List<UnallocatedPeriodSet> unallocatedPeriodSetList =
                programmeDayServices.getListOfUnallocatedPeriodSetsInDay(programmeDay);

        logger.debug("Post UnallocatedPeriodList =>{}", PrettyJSON.toListPrettyFormat(unallocatedPeriodSetList));
        //assertThat(((unallocatedPeriodSetList.size()==1) && (unallocatedPeriodSetList.get(0).getPeriodStartingNumber()==5)),equalTo(true));
        assertThat((unallocatedPeriodSetList.size() == 3), equalTo(true));
    }

    @Test
    public void testGetUnallocatedPeriodsFromProgrammeDaySetList() {
        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList = new ArrayList<>();

        PeriodSetForProgrammeDay periodSetForProgrammeDay1 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay1.setPeriodStartingNumber(1);
        periodSetForProgrammeDay1.setPeriodEndingNumber(3);
        periodSetForProgrammeDay1.setTotalNumberOfPeriodsForSet(3);

        PeriodSetForProgrammeDay periodSetForProgrammeDay2 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay2.setPeriodStartingNumber(4);
        periodSetForProgrammeDay2.setPeriodEndingNumber(5);
        periodSetForProgrammeDay2.setTotalNumberOfPeriodsForSet(2);

        PeriodSetForProgrammeDay periodSetForProgrammeDay3 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay3.setPeriodStartingNumber(6);
        periodSetForProgrammeDay3.setPeriodEndingNumber(8);
        periodSetForProgrammeDay3.setTotalNumberOfPeriodsForSet(3);

        PeriodSetForProgrammeDay periodSetForProgrammeDay4 = new PeriodSetForProgrammeDay();
        periodSetForProgrammeDay4.setPeriodStartingNumber(9);
        periodSetForProgrammeDay4.setPeriodEndingNumber(10);
        periodSetForProgrammeDay4.setTotalNumberOfPeriodsForSet(2);

        periodSetForProgrammeDayList.addAll(new ArrayList<>(Arrays.asList(periodSetForProgrammeDay1,
                periodSetForProgrammeDay2,
                periodSetForProgrammeDay3,
                periodSetForProgrammeDay4)));

        List<Integer> periodNumbersToMarkAsTrue = new ArrayList<>(Arrays.asList(1, 2, 3, 9, 10));
        List<PeriodOrLecture> periodOrLectureList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            PeriodOrLecture periodOrLecture = new PeriodOrLecture("Whatever,Not needed In this context", i, "Period" + i);

            if (periodNumbersToMarkAsTrue.contains(i)) {
                periodOrLecture.setIsAllocated(true);
            } else {
                periodOrLecture.setIsAllocated(false);
            }
            periodOrLectureList.add(periodOrLecture);
        }

        ProgrammeDay programmeDay = new ProgrammeDay("Whatever,Not Really Needed In this Test!", periodOrLectureList);

        List<UnallocatedPeriodSet> unallocatedPeriodSetList =
                programmeDayServices.getUnallocatedPeriodSetFromPeriodSetForProgDay(periodSetForProgrammeDayList, programmeDay);
        logger.debug("UnallocatedPeriodSet==>>>\n\t\t {}", PrettyJSON.toListPrettyFormat(unallocatedPeriodSetList));

        UnallocatedPeriodSet unallocatedPeriodSet = new UnallocatedPeriodSet();
        unallocatedPeriodSet.setPeriodStartingNumber(4);
        unallocatedPeriodSet.setPeriodEndingNumber(5);
        unallocatedPeriodSet.setTotalNumberOfPeriodsForSet(2);

        UnallocatedPeriodSet unallocatedPeriodSet2 = new UnallocatedPeriodSet();
        unallocatedPeriodSet2.setPeriodStartingNumber(6);
        unallocatedPeriodSet2.setPeriodEndingNumber(8);
        unallocatedPeriodSet2.setTotalNumberOfPeriodsForSet(3);

        assertThat((unallocatedPeriodSetList.size() == 2) &&
                        (unallocatedPeriodSetList.containsAll(new ArrayList<>(Arrays.asList(unallocatedPeriodSet, unallocatedPeriodSet2)))),
                equalTo(true));

        programmeDay.getPeriodList().forEach(periodOrLecture -> periodOrLecture.setIsAllocated(true));

        logger.info("ProgrammeDay With all periods set to true ==>{}", PrettyJSON.toListPrettyFormat(programmeDay.getPeriodList()));

        List<UnallocatedPeriodSet> unallocatedPeriodSetList2 =
                programmeDayServices.getUnallocatedPeriodSetFromPeriodSetForProgDay(periodSetForProgrammeDayList, programmeDay);
        logger.info("UnallocatedPeriodList2 when all periods are true,expect empty ==>{}", PrettyJSON.toListPrettyFormat(unallocatedPeriodSetList2));

        assertThat(unallocatedPeriodSetList2.isEmpty(), equalTo(true));
    }

    private ProgrammeDay setUpProgrammeDay(ProgrammeDay programmeDay, String subjectUniqueIdInDb, String tutorUniqueIdInDb, int periodStartingNumber, int periodEndingNumber) {
        for (PeriodOrLecture periodOrLecture : programmeDay.getPeriodList()) {
            int currentPeriodOrLectureNumber = periodOrLecture.getPeriodNumber();
            if ((currentPeriodOrLectureNumber >= periodStartingNumber) && (currentPeriodOrLectureNumber <= periodEndingNumber)) {
                periodOrLecture.setIsAllocated(true);
                periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb);
                periodOrLecture.setTutorUniqueId(tutorUniqueIdInDb);
            }
        }
        return programmeDay;
    }

}
