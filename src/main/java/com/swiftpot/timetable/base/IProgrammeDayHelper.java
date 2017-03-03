package com.swiftpot.timetable.base;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 10:08 AM
 */
@Component
public interface IProgrammeDayHelper {

    /**
     * Check to see if all the {@link ProgrammeDay#periodList} is fully allocated ie <br>
     * each {@link com.swiftpot.timetable.model.PeriodOrLecture} is {@link com.swiftpot.timetable.model.PeriodOrLecture#isAllocated}
     *
     * @param programmeDay The {@link ProgrammeDay} object to check against.
     * @return true if all programmeDays are allocated entirely
     */
    boolean isProgrammeDayFullyAllocated(ProgrammeDay programmeDay);

    /**
     * Lets say we want to set a {@link com.swiftpot.timetable.repository.db.model.SubjectDoc}'s 3 periods to <br>
     * Monday periods,we use this to check to see if the available number of periods unallocated in that day <br>
     * is sufficient for the subject period.<br>
     * <b>WE USE THIS IF AND ONLY IF WE ARE SURE THAT THE NUMBER OF PERIODS UNALLOCATED IS SEQUENTIAL!!!</b>
     *
     * @param programmeDay             The {@link ProgrammeDay} object to check against.
     * @param numberOfPeriodsOfSubject the number of periods we want to allocate
     * @return
     */
    boolean isProgrammeDayCapableOfAcceptingTheIncomingNumberOfPeriodsAssumingUnallocatedDaysAreSequential(ProgrammeDay programmeDay, int numberOfPeriodsOfSubject);
}
