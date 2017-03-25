/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.ProgrammeGroupPersonalTimeTableDocInitialGenerator;
import com.swiftpot.timetable.base.TutorPersonalTimeTableInitialGenerator;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupInitialGenerator;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.ProgrammeGroupPersonalTimeTableDocServices;
import com.swiftpot.timetable.services.SubjectsAssignerService;
import com.swiftpot.timetable.services.TutorPersonalTimeTableDocServices;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.services.servicemodels.UnallocatedPeriodSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private ProgrammeGroupPersonalTimeTableDocServices programmeGroupPersonalTimeTableDocServices;
    @Autowired
    private ProgrammeDayServices programmeDayServices;
    @Autowired
    private ProgrammeGroupDayPeriodSetsDocRepository programmeGroupDayPeriodSetsDocRepository;
    @Autowired
    private ProgrammeGroupDocRepository programmeGroupDocRepository;

    @Override
    public TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationCompletely(String tutorUniqueIdInDb,
                                                                                        int totalSubjectAllocationInDb,
                                                                                        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc,
                                                                                        TimeTableSuperDoc timeTableSuperDoc) {
        String subjectUniqueIdBefore = tutorSubjectAndProgrammeGroupCombinationDoc.getSubjectUniqueId();
        String programmeCodeBefore = tutorSubjectAndProgrammeGroupCombinationDoc.getProgrammeCode();


        while ((tutorSubjectAndProgrammeGroupCombinationDocRepository.
                findBySubjectUniqueIdAndProgrammeCode
                        (subjectUniqueIdBefore, programmeCodeBefore).
                getTotalPeriodLeftToBeAllocated()) > 0) {

            TutorSubjectAndProgrammeGroupCombinationDoc currentStateOfTutorSubjectAndProgrammeGroupCombDoc =
                    tutorSubjectAndProgrammeGroupCombinationDocRepository.
                            findBySubjectUniqueIdAndProgrammeCode
                                    (subjectUniqueIdBefore, programmeCodeBefore);

            int totalPeriodLeftToBeAllocated = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getTotalPeriodLeftToBeAllocated();
            String programmeCode = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getProgrammeCode();
            String subjectUniqueIdInDb = currentStateOfTutorSubjectAndProgrammeGroupCombDoc.getSubjectUniqueId();

            timeTableSuperDoc = this.
                    allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities
                            (tutorUniqueIdInDb, totalSubjectAllocationInDb, totalPeriodLeftToBeAllocated, programmeCode, subjectUniqueIdInDb, timeTableSuperDoc);
        }
        return timeTableSuperDoc;
    }

    private TimeTableSuperDoc allocateTutorSubjectAndProgrammeGroupCombinationAndUpdateDbEntities(String tutorUniqueIdInDb,
                                                                                                  int totalSubjectAllocationInDb,
                                                                                                  int totalPeriodsLeftToBeAllocated,
                                                                                                  String programmeCode,
                                                                                                  String subjectUniqueIdInDb,
                                                                                                  TimeTableSuperDoc timeTableSuperDoc) {

        List<Integer> listOfPeriodAllocation = subjectsAssignerService.getTotalSubjectPeriodAllocationAsList(totalPeriodsLeftToBeAllocated);
        int listOfPeriodAllocationSize = listOfPeriodAllocation.size();
        int periodAllocationValue1 = listOfPeriodAllocation.get(0);//THERE WILL ALWAYS BE A VALUE IN THIS
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = tutorPersonalTimeTableDocRepository.findByTutorUniqueIdInDb(tutorUniqueIdInDb);
        List<ProgrammeDay> tutorPersonalTimeTableDocProgrammeDaysList = tutorPersonalTimeTableDoc.getProgrammeDaysList();

        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc =
                programmeGroupPersonalTimeTableDocRepository.findByProgrammeCodeIgnoreCase(programmeCode);
        List<ProgrammeDay> programmeGroupPersonalProgrammeDaysList = programmeGroupPersonalTimeTableDoc.getProgrammeDaysList();

        ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDoc =
                programmeGroupDayPeriodSetsDocRepository.findByProgrammeCode(programmeCode);

        Map<String, List<PeriodSetForProgrammeDay>> periodSetForProgrammeDayListMap =
                programmeGroupDayPeriodSetsDoc.getMapOfProgDayNameAndTheListOfPeriodSets();


        ProgrammeDay programmeDayToSetOnTimeTableSuperObject = new ProgrammeDay();

        int periodStartingNumber = 0; //TODO TEST TO MAKE SURE THAT THIS WILL NOT REMAIN ZERO WHEN USING IT OUTSIDE THE FOR LOOP BELOW.
        int periodEndingNumber = 0;  //TODO TEST TO MAKE SURE THAT THIS WILL NOT REMAIN ZERO WHEN USING IT OUTSIDE THE FOR LOOP BELOW.
        //go through programmeGroup personal programmeDay List
        for (ProgrammeDay programmeDay : programmeGroupPersonalProgrammeDaysList) {
            if (!programmeDayServices.isProgrammeDayFullyAllocated(programmeDay)) {
                String programmeDayName = programmeDay.getDayName();
                List<UnallocatedPeriodSet> unallocatedPeriodSetList =
                        programmeDayServices.getListOfUnallocatedPeriodSetsInDay(programmeDay);
                Map<Boolean, List<UnallocatedPeriodSet>> booleanUnallocatedPeriodSetMap =
                        this.isAnyOfUnallPeriodsListCapableOfGettingAssignedTheIncomingPeriodAllocValue
                                (unallocatedPeriodSetList, periodAllocationValue1);
                if (booleanUnallocatedPeriodSetMap.containsKey(true)) {
                    //TODO DONE!! COMPLETED!!! find a way to infuse the subject breakdown allocation from the config.properties file,do this in next iteration.
                    //now we check if any of the unallocatedPeriods List can take the periods considering the personal timetable of the tutor too.
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

                    List<UnallocatedPeriodSet> finalUnallocatedListThatSatisfiesAllConstraints =
                            this.getUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTable
                                    (actualUnallocatedPeriodList, tutorProgrammeDayTimeTable);

                    if (!finalUnallocatedListThatSatisfiesAllConstraints.isEmpty()) {//its not empty thus we can set it now finally

                        List<Integer> listOfPeriodAllocationForTotalPeriods = subjectsAssignerService.getTotalSubjectPeriodAllocationAsList(totalSubjectAllocationInDb);//if subject breakdown for total allocation is >2
                        if (listOfPeriodAllocationForTotalPeriods.size() > 2 &&
                                this.isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(subjectUniqueIdInDb, programmeDay)) {
                            //the subject has not been set more than five or six period times in a day
                        } else {


                            UnallocatedPeriodSet unallocatedPeriodSetToUse = new UnallocatedPeriodSet();
                            for (UnallocatedPeriodSet unallocatedPeriodSet : finalUnallocatedListThatSatisfiesAllConstraints) {
                                if (unallocatedPeriodSet.getTotalNumberOfPeriodsForSet() == periodAllocationValue1) {
                                    unallocatedPeriodSetToUse = unallocatedPeriodSet;
                                    break;
                                }
                            }
                            periodStartingNumber = unallocatedPeriodSetToUse.getPeriodStartingNumber(); //SET PERIOD STARTING NUMBER
                            periodEndingNumber = unallocatedPeriodSetToUse.getPeriodEndingNumber(); //SET PERIOD ENDING NUMBER

                            programmeDayToSetOnTimeTableSuperObject = programmeDay; //set the programmeDay to use to set the periods to on timetableSuperDoc object

                            TutorPersonalTimeTableDoc finalTutorPersonalTimeTable =
                                    tutorPersonalTimeTableDocServices.
                                            updateTutorPersonalTimeTableDocWithPeriodsAndSaveInDb(tutorUniqueIdInDb,
                                                    subjectUniqueIdInDb,
                                                    programmeDayName,
                                                    programmeCode,
                                                    periodStartingNumber,
                                                    periodEndingNumber);

                            this.updateDbWithTotalPeriodsThatHasBeenSet
                                    (programmeDay,
                                            programmeCode,
                                            subjectUniqueIdInDb,
                                            periodAllocationValue1,
                                            tutorUniqueIdInDb,
                                            periodStartingNumber,
                                            periodEndingNumber);
                            break; //break out of iterations over programmeDays
                        }
                    }

                }
            }
        }

        //TODO IMPORTANT!!!! THROW EXCEPTION WHEN NO DAY MET THE CRITERIA !!!!!!
        String programmeDayNameToFind = programmeDayToSetOnTimeTableSuperObject.getDayName();

        ProgrammeGroupDoc programmeGroupDocToUpdate = programmeGroupDocRepository.findByProgrammeCode(programmeCode);
        int yearGroupOfProgrammeCode = programmeGroupDocToUpdate.getYearGroup();

        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class); //CONVERT BACK TO OBJECT FROM JSON TO PREVENT PROBLEMS OF WRONG WRITES IN UNSOLICITED PLACES

        final int periodStartingNumberFinal = periodStartingNumber;
        final int periodEndingNumberFinal = periodEndingNumber;
        timeTableSuperDocGeneratedFromString.getYearGroupsList().forEach((YearGroup yearGroup) -> {
            if (yearGroup.getYearNumber() == yearGroupOfProgrammeCode) {
                yearGroup.getProgrammeGroupList().forEach((ProgrammeGroup programmeGroup) -> {
                    if (programmeGroup.getProgrammeCode().equalsIgnoreCase(programmeCode)) {
                        programmeGroup.getProgrammeDaysList().forEach((ProgrammeDay programmeDay) -> {
                            if (programmeDay.getDayName().equalsIgnoreCase(programmeDayNameToFind)) {
                                programmeDay.getPeriodList().forEach((PeriodOrLecture periodOrLecture) -> {
                                    if ((periodOrLecture.getPeriodNumber() >= periodStartingNumberFinal) &&
                                            (periodOrLecture.getPeriodNumber() <= periodEndingNumberFinal)) {
                                        periodOrLecture.setTutorUniqueId(tutorUniqueIdInDb); //set tutor unique id in db
                                        periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb); //set subjectUniqueIdInDb
                                        periodOrLecture.setIsAllocated(true);//set allocated to true.
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        return timeTableSuperDocGeneratedFromString;//TODO CONTINUE HERE NOW IN NEXT ITERATION.

    }

    /**
     * is subjectUniqueIdInDb passed in present in day at least 4,5 or 6 times in the lecture periods? 4,5 or 6 because all periods will have a combination of 2 or 3 thus 2+2=4,2+3=5,3+3=6,that's all the combinations possible.
     *
     * @param subjectUniqueIdInDb the {@link SubjectDoc#id}
     * @param programmeDay        the {@link ProgrammeDay} to search its {@link List} of {@link PeriodOrLecture}
     * @return {@link Boolean#TRUE} IF subjectUniqueIdInDb passed in present day IS at least 5 or 6 times in the lecture periods.{@link Boolean#FALSE} if otherwise
     */
    public boolean isSubjectAllocatedEqualToFourOrFiveOrSixTimesInProgrammeDay(String subjectUniqueIdInDb, ProgrammeDay programmeDay) {
        boolean isSubjectAllocatedEqualToFiveTimesOrEqualToSixTimesInProgrammeDay = false;
        int numberOfTimesSubjectUniqueIdInDbIsFoundInListOfPeriodsForDay = 0;

        for (PeriodOrLecture periodOrLecture : programmeDay.getPeriodList()) {
            if (periodOrLecture.getIsAllocated() && (Objects.equals(subjectUniqueIdInDb, periodOrLecture.getSubjectUniqueIdInDb()))) {
                numberOfTimesSubjectUniqueIdInDbIsFoundInListOfPeriodsForDay += 1;
            }
        }

        if ((numberOfTimesSubjectUniqueIdInDbIsFoundInListOfPeriodsForDay == 4) ||
                (numberOfTimesSubjectUniqueIdInDbIsFoundInListOfPeriodsForDay == 5) ||
                ((numberOfTimesSubjectUniqueIdInDbIsFoundInListOfPeriodsForDay == 6))
                ) {
            isSubjectAllocatedEqualToFiveTimesOrEqualToSixTimesInProgrammeDay = true;
        }
        return isSubjectAllocatedEqualToFiveTimesOrEqualToSixTimesInProgrammeDay;
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

    /**
     * get all {@link List} of {@link UnallocatedPeriodSet} that satisfy all the constraints on the tutor's personal timetable ie. {@link TutorPersonalTimeTableDoc}
     *
     * @param unallocatedPeriodSetsInProgrammeGroupDay the {@link ProgrammeDay}'s {@link List} of {@link UnallocatedPeriodSet} in programmeGroup,ie {@link com.swiftpot.timetable.model.ProgrammeGroup},
     * @param tutorPersonalTimeTableOnDay
     * @return {@link List} of {@link UnallocatedPeriodSet},returns empty list if none is found.
     */
    public List<UnallocatedPeriodSet> getUnallocatedPeriodSetThatDoesNotConflictWithAllocatedPeriodsOnTutorPersonalTimeTable
    (List<UnallocatedPeriodSet> unallocatedPeriodSetsInProgrammeGroupDay, ProgrammeDay tutorPersonalTimeTableOnDay) {
        List<UnallocatedPeriodSet> finalUnallocatedListConsideringTutorTimeTableConstraints = new ArrayList<>();

        for (UnallocatedPeriodSet unallocatedPeriodSetInProgDayTimeTable : unallocatedPeriodSetsInProgrammeGroupDay) {
            int periodStartingNumber = unallocatedPeriodSetInProgDayTimeTable.getPeriodStartingNumber();
            int periodEndingNumber = unallocatedPeriodSetInProgDayTimeTable.getPeriodEndingNumber();
            int totalNumberOfPeriodsForSet = unallocatedPeriodSetInProgDayTimeTable.getTotalNumberOfPeriodsForSet();

            int noOfPeriodsGreaterThanOrEqualToPeriodStartingNoAndLesserThanOrEqualToPeriodEndingNumber = 0;
            for (PeriodOrLecture periodOrLectureInTutorPersonalTimeTableOnDay : tutorPersonalTimeTableOnDay.getPeriodList()) {
                int currentPeriodNumber = periodOrLectureInTutorPersonalTimeTableOnDay.getPeriodNumber();

                if ((currentPeriodNumber >= periodStartingNumber) && (currentPeriodNumber <= periodEndingNumber) &&
                        (!periodOrLectureInTutorPersonalTimeTableOnDay.getIsAllocated())) {
                    noOfPeriodsGreaterThanOrEqualToPeriodStartingNoAndLesserThanOrEqualToPeriodEndingNumber += 1; //add 1
                }
            }

            if (noOfPeriodsGreaterThanOrEqualToPeriodStartingNoAndLesserThanOrEqualToPeriodEndingNumber == totalNumberOfPeriodsForSet) {
                finalUnallocatedListConsideringTutorTimeTableConstraints.add(unallocatedPeriodSetInProgDayTimeTable);//we can to final list as condition is ok.
            }
        }

        return finalUnallocatedListConsideringTutorTimeTableConstraints;
    }

    /**
     * TODO DONE!!!!! Remember to initialize the {@link TutorSubjectAndProgrammeGroupCombinationDoc} <br> immediately during first initialization so that we can search for it and retrieve and set periods left for a tutor's subject and programmeGroup {@link TutorPersonalTimeTableInitialGenerator#generatePersonalTimeTableForAllTutorsInDbAndSaveIntoDb()} handles that <br><br>
     * TODO DONE!!!!! Remember to initialize the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc} <br> immediately during first initialization so that we can search for it and update the timetable of each programmeGroup upon every write on {@link TimeTableSuperDoc}<br>{@link ProgrammeGroupPersonalTimeTableDocInitialGenerator#generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsInDbAndSaveInDb()} handles that. <br><br>
     * TODO DONE!!!!! Remember to initialize the {@link com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc} <br> immediately during first initialization so that we can search for it and update the {@link TutorSubjectAndProgrammeGroupCombinationDoc#totalPeriodLeftToBeAllocated} for that tutor for that subject upon every write on {@link TimeTableSuperDoc} object <br> {@link TutorSubjectAndProgrammeGroupInitialGenerator#generateAllInitialSubjectAndProgrammeGroupCombinationDocsForAllTutorsInDBAndSaveInDb()} handles that <br><br>
     * TODO RETURN SOMETHING FROM THIS METHOD,AS I DON'T REALLY TRUST MONGODB'S WRITE CONCERN FULLY YET.
     * <br><b>THE LOAD OF PARAMETERS ON THIS METHOD IS ABSURD,VERY STUPID,BUT IT HAPPENED BY ACCIDENT,SO ENJOY UNTIL THIS SIDE GETS REFACTORED =D :P</b> <br><br>
     *
     * @param programmeCode
     * @param subjectUniqueIdInDb
     * @param totalPeriodsThatHasBeenSet
     * @param tutorIdResponsibleForSubject
     * @param startingPeriod
     * @param stoppingPeriod
     */
    void updateDbWithTotalPeriodsThatHasBeenSet(ProgrammeDay programmeDay,
                                                String programmeCode,
                                                String subjectUniqueIdInDb,
                                                int totalPeriodsThatHasBeenSet,
                                                String tutorIdResponsibleForSubject,
                                                int startingPeriod,
                                                int stoppingPeriod) {
        String programmeDayName = programmeDay.getDayName();

        //now we decrement the value of the totalSubjectsPeriodLeft in db by the totalPeriodsThatHasBeenSet
        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc =
                tutorSubjectAndProgrammeGroupCombinationDocRepository.
                        findBySubjectUniqueIdAndProgrammeCode
                                (subjectUniqueIdInDb, programmeCode);

        int currentTotalPeriodsLeft = tutorSubjectAndProgrammeGroupCombinationDoc.getTotalPeriodLeftToBeAllocated();
        int periodLoadLeftAfterDeduction = currentTotalPeriodsLeft - totalPeriodsThatHasBeenSet;
        tutorSubjectAndProgrammeGroupCombinationDoc.setTotalPeriodLeftToBeAllocated(periodLoadLeftAfterDeduction);

        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDocUpdated =
                new TutorSubjectAndProgrammeGroupCombinationDoc(subjectUniqueIdInDb, programmeCode, periodLoadLeftAfterDeduction);
        tutorSubjectAndProgrammeGroupCombinationDocUpdated.setId(tutorSubjectAndProgrammeGroupCombinationDoc.getId());

        tutorSubjectAndProgrammeGroupCombinationDocRepository.save(tutorSubjectAndProgrammeGroupCombinationDocUpdated);

        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc =
                programmeGroupPersonalTimeTableDocServices.
                        updateProgrammeGroupPersonalTimeTableDocWithPeriodsAndSaveInDb
                                (tutorIdResponsibleForSubject, programmeCode, subjectUniqueIdInDb, programmeDayName, startingPeriod, stoppingPeriod);


        //now we have to update the tutorDoc's personal timetable too..
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc =
                tutorPersonalTimeTableDocServices.
                        updateTutorPersonalTimeTableDocWithPeriodsAndSaveInDb
                                (tutorIdResponsibleForSubject, subjectUniqueIdInDb, programmeDayName, programmeCode, startingPeriod, stoppingPeriod);

    }
}
