package com.swiftpot.timetable;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDays;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 5:05 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimeTableProgrammeDaysGeneratorTest {

    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;

    @Test
    public void testProgrammeDaysGenerationInitial() throws Exception {
        List<ProgrammeDays> programmeDaysList =  timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
        for (ProgrammeDays programmeDays : programmeDaysList) {
            System.out.println("Program Day Name = \n "+programmeDays.getDayName());
            for (PeriodOrLecture x : programmeDays.getPeriodList()) {
                System.out.println("Periods = periodstartandendtime= \n"+x.getPeriodStartandEndTime());
                System.out.println("periodName = \n"+x.getPeriodName());
                System.out.println("\n\n============================================");
            }
        }
    }
}
