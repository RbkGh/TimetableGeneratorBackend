/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 1:10 PM
 */
@Document(collection = "ProgrammeGroupDayPeriodSetsDoc")
public class ProgrammeGroupDayPeriodSetsDoc {

    /**
     * the {@link ProgrammeGroupDoc#programmeCode} of the {@link ProgrammeGroupDoc}
     */
    private String programmeCode;

    /**
     * This will be a {@link Map} of {@link com.swiftpot.timetable.model.ProgrammeDay#dayName} and a <br>
     * corresponding {@link List} of {@link PeriodSetForProgrammeDay}
     */
    private Map<String, List<PeriodSetForProgrammeDay>> mapOfProgDayNameAndTheListOfPeriodSets;

    /**
     * @return String
     * @see ProgrammeGroupDayPeriodSetsDoc#programmeCode
     */
    public String getProgrammeCode() {
        return programmeCode;
    }

    /**
     * @param programmeCode
     * @see ProgrammeGroupDayPeriodSetsDoc#programmeCode
     */
    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    /**
     * @return
     * @see ProgrammeGroupDayPeriodSetsDoc#mapOfProgDayNameAndTheListOfPeriodSets
     */
    public Map<String, List<PeriodSetForProgrammeDay>> getMapOfProgDayNameAndTheListOfPeriodSets() {
        return mapOfProgDayNameAndTheListOfPeriodSets;
    }

    public void setMapOfProgDayNameAndTheListOfPeriodSets(Map<String, List<PeriodSetForProgrammeDay>> mapOfProgDayNameAndTheListOfPeriodSets) {
        this.mapOfProgDayNameAndTheListOfPeriodSets = mapOfProgDayNameAndTheListOfPeriodSets;
    }
}
