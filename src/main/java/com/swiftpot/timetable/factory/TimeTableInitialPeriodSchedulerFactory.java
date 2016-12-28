package com.swiftpot.timetable.factory;


import com.swiftpot.timetable.base.TimeTableInitialPeriodsScheduler;
import com.swiftpot.timetable.base.impl.TimeTableInitialPeriodsSchedulerFromFileImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 9:47 AM
 */
@Component
public class TimeTableInitialPeriodSchedulerFactory {

    @Autowired
    TimeTableInitialPeriodsSchedulerFromFileImpl timeTablePeriodSchedulerFromFile;

    String timeTablePeriodSchedulerType;

    /**
     * @return TimeTableInitialPeriodsScheduler
     */
    public TimeTableInitialPeriodsScheduler getTimeTablePeriodScheduler() {
        if (this.timeTablePeriodSchedulerType == "FROM_FILE") {
            return timeTablePeriodSchedulerFromFile;
        } else return timeTablePeriodSchedulerFromFile;
    }

    public TimeTableInitialPeriodSchedulerFactory setTimeTablePeriodSchedulerType(String timeTablePeriodSchedulerType) {
        this.timeTablePeriodSchedulerType = timeTablePeriodSchedulerType;
        return this;
    }
}
