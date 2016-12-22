package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator;
import com.swiftpot.timetable.model.ProgrammeDays;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 12:39 PM
 */
@Component
public class TimeTableDefaultPeriodsAllocatorDefaultImpl implements TimeTableDefaultPeriodsAllocator {
    @Override
    public TimeTableSuperDoc allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) {
        List<YearGroup> yearGroupList = timeTableSuperDocWithInitialDefaultDataSet.getYearGroupsList();
        int numberOfYearGroups = yearGroupList.size();
        for (int currentNo = 0; currentNo < numberOfYearGroups; currentNo++) {
            //now we need to get all ProgrammeGroups in yearGroupList
            List<ProgrammeGroup> programmeGroupList = yearGroupList.get(currentNo).getProgrammeGroupList();
            int numberOfProgrammeGroups = programmeGroupList.size();
            for (int currentProgrammeGroup = 0; currentProgrammeGroup < numberOfProgrammeGroups; currentProgrammeGroup++) {
                //now we need to get all the ProgrammeDays as list
                List<ProgrammeDays> programmeDaysList = programmeGroupList.get(currentProgrammeGroup).getProgrammeDaysList();
                int numberOfProgrammeDays = programmeDaysList.size();
                for (int currentProgrammeDay = 0; currentProgrammeDay < numberOfProgrammeDays; currentProgrammeDay++) {

                }
            }
        }

        return null;
    }
}
