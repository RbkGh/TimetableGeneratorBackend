package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 12:39 PM
 */
@Component
public class TimeTableDefaultPeriodsAllocatorDefaultImpl implements TimeTableDefaultPeriodsAllocator {
    @Override
    public TimeTableSuperDoc allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) {
        return null;
    }
}
