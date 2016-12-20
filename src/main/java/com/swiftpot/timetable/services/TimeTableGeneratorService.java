package com.swiftpot.timetable.services;

import com.swiftpot.timetable.factory.TimeTablePeriodSchedulerFactory;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 10:02 AM
 */
@Service
public class TimeTableGeneratorService {

    @Autowired
    TimeTablePeriodSchedulerFactory timeTablePeriodSchedulerFactory;


    public List<PeriodOrLecture> generateAllPeriodsOrLectureFirstTime(){
        return this.timeTablePeriodSchedulerFactory.setTimeTablePeriodSchedulerType("FROM_FILE").getTimeTablePeriodScheduler().generateAllPeriodsOrLecture();
    }

}
