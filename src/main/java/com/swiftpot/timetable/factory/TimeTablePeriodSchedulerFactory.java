package com.swiftpot.timetable.factory;


import com.swiftpot.timetable.base.TimeTablePeriodScheduler;
import com.swiftpot.timetable.base.impl.TimeTablePeriodSchedulerFromFileImpl;
import com.swiftpot.timetable.util.TimeTablePeriodSchedulerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 9:47 AM
 */
@Component
public class TimeTablePeriodSchedulerFactory {

    @Autowired
    TimeTablePeriodSchedulerFromFileImpl timeTablePeriodSchedulerFromFile;

    String timeTablePeriodSchedulerType;

    /**
     *
     * @return  TimeTablePeriodScheduler
     */
    public TimeTablePeriodScheduler getTimeTablePeriodScheduler(){
        if(this.timeTablePeriodSchedulerType =="FROM_FILE") {
            return timeTablePeriodSchedulerFromFile;
        }
        else return timeTablePeriodSchedulerFromFile;
    }



    public String getTimeTablePeriodSchedulerType() {
        return timeTablePeriodSchedulerType;
    }

    public TimeTablePeriodSchedulerFactory setTimeTablePeriodSchedulerType(String timeTablePeriodSchedulerType) {
        this.timeTablePeriodSchedulerType = timeTablePeriodSchedulerType;
        return this;
    }
}
