package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.services.ProgrammeGroupDocCreator;
import com.swiftpot.timetable.services.SubjectsAssigner;
import com.swiftpot.timetable.base.impl.TimeTablePeriodSchedulerFromFileImpl;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimetableTechnicalApplicationTests {

    @Autowired
    BusinessLogicConfigurationProperties businessLogicConfigurationProperties;
    @Autowired
    SubjectsAssigner subjectsAssigner;
    @Autowired
    TimeTablePeriodSchedulerFromFileImpl timeTablePeriodSchedulerFromFile;
    @Autowired
    ProgrammeGroupDocCreator groupDocCreator;

    @Test
    public void contextLoads() {

    }


    /**
     * Test to ensure that property for period duration is set in properties file and only numbers allowed
     */
    @Test
    public void testReadingFromCustomFile() {
        String charSequence = "[0-9]+";
        String periodDuration = businessLogicConfigurationProperties.PERIOD_DURATION_IN_SECONDS;
        System.out.println("periodDuration = " + periodDuration);
        assertThat((periodDuration.matches(charSequence)) && (!periodDuration.isEmpty()),
                equalTo(true));

    }

    @Test
    public void testTotalSubjectAllocationGenerationVariables() {
        List<Integer> testSampleSpace = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        //start from i=1 as periods allocation does not have 1 involved
        for (int i = 1; i <= testSampleSpace.size(); i++) {
            List<Integer> totalSubjectAllocationList = subjectsAssigner.getTotalSubjectPeriodAllocationAsList(i);
            System.out.println("Total Subject Allocation for " + i + " periods = \n");
            for (Integer x : totalSubjectAllocationList) {
                System.out.println(x + "\n");
            }

            switch (i) {
                case 1:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{1}, totalSubjectAllocationList);
                    break;
                case 2:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{2}, totalSubjectAllocationList);
                    break;
                case 3:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3}, totalSubjectAllocationList);
                    break;
                case 4:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{2, 2}, totalSubjectAllocationList);
                    break;
                case 5:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 2}, totalSubjectAllocationList);
                    break;
                case 6:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 3}, totalSubjectAllocationList);
                    break;
                case 7:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 2, 2}, totalSubjectAllocationList);
                    break;
                case 8:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 3, 2}, totalSubjectAllocationList);
                    break;
                case 9:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 3, 3}, totalSubjectAllocationList);
                    break;
                case 10:
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3, 3, 2, 2}, totalSubjectAllocationList);
                    break;

                default:
                    System.out.println("Only 1-10 accepted");

            }

        }

    }


    @Test
    public void testTotalPeriodsandTime() {
        List<Integer> testSampleSpace = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        //start from i=1 ,since there is no 0 period in real world
        for (int i = 1; i <= testSampleSpace.size(); i++) {
            List<PeriodOrLecture> periodOrLectures = timeTablePeriodSchedulerFromFile.generateAllPeriodsOrLecture();
            System.out.println(" period " + i + " \n");
            for (PeriodOrLecture x : periodOrLectures) {
                System.out.println("Period Name = " + x.getPeriodName() + " Period Duration = " + x.getPeriodStartandEndTime());
            }

            switch (i) {
                case 1:
                    System.out.println("case 1 object = " + periodOrLectures.get(1 - 1).toString());
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("07:30-08:10AM", 1, "PERIOD 1"), periodOrLectures.get(1 - 1));
                    break;
                case 2:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("08:10-08:50AM", 2, "PERIOD 2"), periodOrLectures.get(2 - 1));
                    break;
                case 3:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("09:20-10:00AM", 3, "PERIOD 3"), periodOrLectures.get(3 - 1));
                    break;
                case 4:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("10:00-10:40AM", 4, "PERIOD 4"), periodOrLectures.get(4 - 1));
                    break;
                case 5:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("10:40-11:20AM", 5, "PERIOD 5"), periodOrLectures.get(5 - 1));
                    break;
                case 6:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("11:20-12:00PM", 6, "PERIOD 6"), periodOrLectures.get(6 - 1));
                    break;
                case 7:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("12:00-12:40PM", 7, "PERIOD 7"), periodOrLectures.get(7 - 1));
                    break;
                case 8:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("01:00-01:40PM", 8, "PERIOD 8"), periodOrLectures.get(8 - 1));
                    break;
                case 9:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("01:40-02:20PM", 9, "PERIOD 9"), periodOrLectures.get(9 - 1));
                    break;
                case 10:
                    assertPeriodStartEndTimeIsCorrect(preparePeriodObjectExpected("02:20-03:00PM", 10, "PERIOD 10"), periodOrLectures.get(10 - 1));
                    break;

                default:
                    System.out.println("Only 1-10 accepted");

            }

        }

    }

    @Test
    public void testAlphabetIncrementer() {
        char c = 'c';

        c = (char) (((c - 'a' + 1) % 26) + 'a');

        System.out.println(c);
        assertThat("D", equalTo(String.valueOf(c).toUpperCase()));

    }


    private PeriodOrLecture preparePeriodObjectExpected(String periodStartandEndTime, int periodNumber, String periodName) {
        PeriodOrLecture periodOrLecture = new PeriodOrLecture(periodStartandEndTime, periodNumber, periodName);
        periodOrLecture.setIsAllocated(false);
        return periodOrLecture;
    }

    private void assertPeriodStartEndTimeIsCorrect(PeriodOrLecture periodOrLectureExpected, PeriodOrLecture periodOrLecturegenerated) {
        assert (periodOrLectureExpected.getPeriodStartandEndTime().equals(periodOrLecturegenerated.getPeriodStartandEndTime()));
    }

    private void assertTotalSubjectAllocationGenerationVariables(Integer[] val, List<Integer> totalSubjectAllocationList) {
        assertThat(new ArrayList<>(Arrays.asList(val)), equalTo(totalSubjectAllocationList));
    }

}
