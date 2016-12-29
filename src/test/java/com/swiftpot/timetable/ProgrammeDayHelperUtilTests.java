package com.swiftpot.timetable;

import com.swiftpot.timetable.base.IProgrammeDayHelper;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.util.ProgrammeDayHelperUtilDefaultImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 10:39 AM
 */

public class ProgrammeDayHelperUtilTests {

    @Test
    public void testIfProgrammeDayIsAllocatedBothFalseAndTrue() {
        IProgrammeDayHelper iProgrammeDayHelper = new ProgrammeDayHelperUtilDefaultImpl();
        ProgrammeDay programmeDay = new ProgrammeDay("Monday", new ArrayList<>(Arrays.asList(new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true))));

        boolean isProgrammeDayAllotedTrue = iProgrammeDayHelper.isProgrammeDayAlloted(programmeDay);
        System.out.println("********is programme day allocated = " + isProgrammeDayAllotedTrue+"*********");
        assertTrue(isProgrammeDayAllotedTrue);
        assertThat(true,equalTo(isProgrammeDayAllotedTrue));

        //now change one period or lecture to false and expect false
        programmeDay.getPeriodList().get(8).setIsAllocated(false);
        programmeDay.getPeriodList().get(4).setIsAllocated(false);
        boolean isProgrammeDayAllotedFalse = iProgrammeDayHelper.isProgrammeDayAlloted(programmeDay);
        System.out.println("********is programme day allocated = " + isProgrammeDayAllotedFalse+"*********");
        assertFalse(isProgrammeDayAllotedFalse);
        assertThat(false,equalTo(isProgrammeDayAllotedFalse));
    }
}
