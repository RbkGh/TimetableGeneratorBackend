package com.swiftpot.timetable.base;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 10:23 PM
 */
public interface TimeTablePeriodScheduler {
    List<PeriodOrLecture> generateAllPeriodsOrLecture();
}
