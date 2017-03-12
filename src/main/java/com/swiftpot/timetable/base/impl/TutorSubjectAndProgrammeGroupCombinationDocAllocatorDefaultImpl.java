/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.ProgrammeGroupDayPeriodSetsDocRepository;
import com.swiftpot.timetable.repository.ProgrammeGroupPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.TutorSubjectAndProgrammeGroupCombinationDocRepository;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.SubjectsAssignerService;
import com.swiftpot.timetable.services.TutorPersonalTimeTableDocServices;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.services.servicemodels.UnallocatedPeriodSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Mar-17 @ 12:39 PM
 * @see com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator
 */
@Component
public class TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl implements TutorSubjectAndProgrammeGroupCombinationDocAllocator {

    @Autowired
    private TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;
    @Autowired
    private SubjectsAssignerService subjectsAssignerService;
    @Autowired
    private TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    private TutorPersonalTimeTableDocServices tutorPersonalTimeTableDocServices;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    private ProgrammeDayServices programmeDayServices;
    @Autowired
    private ProgrammeGroupDayPeriodSetsDocRepository programmeGroupDayPeriodSetsDocRepository;

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
            String programmeCode = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getProgrammeCode();
            String subjectUniqueIdInDb = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getSubjectUniqueId();

            timeTableSuperDoc = this.
                    allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities
                            (tutorUniqueIdInDb, totalPeriodLeftToBeAllocated, programmeCode, subjectUniqueIdInDb, timeTableSuperDoc);
        }
        return timeTableSuperDoc;
    }

    private TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities(String tutorUniqueIdInDb,
                                                                                                  int totalPeriodsLeftToBeAllocated,
                                                                                                  String programmeCode,
                                                                                                  String subjectUniqueIdInDb,
                                                                                                  TimeTableSuperDoc timeTableSuperDoc) {

        List<Integer> listOfPeriodAllocation = subjectsAssignerService.getTotalSubjectPeriodAllocationAsList(totalPeriodsLeftToBeAllocated);
        int listOfPeriodAllocationSize = listOfPeriodAllocation.size();
        if (listOfPeriodAllocationSize == 1) {
            int periodAllocationValue1 = listOfPeriodAllocation.get(0);
            TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = tutorPersonalTimeTableDocRepository.findByTutorUniqueIdInDb(tutorUniqueIdInDb);
            List<ProgrammeDay> tutorPersonalTimeTableDocProgrammeDaysList = tutorPersonalTimeTableDoc.getProgrammeDaysList();

            ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc =
                    programmeGroupPersonalTimeTableDocRepository.findByProgrammeCodeIgnoreCase(programmeCode);
            List<ProgrammeDay> programmeGroupPersonalProgrammeDaysList = programmeGroupPersonalTimeTableDoc.getProgrammeDaysList();

            ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDoc =
                    programmeGroupDayPeriodSetsDocRepository.findByProgrammeCode(programmeCode);

            Map<String, List<PeriodSetForProgrammeDay>> periodSetForProgrammeDayListMap = programmeGroupDayPeriodSetsDoc.getMapOfProgDayNameAndTheListOfPeriodSets();
            //go through programmeGroup personal programmeDay List
            for (ProgrammeDay programmeDay : programmeGroupPersonalProgrammeDaysList) {
                if (!programmeDayServices.isProgrammeDayFullyAllocated(programmeDay)) {
                    String programmeDayName = programmeDay.getDayName();
                    List<PeriodOrLecture> periodOrLectureList = programmeDay.getPeriodList();
                    List<UnallocatedPeriodSet> unallocatedPeriodSetList =
                            programmeDayServices.getListOfUnallocatedPeriodSetsInDay(programmeDay);
                    Map<Boolean, List<UnallocatedPeriodSet>> booleanUnallocatedPeriodSetMap =
                            this.isAnyOfUnallPeriodsListCapableOfGettingAssignedTheIncomingPeriodAllocValue
                                    (unallocatedPeriodSetList, periodAllocationValue1);
                    if (booleanUnallocatedPeriodSetMap.containsKey(true)) {
                        //TODO DONE!! COMPLETED!!! find a way to infuse the subject breakdown allocation from the config.properties file,do this in next iteration.
                        List<PeriodSetForProgrammeDay> periodSetForProgrammeDayFromDb =
                                periodSetForProgrammeDayListMap.get(programmeDayName);
                        List<UnallocatedPeriodSet> actualUnallocatedPeriodList =
                                programmeDayServices.
                                        getUnallocatedPeriodSetFromPeriodSetForProgDay
                                                (periodSetForProgrammeDayFromDb, programmeDay);

                        ProgrammeDay tutorProgrammeDayTimeTable =
                                programmeDayServices.
                                        getProgrammeDayFromProgrammeDayListUsingProgrammeDayName
                                                (programmeDayName, tutorPersonalTimeTableDocProgrammeDaysList);
                        //TODO CONTINUE FROM HERE IN NEXT ROUND

                    }
                }
            }

        } else if (listOfPeriodAllocationSize == 2) {
            int periodAllocationValue2 = listOfPeriodAllocation.get(1);
        } else if (listOfPeriodAllocationSize == 3) {
            int periodAllocationValue3 = listOfPeriodAllocation.get(2);
        } else {
            throw new UnsupportedOperationException("Only a maximum of 3 breakdowns of a subject is supported");
        }


        return null;
    }

    /**
     * is any of the list of {@link List} of {@link UnallocatedPeriodSet} capable of getting Assigned The incoming PeriodAllocation value?
     *
     * @param unallocatedPeriodSetsList    {@link List} of {@link UnallocatedPeriodSet}
     * @param periodToBeSetAllocationValue the value of the period to be allocated,eg,5periods or 3 periods
     * @return {@link Map} of {@link Map<Boolean,List<UnallocatedPeriodSet>>}
     */
    private Map<Boolean, List<UnallocatedPeriodSet>> isAnyOfUnallPeriodsListCapableOfGettingAssignedTheIncomingPeriodAllocValue(
            List<UnallocatedPeriodSet> unallocatedPeriodSetsList, int periodToBeSetAllocationValue) {

        Map<Boolean, List<UnallocatedPeriodSet>> booleanUnallocatedPeriodSetMap = new HashMap<>();

        List<UnallocatedPeriodSet> listOfUnallocatedPeriodsThatCanTakeIncomingPeriodAllocValue = new ArrayList<>();
        for (UnallocatedPeriodSet unallocatedPeriodSet : unallocatedPeriodSetsList) {
            int totalNumberOfPeriodsForSet = unallocatedPeriodSet.getTotalNumberOfPeriodsForSet();
            if (totalNumberOfPeriodsForSet >= periodToBeSetAllocationValue) {
                listOfUnallocatedPeriodsThatCanTakeIncomingPeriodAllocValue.add(unallocatedPeriodSet);
            }
        }

        if (listOfUnallocatedPeriodsThatCanTakeIncomingPeriodAllocValue.isEmpty()) {
            booleanUnallocatedPeriodSetMap.put(false, null);
        } else {
            booleanUnallocatedPeriodSetMap.put(true, listOfUnallocatedPeriodsThatCanTakeIncomingPeriodAllocValue);
        }

        return booleanUnallocatedPeriodSetMap;
    }
}
