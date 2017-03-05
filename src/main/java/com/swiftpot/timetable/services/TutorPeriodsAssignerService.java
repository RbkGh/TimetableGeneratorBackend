package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         05-Mar-17 @ 5:10 PM
 */
@Service
public class TutorPeriodsAssignerService {

    @Autowired
    TimeTableSuperDocServices timeTableSuperDocServices;

    /**
     * todo IMPORTANT!!!IMPORTANT!!!! implementation not completed
     * assign tutorSubjectAndProgrammeGroupCombination onto timeTableSuperDoc object and update all concerned entities in db
     *
     * @param incomingTimeTableSuperDoc                   the incoming timetable object,ie the current state of the timetable ,propably,some periods are already set
     * @param tutorSubjectAndProgrammeGroupCombinationDoc {@link TutorSubjectAndProgrammeGroupCombinationDoc}
     * @param tutorUniqueIdInDb                           {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id}
     * @return {@link TimeTableSuperDoc}
     */
    TimeTableSuperDoc
    assignTutorSubjectandProgrammeGroupCombinationOntoTimeTableSuperDocObject(TimeTableSuperDoc incomingTimeTableSuperDoc,
                                                                              TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc,
                                                                              String tutorUniqueIdInDb
    ) {
        String timeTableSuperDocString = new Gson().toJson(incomingTimeTableSuperDoc);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class);
        String programmeCode = tutorSubjectAndProgrammeGroupCombinationDoc.getProgrammeCode();
        ProgrammeGroup programmeGroupToUpdate = timeTableSuperDocServices.
                getProgrammeGroupObjectFromTimeTableSuperDocObject(timeTableSuperDocGeneratedFromString, programmeCode);

        return null;
    }
}
