package com.swiftpot.timetable;

import com.swiftpot.timetable.base.IProgrammeDayHelper;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.util.PrettyJSON;
import com.swiftpot.timetable.util.ProgrammeDayHelperUtilDefaultImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

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

    private static final Logger logger = LogManager.getLogger();
    IProgrammeDayHelper iProgrammeDayHelper;
    ProgrammeDay programmeDay;

    @Before
    public void setup() {
        iProgrammeDayHelper = new ProgrammeDayHelperUtilDefaultImpl();
        programmeDay = new ProgrammeDay("Monday", new ArrayList<>(Arrays.asList(new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true),
                new PeriodOrLecture(true))));
    }

    @Test
    public void testIfProgrammeDayIsAllocatedBothFalseAndTrue() {


        boolean isProgrammeDayAllotedTrue = iProgrammeDayHelper.isProgrammeDayFullyAllocated(programmeDay);
        System.out.println("********is programme day allocated = " + isProgrammeDayAllotedTrue+"*********");
        assertTrue(isProgrammeDayAllotedTrue);
        assertThat(true,equalTo(isProgrammeDayAllotedTrue));

        //now change one period or lecture to false and expect false
        programmeDay.getPeriodList().get(8).setIsAllocated(false);
        programmeDay.getPeriodList().get(4).setIsAllocated(false);
        boolean isProgrammeDayAllotedFalse = iProgrammeDayHelper.isProgrammeDayFullyAllocated(programmeDay);
        System.out.println("********is programme day allocated = " + isProgrammeDayAllotedFalse+"*********");
        assertFalse(isProgrammeDayAllotedFalse);
        assertThat(false,equalTo(isProgrammeDayAllotedFalse));
    }

    @Test
    public void testIfProgrammeDayIsCapableOfHandlingIncomingNumberOfPeriodsExpectFalse() {
        boolean isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods =
                iProgrammeDayHelper.isProgrammeDayCapableOfAcceptingTheIncomingNumberOfPeriodsAssumingUnallocatedDaysAreSequential(programmeDay, 4);
        System.out.println("isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods======" + isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods);
        assertFalse(isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods);
    }

    @Test(expected = AssertionError.class)
    public void testIfProgrammeDayIsCapableOfHandlingIncomingNumberOfPeriodsExpectAssertionError() {
        programmeDay.getPeriodList().get(8).setIsAllocated(false);
        programmeDay.getPeriodList().get(9).setIsAllocated(false);
        boolean isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods =
                iProgrammeDayHelper.isProgrammeDayCapableOfAcceptingTheIncomingNumberOfPeriodsAssumingUnallocatedDaysAreSequential(programmeDay, 2);
        assertFalse(isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods);
    }

    @Test
    public void testIfProgrammeDayIsCapableOfHandlingIncomingNumberOfPeriodsExpectTrue() {
        programmeDay.getPeriodList().get(8).setIsAllocated(false);
        programmeDay.getPeriodList().get(9).setIsAllocated(false);
        logger.info("Periods In Day ==> {}", PrettyJSON.toListPrettyFormat(programmeDay.getPeriodList()));
        boolean isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods =
                iProgrammeDayHelper.isProgrammeDayCapableOfAcceptingTheIncomingNumberOfPeriodsAssumingUnallocatedDaysAreSequential(programmeDay, 2);
        System.out.println("Expect true -->isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods======" + isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods);
        assertTrue(isProgrammeDayCapableOfHandlingIncomingNumberOfPeriods);
    }
}
