/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command;

import com.swiftpot.timetable.command.commands.commandfactory.TimeTableGenerationCommandFactory;
import com.swiftpot.timetable.command.invokers.TimeTableGenerationInvoker;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
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
    private TimeTableGenerationInvoker timeTableGenerationInvoker;

    /**
     * generate the full timetable from here.
     *
     * @return
     * @throws Exception
     */
    public TimeTableSuperDoc generateTimeTable() throws Exception {


        //reset temporary entities in database
        timeTableGenerationInvoker = new TimeTableGenerationInvoker(TimeTableGenerationCommandFactory.RESET_TEMPORARY_DATABASE_ENTITIES);
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial temporary entities
        timeTableGenerationInvoker = new TimeTableGenerationInvoker(TimeTableGenerationCommandFactory.GENERATE_INITIAL_TEMPORARY_ENTITIES);
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial timetable with default data
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker
                        (TimeTableGenerationCommandFactory.GENERATE_INITIAL_TIMETABLESUPERDOC_WITH_DEFAULT_DATA);
        TimeTableSuperDoc timeTableSuperDocWithDefaultData =
                timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //allocate default periods for all programmeGroups in db
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker(TimeTableGenerationCommandFactory.ALLOCATE_DEFAULT_PERIODS_FOR_ALL_PROGRAMMEGROUPS);
        TimeTableSuperDoc timeTableSuperDocWithAllDefaultPeriodsSet =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation(timeTableSuperDocWithDefaultData);


        //allocate periods for all tutors
        timeTableGenerationInvoker =
                new TimeTableGenerationInvoker
                        (TimeTableGenerationCommandFactory.ALLOCATE_PERIODS_FOR_ALL_TUTORS);
        TimeTableSuperDoc finalTimeTableSuperDoc =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation
                                (timeTableSuperDocWithAllDefaultPeriodsSet);

        return finalTimeTableSuperDoc;
    }
}
