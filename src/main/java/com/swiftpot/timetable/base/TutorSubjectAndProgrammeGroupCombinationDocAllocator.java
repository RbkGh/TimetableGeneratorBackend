/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base;

import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc;
import org.springframework.stereotype.Component;

/**
 * This interface will be used to allocate {@link TutorSubjectAndProgrammeGroupCombinationDoc} <br>
 * until the {@link TutorSubjectAndProgrammeGroupCombinationDoc#totalPeriodLeftToBeAllocated} is 0.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Mar-17 @ 12:04 PM
 */
@Component
public interface TutorSubjectAndProgrammeGroupCombinationDocAllocator {

    /**
     * allocate the {@link TutorDoc#id} and the <br>
     * {@link SubjectDoc#id} onto the timetable object for tutor,programmeCode and any other entities involved.
     *
     * @param tutorUniqueIdInDb                           The {@link TutorDoc#id} of the {@link TutorDoc} object <br><br>
     * @param totalSubjectAllocationInDb                  The {@link SubjectDoc}'s totalSubjectAllocation,ie. the {@link com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc#totalSubjectAllocation} of the subject<br><br>
     * @param tutorSubjectAndProgrammeGroupCombinationDoc the {@link TutorSubjectAndProgrammeGroupCombinationDoc} object <br><br>
     * @param timeTableSuperDoc                           the {@link TimeTableSuperDoc}  With At Least Default Periods Set Already ,at least the timetable object must have passed through <br><br>
     *                                                    {@link TimeTableDefaultPeriodsAllocator#allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc)} already
     * @return {@link TimeTableSuperDoc} with updated values if there was any need for any update
     */
    TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationCompletely
    (String tutorUniqueIdInDb,
     int totalSubjectAllocationInDb,
     TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc,
     TimeTableSuperDoc timeTableSuperDoc) throws PracticalSubjectForDayNotFoundException;
}
