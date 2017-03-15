/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * allocate default periods for all programmeGroups .call {@link com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator} here.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 8:13 AM
 * @see com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator
 */
@Component
public class AllocateDefaultPeriodsForAllProgrammeGroupsCommand implements TimetableGenerationCommand {

    @Autowired
    private TimeTablePopulatorService timeTablePopulatorService;

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        return timeTablePopulatorService.
                partTwoAllocateDefaultPeriods
                        (TimeTableDefaultPeriodsAllocatorFactory.DEFAULT_IMPLEMENTATION, timeTableSuperDoc);
    }

}
