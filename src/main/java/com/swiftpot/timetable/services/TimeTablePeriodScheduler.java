package com.swiftpot.timetable.services;

import com.swiftpot.timetable.base.TimeTablePeriodSchedule;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.repository.PeriodAndTimeAndSubjectAndTutorAssignedDocRepository;
import com.swiftpot.timetable.repository.db.model.PeriodAndTimeAndSubjectAndTutorAssignedDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 10:18 PM
 */
@Service
public class TimeTablePeriodScheduler implements TimeTablePeriodSchedule{

    @Autowired
    PeriodAndTimeAndSubjectAndTutorAssignedDocRepository ptstaDocRepository;

    @Override
    public List<PeriodOrLecture> getFullPeriodsAndTimesForDayPerEachProgrammeGroup() {
        return null;
    }
}
