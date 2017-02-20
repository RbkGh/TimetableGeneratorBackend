package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 2:17 PM
 */
public interface ProgrammeGroupDocRepository extends MongoRepository<ProgrammeGroupDoc, String> {


    List<ProgrammeGroupDoc> findByProgrammeInitials(String programmeInitials);

    /**
     * find only programmes in this yearGroup only,eg all programmeGroups in year 2 alone
     *
     * @param yearGroup
     * @return
     */
    List<ProgrammeGroupDoc> findByYearGroup(int yearGroup);
}
