/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.sun.istack.Nullable;
import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * generate 2 periods only ie {2,2,2,2,2} for programmeGroups that have 8periods as one of their subjects period allocation.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         13-Apr-17 @ 7:00 PM
 */
public class GenerateTwoPeriodsForFormThreeClassesHavingEightPeriodsAsSubjectPeriodsCommand implements TimetableGenerationCommand {

    @Autowired
    private TimeTablePopulatorService timeTablePopulatorService;

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(@Nullable TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        timeTablePopulatorService.partThreeGenerateTwoPeriodsForAtLeastOneDayInWeekForProgrammesThatHaveEightPeriodsSubjectAllocation();
        return timeTableSuperDoc;
    }
}
