/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Use this class to delete and empty all entities that are used temporarily during generation of {@link TimeTableSuperDoc}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Mar-17 @ 6:10 PM
 */
@Component
public class EmptyTemporaryDatabaseEntitiesCommand implements TimetableGenerationCommand {

    @Autowired
    private PeriodAndTimeAndSubjectAndTutorAssignedDocRepository periodAndTimeAndSubjectAndTutorAssignedDocRepository;
    @Autowired
    private ProgrammeGroupDayPeriodSetsDocRepository programmeGroupDayPeriodSetsDocRepository;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    private TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    private TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) {
        int completedStatus = this.resetTemporaryEntitiesInDb();
        System.out.println("All temporary entities deleted successfully" + completedStatus);
        return timeTableSuperDoc;
    }

    /**
     * Use builder method to do this later as an update
     *
     * @return {@link Integer} to just show it is completed.,nothing else,just because i dont trust mongo fully
     */
    private int resetTemporaryEntitiesInDb() {
        periodAndTimeAndSubjectAndTutorAssignedDocRepository.deleteAll();
        programmeGroupDayPeriodSetsDocRepository.deleteAll();
        programmeGroupPersonalTimeTableDocRepository.deleteAll();
        tutorPersonalTimeTableDocRepository.deleteAll();
        tutorSubjectAndProgrammeGroupCombinationDocRepository.deleteAll();
        return 0;
    }
}
