package com.swiftpot.timetable.services;

import com.swiftpot.timetable.base.TimeTablePeriodSchedule;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.repository.PeriodAndTimeAndSubjectAndTutorAssignedDocRepository;
import com.swiftpot.timetable.repository.db.model.PeriodAndTimeAndSubjectAndTutorAssignedDoc;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 10:18 PM
 */
@Service
public class TimeTablePeriodSchedulerFromFileImpl implements TimeTablePeriodSchedule{

    @Autowired
    PeriodAndTimeAndSubjectAndTutorAssignedDocRepository ptstaDocRepository;
    @Autowired
    BusinessLogicConfigurationProperties businessLogicConfigurationProperties;

    @Override
    public List<PeriodOrLecture> getFullPeriodsAndTimesForDayPerEachProgrammeGroup() {
        int totalTimeTablePeriods = Integer.valueOf(businessLogicConfigurationProperties.TIMETABLE_PERIOD_TOTAL);
        List<PeriodOrLecture> periodOrLectures = new ArrayList<>();
        //start from 1 instead of 0,as timetable periods is counted from 1 in the real world
        for (int i = 1; i <= totalTimeTablePeriods; i++) {
            switch (i) {
                case 1 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_1,1,"PERIOD 1"));
                    break;
                case 2 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_2,2,"PERIOD 2"));
                    break;
                case 3 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_3,3,"PERIOD 3"));
                    break;
                case 4 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_4,4,"PERIOD 4"));
                    break;
                case 5 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_5,5,"PERIOD 5"));
                    break;
                case 6 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_6,6,"PERIOD 6"));
                    break;
                case 7 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_7,7,"PERIOD 7"));
                    break;
                case 8 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_8,8,"PERIOD 8"));
                    break;
                case 9 :
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_9,9,"PERIOD 9"));
                    break;
                case 10:
                    periodOrLectures.add(preparePeriodObject(businessLogicConfigurationProperties.TIMETABLE_PERIO_10,10,"PERIOD 10"));
                    break;
                default:
                    throw new AssertionError("Only values 1-10 required");

            }
        }
        return periodOrLectures;
    }

    private PeriodOrLecture preparePeriodObject(String periodStartandEndTime,int periodNumber,String periodName){
        PeriodOrLecture periodOrLecture = new PeriodOrLecture(periodStartandEndTime,periodNumber,periodName);
        periodOrLecture.setIsAllocated(false);
        return periodOrLecture;
    }
}
