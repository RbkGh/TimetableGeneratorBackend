package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 8:52 PM
 */
public interface ProgrammeGroupPersonalTimeTableDocRepository extends MongoRepository<ProgrammeGroupPersonalTimeTableDoc, String> {

    /**
     * find the the {@link ProgrammeGroupPersonalTimeTableDoc} ignoring the case type of <br>
     * {@link ProgrammeGroupPersonalTimeTableDoc#programmeCode} passed in
     *
     * @param programmeCode
     * @return
     */
    ProgrammeGroupPersonalTimeTableDoc findByProgrammeCodeIgnoreCase(String programmeCode);
}
