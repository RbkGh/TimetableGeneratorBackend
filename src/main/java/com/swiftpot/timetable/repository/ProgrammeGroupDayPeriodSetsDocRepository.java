/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDayPeriodSetsDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 1:43 PM
 */
public interface ProgrammeGroupDayPeriodSetsDocRepository extends MongoRepository<ProgrammeGroupDayPeriodSetsDoc, String> {

    /**
     * find by {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#programmeCode}
     *
     * @param programmeCode the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#programmeCode}
     * @return {@link ProgrammeGroupDayPeriodSetsDoc}
     */
    ProgrammeGroupDayPeriodSetsDoc findByProgrammeCode(String programmeCode);
}
