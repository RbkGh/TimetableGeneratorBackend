package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 5:05 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimeTableProgrammeDayGeneratorTest {

    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;

    @Test
    public void testProgrammeDaysGenerationInitial() throws Exception {
        List<ProgrammeDay> programmeDaysList = timeTableGeneratorService.generateAllProgrammeDaysFirstTime();

        for (ProgrammeDay programmeDay : programmeDaysList) {
            System.out.println("*****************ONE PROGRAM DAY***************************\n\n\t\t");
            System.out.println("Program Day Name = \n \t\t\t------------------" + programmeDay.getDayName() + "-----------");
            for (PeriodOrLecture x : programmeDay.getPeriodList()) {
                System.out.println("periodstartandendtime = \t" + x.getPeriodStartandEndTime());
                System.out.println("periodName            = \t" + x.getPeriodName());
                System.out.println("\n\n============================================");
            }
            System.out.println("Program Day Name = \n \t\t\t------------------" + programmeDay.getDayName() + "-----------");
            System.out.println("*****************ONE PROGRAM DAY END***************************\n\n\t\t\t\t");
        }

        System.out.println("\n\n&&&&&&&& Day 3 Name,counting from 0            = " + programmeDaysList.get(3).getDayName());
        System.out.println("\n\n&&&&&&&& Day 3 Period 9 Number,counting from 0 = " + programmeDaysList.get(3).getPeriodList().get(9).getPeriodNumber());
        assertThat(programmeDaysList.get(3).getDayName().equals("THURSDAY") &&
                        (programmeDaysList.get(3).getPeriodList().get(9).getPeriodNumber() == 10),
                equalTo(true)
        );
    }
}
