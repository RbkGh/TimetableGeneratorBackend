package com.swiftpot.timetable.base;

/**
 * @author Ace Programmer Rbk
 * <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 * 27-Dec-16 @ 4:05 PM
 */

import com.swiftpot.timetable.model.ProgrammeDayPeriodsAllocation;

import java.util.List;

/**
 * use this to get the periods allocation for a specific day eg.
 * Form 1 Monday has 10 periods and breakdown is 3,3,2,2 or say 2,3,3,2
 */
public interface ProgrammeDaysPeriodsAllocator {
    List<ProgrammeDayPeriodsAllocation> getPeriodAllocationsForAllDays();
}
