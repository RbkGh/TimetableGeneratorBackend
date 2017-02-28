package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         28-Feb-17 @ 8:36 AM
 */
public interface TutorSubjectAndProgrammeGroupCombinationDocRepository extends MongoRepository<TutorSubjectAndProgrammeGroupCombinationDoc, String> {
    /**
     * find the object so that we can pull the {@link TutorSubjectAndProgrammeGroupCombinationDoc#totalPeriodLeftToBeAllocated} from it for further calculation
     *
     * @param subjectUniqueId the {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id} of the {@link com.swiftpot.timetable.repository.db.model.SubjectDoc}
     * @param programmeCode   the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#programmeCode}
     * @return {@link TutorSubjectAndProgrammeGroupCombinationDoc}
     */
    TutorSubjectAndProgrammeGroupCombinationDoc findBySubjectUniqueIdAndProgrammeCode(String subjectUniqueId, String programmeCode);
}
