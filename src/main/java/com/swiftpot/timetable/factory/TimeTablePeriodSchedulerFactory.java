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

    String timeTablePeriodSchedulerType;



    /**
     *
     * @return  TimeTablePeriodScheduler
     */
    public TimeTablePeriodScheduler getTimeTablePeriodScheduler(){
        if(timeTablePeriodSchedulerType.equals("FROM_FILE")) {
            return new TimeTablePeriodSchedulerFromFileImpl();
        }
        else return new TimeTablePeriodSchedulerFromFileImpl();
    }



    public String getTimeTablePeriodSchedulerType() {
        return timeTablePeriodSchedulerType;
    }

    public TimeTablePeriodSchedulerFactory setTimeTablePeriodSchedulerType(String timeTablePeriodSchedulerType) {
        this.timeTablePeriodSchedulerType = timeTablePeriodSchedulerType;
        return this;
    }
}
