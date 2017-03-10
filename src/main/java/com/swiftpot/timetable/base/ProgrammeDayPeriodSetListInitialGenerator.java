/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDayPeriodSetsDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use this to generate a PeriodSetForProgrammeDay for each programme in database. or list of programmes provided to it
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 12:39 PM
 */
@Component
public interface ProgrammeDayPeriodSetListInitialGenerator {

    /**
     * generate a {@link List} of {@link ProgrammeGroupDayPeriodSetsDoc} from the {@link List} of {@link ProgrammeGroupDoc} passed in and save into db.
     */
    void generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDocAndSaveInDb() throws Exception;

    /**
     * generate a {@link List} of {@link ProgrammeGroupDayPeriodSetsDoc} from the {@link List} of {@link ProgrammeGroupDoc} passed in without saving in db.
     *
     * @param programmeGroupDocs {@link List} of {@link ProgrammeGroupDoc} to generate {@link List} of {@link ProgrammeGroupDayPeriodSetsDoc} from.
     * @return {@link List} of {@link ProgrammeGroupDayPeriodSetsDoc}
     */
    List<ProgrammeGroupDayPeriodSetsDoc> generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDoc(List<ProgrammeGroupDoc> programmeGroupDocs) throws Exception;
}
