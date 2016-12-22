package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 12:35 PM
 */
public interface TimeTableDefaultPeriodsAllocator {
    /**
     *
     * @param timeTableSuperDocWithInitialDefaultDataSet {@link TimeTableSuperDoc}
     * @return {@link TimeTableSuperDoc }
     * <p>Returns a {@link TimeTableSuperDoc } with the default Periods eg 10periods workshop days and worship and class meetings set to returning object
     * for each programmeGroup</p>
     */
    TimeTableSuperDoc allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet);
}
