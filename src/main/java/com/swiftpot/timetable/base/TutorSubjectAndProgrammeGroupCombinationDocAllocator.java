/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc;

/**
 * This interface will be used to allocate {@link TutorSubjectAndProgrammeGroupCombinationDoc} <br>
 * until the {@link TutorSubjectAndProgrammeGroupCombinationDoc#totalPeriodLeftToBeAllocated} is 0.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Mar-17 @ 12:04 PM
 */
public interface TutorSubjectAndProgrammeGroupCombinationDocAllocator {

    /**
     * allocate the {@link TutorDoc#id} and the <br>
     * {@link SubjectDoc#id} onto the timetable object for tutor,programmeCode and any other entities involved.
     *
     * @param tutorUniqueIdInDb                           The {@link TutorDoc#id} of the {@link TutorDoc} object
     * @param tutorSubjectAndProgrammeGroupCombinationDoc the {@link TutorSubjectAndProgrammeGroupCombinationDoc} object
     * @return {@link Integer 0} to acknowledge that the process is complete
     */
    int allocateTutorSubjectAndProgrammeGroupCombinationCompletely
    (String tutorUniqueIdInDb, TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc);
}
