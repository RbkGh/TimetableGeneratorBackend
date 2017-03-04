package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 10:23 PM
 */
@Component
public interface ProgrammeGroupPersonalTimeTableDocInitialGenerator {

    /**
     * * this will generate {@link ProgrammeGroupPersonalTimeTableDoc} for each <br>
     * {@link List} element of {@link ProgrammeGroupDoc} in database and all of the
     * {@link ProgrammeGroupPersonalTimeTableDoc} generated will be saved back into database.
     */
    void generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsInDbAndSaveInDb() throws Exception;

    /**
     * this will generate {@link ProgrammeGroupPersonalTimeTableDoc} for each <br>
     * {@link List} element of {@link ProgrammeGroupDoc} passed in,
     * <b>THIS WILL NOT BE SAVED IN DB,ALLOWS ALSO FOR UNIT TESTING</b>
     *
     * @param programmeGroupDocs
     * @return {@link List} of {@link ProgrammeGroupPersonalTimeTableDoc}
     */
    List<ProgrammeGroupPersonalTimeTableDoc> generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsPassedIn(List<ProgrammeGroupDoc> programmeGroupDocs) throws Exception;
}
