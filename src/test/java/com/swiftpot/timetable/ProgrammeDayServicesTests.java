package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import com.swiftpot.timetable.services.servicemodels.AllocatedPeriodSet;
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
import java.util.List;

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
