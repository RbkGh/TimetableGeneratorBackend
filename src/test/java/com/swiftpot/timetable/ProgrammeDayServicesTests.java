package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
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

}
