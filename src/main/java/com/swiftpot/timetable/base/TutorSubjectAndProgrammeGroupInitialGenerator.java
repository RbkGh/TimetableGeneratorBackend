/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base;

/**
 * This interface will be used to generate all the initial <br>
 * {@link com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc} <br>
 * for all the combinations of {@link com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj#tutorSubjectId} and each {@link com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj#tutorProgrammeCodesList}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         28-Feb-17 @ 7:23 AM
 */
public interface TutorSubjectAndProgrammeGroupInitialGenerator {

    /**
     * this will retrieve all {@link com.swiftpot.timetable.repository.db.model.TutorDoc}s in <br>
     * database and generate {@link com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc}
     * for all the combinations of {@link com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj#tutorSubjectId} and each {@link com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj#tutorProgrammeCodesList}
     * for every {@link com.swiftpot.timetable.repository.db.model.TutorDoc} in DB.<br>
     * <b>THIS SHOULD BE GENERATED AT THE BEGINNING OF GENERATION OF A TIMETABLE</b>
     */
    void generateAllInitialSubjectAndProgrammeGroupCombinationDocsForAllTutorsInDBAndSaveInDb();
}
