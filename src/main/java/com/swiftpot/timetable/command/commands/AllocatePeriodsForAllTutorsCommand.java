/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * allocate all periods for each {@link com.swiftpot.timetable.repository.db.model.TutorDoc}s in db,this is the final
 * stage in the generation of the timetable .
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 9:47 AM
 * @see TimeTablePopulatorService#partOneGenerateInitialTimeTableSuperDocWithInitialData()
 */
@Component
public class AllocatePeriodsForAllTutorsCommand implements TimetableGenerationCommand {

    @Autowired
    private TimeTablePopulatorService timeTablePopulatorService;

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        return timeTablePopulatorService.partThreeAllocatePeriodsForAllTutors(timeTableSuperDoc);
    }

}
