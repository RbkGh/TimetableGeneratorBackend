/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import org.springframework.stereotype.Component;

/**
 * Generate initial default data onto {@link TimeTableSuperDoc}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 8:49 AM
 * @see TimeTablePopulatorService#partOneGenerateInitialTimeTableSuperDocWithInitialData()
 */
@Component
public class GenerateInitialTimeTableSuperDocWithDefaultDataCommand implements TimetableGenerationCommand {

    private TimeTablePopulatorService timeTablePopulatorService;

    public GenerateInitialTimeTableSuperDocWithDefaultDataCommand(TimeTablePopulatorService timeTablePopulatorService) {
        this.timeTablePopulatorService = timeTablePopulatorService;
    }

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        return timeTablePopulatorService.partOneGenerateInitialTimeTableSuperDocWithInitialData();
    }
}
