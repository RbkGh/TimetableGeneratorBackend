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
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.RESET_TEMPORARY_DATABASE_ENTITIES);
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial temporary entities
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.GENERATE_INITIAL_TEMPORARY_ENTITIES);
        timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //generate initial timetable with default data
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.GENERATE_INITIAL_TIMETABLESUPERDOC_WITH_DEFAULT_DATA);
        TimeTableSuperDoc timeTableSuperDocWithDefaultData =
                timeTableGenerationInvoker.executeTimeTableGenerationOperation(null);


        //allocate default periods for all programmeGroups in db
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.ALLOCATE_DEFAULT_PERIODS_FOR_ALL_PROGRAMMEGROUPS);
        TimeTableSuperDoc timeTableSuperDocWithAllDefaultPeriodsSet =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation(timeTableSuperDocWithDefaultData);

        //generate 2 periods only ie {2,2,2,2,2} for programme group that has one of its subjects having 8periods as its subject allocation.
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.GENERATE_TWO_PERIODS_ONLY_IN_PROGRAMME_GROUP_NEEDING_IT);
        TimeTableSuperDoc timeTableSuperDocWithTwoPeriodsForProgrammeDayPeriodSetRequiringIt =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation(timeTableSuperDocWithAllDefaultPeriodsSet);

        //allocate periods for all tutors
        timeTableGenerationInvoker.setTimeTableGenerationImplType(TimeTableGenerationCommandFactory.ALLOCATE_PERIODS_FOR_ALL_TUTORS);
        TimeTableSuperDoc finalTimeTableSuperDoc =
                timeTableGenerationInvoker.
                        executeTimeTableGenerationOperation
                                (timeTableSuperDocWithTwoPeriodsForProgrammeDayPeriodSetRequiringIt);

        return finalTimeTableSuperDoc;
    }
}
