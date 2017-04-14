/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands.commandfactory;

import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.command.commands.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 2:16 PM
 */
@Component
public class TimeTableGenerationCommandFactory {
    /**
     * Use this for {@link AllocateDefaultPeriodsForAllProgrammeGroupsCommand}
     */
    public static final String ALLOCATE_DEFAULT_PERIODS_FOR_ALL_PROGRAMMEGROUPS = "ALLOCATED_DEFAULT_PERIODS_FOR_ALL_PROGRAMMEGROUPS";
    /**
     * Use this for {@link AllocatePeriodsForAllTutorsCommand}
     */
    public static final String ALLOCATE_PERIODS_FOR_ALL_TUTORS = "ALLOCATE_PERIODS_FOR_ALL_TUTORS";
    /**
     * uSE THIS FOR {@link GenerateInitialTemporaryEntitiesCommand}
     */
    public static final String GENERATE_INITIAL_TEMPORARY_ENTITIES = "GENERATE_INITIAL_TEMPORARY_ENTITIES";
    /**
     * use this for {@link GenerateInitialTimeTableSuperDocWithDefaultDataCommand}
     */
    public static final String GENERATE_INITIAL_TIMETABLESUPERDOC_WITH_DEFAULT_DATA = "GENERATE_INITIAL_TIMETABLESUPERDOC_WITH_DEFAULT_DATA";
    /**
     * use this for {@link ResetTemporaryDatabaseEntitiesCommand}
     */
    public static final String RESET_TEMPORARY_DATABASE_ENTITIES = "RESET_TEMPORARY_DATABASE_ENTITIES";
    /**
     * USE THIS FOR {@link GenerateTwoPeriodsForFormThreeClassesHavingEightPeriodsAsSubjectPeriodsCommand}
     */
    public static final String GENERATE_TWO_PERIODS_ONLY_IN_PROGRAMME_GROUP_NEEDING_IT = "GENERATE_TWO_PERIODS_ONLY_IN_PROGRAMME_GROUP_NEEDING_IT";

    @Autowired
    private AllocateDefaultPeriodsForAllProgrammeGroupsCommand allocateDefaultPeriodsForAllProgrammeGroupsCommand;
    @Autowired
    private AllocatePeriodsForAllTutorsCommand allocatePeriodsForAllTutorsCommand;
    @Autowired
    private GenerateInitialTemporaryEntitiesCommand generateInitialTemporaryEntitiesCommand;
    @Autowired
    private GenerateInitialTimeTableSuperDocWithDefaultDataCommand generateInitialTimeTableSuperDocWithDefaultDataCommand;
    @Autowired
    private ResetTemporaryDatabaseEntitiesCommand resetTemporaryDatabaseEntitiesCommand;
    @Autowired
    private GenerateTwoPeriodsForFormThreeClassesHavingEightPeriodsAsSubjectPeriodsCommand generateTwoPeriodsForFormThreeClassesHavingEightPeriodsAsSubjectPeriodsCommand;

    /**
     * @param implementationType at least one of the static final Strings from {@link TimeTableGenerationCommandFactory} ,<br>
     *                           eg {@link TimeTableGenerationCommandFactory#RESET_TEMPORARY_DATABASE_ENTITIES}
     * @return The {@link TimetableGenerationCommand}
     * @throws UnsupportedOperationException
     */
    public TimetableGenerationCommand getTimeTableGenerationCommandImpl(String implementationType) throws UnsupportedOperationException {
        switch (implementationType) {

            case ALLOCATE_DEFAULT_PERIODS_FOR_ALL_PROGRAMMEGROUPS:
                return allocateDefaultPeriodsForAllProgrammeGroupsCommand;

            case GENERATE_TWO_PERIODS_ONLY_IN_PROGRAMME_GROUP_NEEDING_IT:
                return generateTwoPeriodsForFormThreeClassesHavingEightPeriodsAsSubjectPeriodsCommand;

            case ALLOCATE_PERIODS_FOR_ALL_TUTORS:
                return allocatePeriodsForAllTutorsCommand;

            case GENERATE_INITIAL_TEMPORARY_ENTITIES:
                return generateInitialTemporaryEntitiesCommand;

            case GENERATE_INITIAL_TIMETABLESUPERDOC_WITH_DEFAULT_DATA:
                return generateInitialTimeTableSuperDocWithDefaultDataCommand;

            case RESET_TEMPORARY_DATABASE_ENTITIES:
                return resetTemporaryDatabaseEntitiesCommand;

            default:
                throw new UnsupportedOperationException(" " + implementationType + " not supported.Only one of the final strings in this class is allowed.");
        }
    }
}
