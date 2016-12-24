package com.swiftpot.timetable.services;

import com.swiftpot.timetable.factory.TimeTableInitialPeriodSchedulerFactory;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 10:02 AM
 */
@Service
public class TimeTableGeneratorService {

    @Autowired
    TimeTableInitialPeriodSchedulerFactory timeTableInitialPeriodSchedulerFactory;
    @Autowired
    BusinessLogicConfigurationProperties businessLogicConfigurationProperties;


    public List<ProgrammeDay> generateAllProgrammeDaysFirstTime() throws Exception{
        List<ProgrammeDay> totalProgDays;
        System.out.println("Timetable total days =====" + businessLogicConfigurationProperties.TIMETABLE_DAY_TOTAL);
        int totalDays = Integer.parseInt(businessLogicConfigurationProperties.TIMETABLE_DAY_TOTAL);
        switch (totalDays) {
            case 1:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(1);
                break;
            case 2:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(2);
                break;
            case 3:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(3);
                break;
            case 4:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(4);
                break;
            case 5:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(5);
                break;
            case 6:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(6);
                break;
            case 7:
                totalProgDays = generateAllProgrammeDaysAccordingToTotalDays(7);
                break;
            default:
                throw new Exception("Only 7days available,any value not within 1-7 will fail");
        }
        return totalProgDays;
    }

    private List<ProgrammeDay> generateAllProgrammeDaysAccordingToTotalDays(int totalNumberOfDays) throws Exception {
        List<ProgrammeDay> totalProgrammeDays = new ArrayList<>();
        List<PeriodOrLecture> allPeriodsOrLecture = generateAllPeriodsOrLectureFirstTime();

        //we start from 1 because days are in range of 1 to 7,0 excluded
        for (int i = 1; i <= totalNumberOfDays; i++) {
            ProgrammeDay programmeDay = new ProgrammeDay();
            programmeDay.setPeriodList(allPeriodsOrLecture);
            switch (i) {
                case 1:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_1);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 2:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_2);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 3:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_3);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 4:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_4);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 5:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_5);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 6:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_6);
                    totalProgrammeDays.add(programmeDay);
                    break;
                case 7:
                    programmeDay.setDayName(businessLogicConfigurationProperties.TIMETABLE_DAY_7);
                    totalProgrammeDays.add(programmeDay);
                    break;
                default:
                    throw new Exception("Only 7days available,any value not within 1-7 will fail");
            }
        }
        return totalProgrammeDays;
    }

    private List<PeriodOrLecture> generateAllPeriodsOrLectureFirstTime() {
        return timeTableInitialPeriodSchedulerFactory.setTimeTablePeriodSchedulerType("FROM_FILE").getTimeTablePeriodScheduler().generateAllPeriodsOrLecture();
    }
}
