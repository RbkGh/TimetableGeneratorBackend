/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.commands;

import com.swiftpot.timetable.base.ProgrammeDayPeriodSetListInitialGenerator;
import com.swiftpot.timetable.base.ProgrammeGroupPersonalTimeTableDocInitialGenerator;
import com.swiftpot.timetable.base.TutorPersonalTimeTableInitialGenerator;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupInitialGenerator;
import com.swiftpot.timetable.base.impl.ProgrammeDayPeriodSetListInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.base.impl.ProgrammeGroupPersonalTimeTableDocInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.base.impl.TutorPersonalTimeTableInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initial Generators for all temporary entities for the generation of {@link TimeTableSuperDoc}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Mar-17 @ 6:34 PM
 */
@Component
public class GenerateInitialTemporaryEntitiesCommand implements TimetableGenerationCommand {

    @Autowired
    private ProgrammeDayPeriodSetListInitialGenerator programmeDayPeriodSetListInitialGenerator;
    @Autowired
    private ProgrammeDayPeriodSetListInitialGeneratorDefaultImpl programmeDayPeriodSetListInitialGeneratorDefault;
    @Autowired
    private TutorSubjectAndProgrammeGroupInitialGenerator tutorSubjectAndProgrammeGroupInitialGenerator;
    @Autowired
    private TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl tutorSubjectAndProgrammeGroupInitialGeneratorDefault;
    @Autowired
    private TutorPersonalTimeTableInitialGenerator tutorPersonalTimeTableInitialGenerator;
    @Autowired
    private TutorPersonalTimeTableInitialGeneratorDefaultImpl tutorPersonalTimeTableInitialGeneratorDefault;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocInitialGenerator programmeGroupPersonalTimeTableDocInitialGenerator;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocInitialGeneratorDefaultImpl programmeGroupPersonalTimeTableDocInitialGeneratorDefault;

    @Override
    public TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        int completedTask = this.generateAllTemporaryEntitiesForTimeTableGeneration();
        System.out.println("generateAllTemporaryEntitiesForTimeTableGeneration completed successfully==>" + completedTask);
        return timeTableSuperDoc;
    }

    private synchronized int generateAllTemporaryEntitiesForTimeTableGeneration() throws Exception {
        programmeDayPeriodSetListInitialGenerator = programmeDayPeriodSetListInitialGeneratorDefault;
        programmeDayPeriodSetListInitialGenerator.
                generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDocAndSaveInDb();

        tutorSubjectAndProgrammeGroupInitialGenerator = tutorSubjectAndProgrammeGroupInitialGeneratorDefault;
        tutorSubjectAndProgrammeGroupInitialGenerator.
                generateAllInitialSubjectAndProgrammeGroupCombinationDocsForAllTutorsInDBAndSaveInDb();

        tutorPersonalTimeTableInitialGenerator = tutorPersonalTimeTableInitialGeneratorDefault;
        tutorPersonalTimeTableInitialGenerator.
                generatePersonalTimeTableForAllTutorsInDbAndSaveIntoDb();

        programmeGroupPersonalTimeTableDocInitialGenerator = programmeGroupPersonalTimeTableDocInitialGeneratorDefault;
        programmeGroupPersonalTimeTableDocInitialGenerator.
                generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsInDbAndSaveInDb();

        return 0;
    }

}
