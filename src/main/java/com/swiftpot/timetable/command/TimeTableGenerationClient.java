/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command;

import com.swiftpot.timetable.command.commands.*;
import com.swiftpot.timetable.command.invokers.TimeTableGenerationInvoker;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 10:15 AM
 */
@Component
public class TimeTableGenerationClient {

    @Autowired
    TimeTableGenerationInvoker timeTableGenerationInvoker;

    /**
     * generate the full timetable from here.
     *
     * @return
     * @throws Exception
     */
    public TimeTableSuperDoc generateTimeTable() throws Exception {


        //reset temporary entities in database
        timeTableGenerationInvoker = new TimeTableGenerationInvoker(new ResetTemporaryDatabaseEntitiesCommand());
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial temporary entities
        timeTableGenerationInvoker = new TimeTableGenerationInvoker(new GenerateInitialTemporaryEntitiesCommand());
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial timetable with default data
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker
                        (new GenerateInitialTimeTableSuperDocWithDefaultDataCommand
                                (new TimeTablePopulatorService()));
        TimeTableSuperDoc timeTableSuperDocWithDefaultData =
                timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //allocate default periods for all programmeGroups in db
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker
                        (new AllocateDefaultPeriodsForAllProgrammeGroupsCommand
                                (new TimeTablePopulatorService()));
        TimeTableSuperDoc timeTableSuperDocWithAllDefaultPeriodsSet =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation(timeTableSuperDocWithDefaultData);


        //allocate periods for all tutors
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker
                        (new AllocatePeriodsForAllTutorsCommand
                                (new TimeTablePopulatorService()));
        TimeTableSuperDoc finalTimeTableSuperDoc =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation
                                (timeTableSuperDocWithAllDefaultPeriodsSet);

        return finalTimeTableSuperDoc;
    }
}
