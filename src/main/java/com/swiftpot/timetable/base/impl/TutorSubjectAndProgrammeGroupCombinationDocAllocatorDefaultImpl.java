/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.TutorSubjectAndProgrammeGroupCombinationDocRepository;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc;
import com.swiftpot.timetable.services.SubjectsAssignerService;
import com.swiftpot.timetable.services.TutorPersonalTimeTableDocServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Mar-17 @ 12:39 PM
 * @see com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator
 */
@Component
public class TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl implements TutorSubjectAndProgrammeGroupCombinationDocAllocator {

    @Autowired
    TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;
    @Autowired
    SubjectsAssignerService subjectsAssignerService;
    @Autowired
    TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    TutorPersonalTimeTableDocServices tutorPersonalTimeTableDocServices;

    //todo remember to remove the todo in the interface implemented here that it is done once the implementation is complete
    @Override
    public TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationCompletely(String tutorUniqueIdInDb,
                                                                                        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc,
                                                                                        TimeTableSuperDoc timeTableSuperDoc) {
        TutorSubjectAndProgrammeGroupCombinationDoc currentStateOfTutorSubjectAndProgrammeGroupCombDoc =
                tutorSubjectAndProgrammeGroupCombinationDocRepository.
                        findBySubjectUniqueIdAndProgrammeCode
                                (tutorSubjectAndProgrammeGroupCombinationDoc.getSubjectUniqueId(), tutorSubjectAndProgrammeGroupCombinationDoc.getProgrammeCode());
        while (currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getTotalPeriodLeftToBeAllocated() > 0) {
            int totalPeriodLeftToBeAllocated = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getTotalPeriodLeftToBeAllocated();

            timeTableSuperDoc = this.
                    allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities
                            (tutorUniqueIdInDb, totalPeriodLeftToBeAllocated, timeTableSuperDoc);
        }
        return timeTableSuperDoc;
    }

    private TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities(String tutorUniqueIdInDb,
                                                                                                  int totalPeriodsLeftToBeAllocated,
                                                                                                  TimeTableSuperDoc timeTableSuperDoc) {

        List<Integer> listOfPeriodAllocation = subjectsAssignerService.getTotalSubjectPeriodAllocationAsList(totalPeriodsLeftToBeAllocated);
        int listOfPeriodAllocationSize = listOfPeriodAllocation.size();
        if (listOfPeriodAllocationSize == 1) {
            int periodAllocationValue1 = listOfPeriodAllocation.get(0);
            TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = tutorPersonalTimeTableDocRepository.findByTutorUniqueIdInDb(tutorUniqueIdInDb);
            List<ProgrammeDay> tutorPersonalTimeTableDocProgrammeDaysList = tutorPersonalTimeTableDoc.getProgrammeDaysList();

        } else if (listOfPeriodAllocationSize == 2) {
            int periodAllocationValue2 = listOfPeriodAllocation.get(1);
        } else if (listOfPeriodAllocationSize == 3) {
            int periodAllocationValue3 = listOfPeriodAllocation.get(2);
        } else {
            throw new UnsupportedOperationException("Only a maximum of 3 breakdowns of a subject is supported");
        }


        return null;
    }
}
