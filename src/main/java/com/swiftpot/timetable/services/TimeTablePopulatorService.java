/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl;
import com.swiftpot.timetable.exception.NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException;
import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.exception.SubjectWithEightPeriodsInDepartmentNotFoundException;
import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.util.YearGroupNumberAndNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 10:49 AM
 */
@Service
public class TimeTablePopulatorService {

    @Autowired
    private ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    private ProgrammeDaysGenerator programmeDaysGenerator;
    @Autowired
    private TimeTableDefaultPeriodsAllocatorFactory timeTableDefaultPeriodsAllocatorFactory;
    @Autowired
    private TutorDocRepository tutorDocRepository;
    @Autowired
    private TutorSubjectAndProgrammeGroupCombinationDocAllocator tutorSubjectAndProgrammeGroupCombinationDocAllocator;
    @Autowired
    private TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl tutorSubjectAndProgrammeGroupCombinationDocAllocatorDefault;
    @Autowired
    private SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    private SubjectDocRepository subjectDocRepository;
    @Autowired
    private TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;
    @Autowired
    private DepartmentDocRepository departmentDocRepository;
    @Autowired
    private ProgrammeGroupDocServices programmeGroupDocServices;
    @Autowired
    private ProgrammeGroupDayPeriodSetsDocRepository programmeGroupDayPeriodSetsDocRepository;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    private ProgrammeDayServices programmeDayServices;

    private static final Logger logger = LogManager.getLogger();


    /**
     * generate an initial {@link TimeTableSuperDoc} with default data,ie {@link YearGroup},{@link ProgrammeGroup},{@link com.swiftpot.timetable.model.ProgrammeDay}s and other initial data
     *
     * @return {@link TimeTableSuperDoc} with default initial data set.
     * @throws Exception
     */
    public TimeTableSuperDoc partOneGenerateInitialTimeTableSuperDocWithInitialData() throws Exception {
        List<ProgrammeGroupDoc> allProgrammeGroupDocsListInDb = this.getAllProgrammeGroupDocsListInDb();

        TimeTableSuperDoc timeTableSuperDoc = new TimeTableSuperDoc();
        //set yearGroupList
        List<YearGroup> yearGroupsList = new ArrayList<>();

        Map<Integer, List<ProgrammeGroupDoc>> mapOfYearGroupAndProgrammeGroupDocs =
                programmeGroupDocServices.
                        getYearGroupNumbersAndProgrammeGroupsThatExistInEachYearGroupNumber(allProgrammeGroupDocsListInDb);

        int numberOfYearGroups = mapOfYearGroupAndProgrammeGroupDocs.size();
        //go through ProgrammeGroupDoc list and set new YearGroup for each instance and add to yearGroupsList
        for (int currentNo = 1; currentNo <= numberOfYearGroups; currentNo++) { //no yearGroup starts from 0,hence we start our loop from 1 instead of 0
            int currentYearGroup = currentNo;
            String currentYearName = YearGroupNumberAndNames.getYearGroupName(currentYearGroup);

            List<ProgrammeGroup> programmeGroupList = new ArrayList<>();
            //now to set programmeGroupList for YearGroup,get the programmeGroupDocs List from the map using the currentYearGroup number as the key.
            List<ProgrammeGroupDoc> programmeGroupDocForCurrentYearGroupList = mapOfYearGroupAndProgrammeGroupDocs.get(currentYearGroup);
            //set each ProgrammeGroup's parameters from ProgramGroupDoc and add to ProgrammeGroup list
            for (ProgrammeGroupDoc x : programmeGroupDocForCurrentYearGroupList) {
                ProgrammeGroup programmeGroup = new ProgrammeGroup();
                programmeGroup.setProgrammeCode(x.getProgrammeCode());
                //set wether the programmeCode Requires a technical Workshop or not
                programmeGroup.setIsProgrammeRequiringPracticalsClassroom(x.getIsTechnicalWorkshopOrLabRequired());
                //Now we need to set programmeDaysList ,Initialize it typically from Monday To Friday with the days and the periodsList
                programmeGroup.setProgrammeDaysList(programmeDaysGenerator.generateAllProgrammeDays(x.getProgrammeCode()));
                //now we set programme subjects unique ids List
                if (x.getIsTechnicalWorkshopOrLabRequired()) { //at this point we expect that there is at least one practicals subject present
                    programmeGroup.setProgrammeSubjectsUniqueIdInDbList(this.getProgrammeGroupSubjectUniqueIdList(x));
                } else {
                    programmeGroup.setProgrammeSubjectsUniqueIdInDbList(null);//not a practicals subject,thus set to null
                }
                //finally in this loop,finish off by adding the programmeGroup to the programmeGroupList
                programmeGroupList.add(programmeGroup);
            }

            YearGroup yearGroup = new YearGroup();
            yearGroup.setYearName(currentYearName);
            yearGroup.setYearNumber(currentYearGroup);
            //now we can set programmeGroupList for YearGroup,as we have finished iterating through
            yearGroup.setProgrammeGroupList(programmeGroupList);
            //now we can add current YearGroup Object to the yearGroupList
            yearGroupsList.add(yearGroup);
        }
        //now we can set yearGroupList to the timeTableSuperDoc and return it finally
        timeTableSuperDoc.setYearGroupsList(yearGroupsList);

        return timeTableSuperDoc;
    }

    /**
     * allocate default periods for all programmeDays on timetableSuperDoc object
     *
     * @param timeTableDefaultPeriodsAllocatorType       the type of allocator to pass in . eg pass in {@link TimeTableDefaultPeriodsAllocatorFactory#DEFAULT_IMPLEMENTATION} for the default implementation.
     * @param timeTableSuperDocWithInitialDefaultDataSet the {@link TimeTableSuperDoc} that has passed through {@link TimeTablePopulatorService#partOneGenerateInitialTimeTableSuperDocWithInitialData()} already for default data to be set already.
     * @return {@link TimeTableSuperDoc}
     * @throws Exception
     */
    public TimeTableSuperDoc partTwoAllocateDefaultPeriods(String timeTableDefaultPeriodsAllocatorType, TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) throws Exception {
        timeTableDefaultPeriodsAllocatorFactory.setTimeTableDefaultPeriodsAllocator(timeTableDefaultPeriodsAllocatorType);
        return timeTableDefaultPeriodsAllocatorFactory.getTimeTableDefaultPeriodsAllocator().
                allocateDefaultPeriodsOnTimeTable(timeTableSuperDocWithInitialDefaultDataSet);
    }

    /**
     * generate 2 periods for day ie {2,2,2,2,2} for all elective subjects that have 8periods as subject allocation
     *
     * @return 0 to only show that method has completed execution
     */
    public int partThreeGenerateTwoPeriodsForAtLeastOneDayInWeekForProgrammesThatHaveEightPeriodsSubjectAllocation() {
        //todo THROW AN EXCEPTION IF A CORE SUBJECT HAS 8 PERIOD ALLOCATIONS LATER,TO NULLIFY THE ASSUMPTION BEING USED HERE
        List<DepartmentDoc> electiveDepartmentDocs = departmentDocRepository.findByDeptType(DepartmentDoc.DEPARTMENT_TYPE_ELECTIVE);
        List<DepartmentDoc> departmentDocsThatHaveAtLeastOneSubjectHavingEightPeriodAllcoation = new ArrayList<>(0);
        int yearGroupNumberToCheck = 3;
        int numberOfPeriodsToMatchAgainst = 8;
        for (DepartmentDoc departmentDoc : electiveDepartmentDocs) {
            List<String> subjectsDocIdList = departmentDoc.getProgrammeSubjectsDocIdList();
            for (String subjectDocId : subjectsDocIdList) {
                SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectDocId);
                if (subjectDoc.getSubjectYearGroupList().contains(yearGroupNumberToCheck)) { //if the yearGroups contain 3rd years,
                    SubjectAllocationDoc subjectAllocationDoc =
                            subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectDoc.getSubjectCode(), yearGroupNumberToCheck);
                    if (Objects.equals(subjectAllocationDoc.getTotalSubjectAllocation(), numberOfPeriodsToMatchAgainst)) {
                        departmentDocsThatHaveAtLeastOneSubjectHavingEightPeriodAllcoation.add(departmentDoc);
                        break; //we expect only one subject to be 8 periods,TODO refactor and throw exception if more than one subject is 8periods in department later
                    }
                }
            }
        }

        if (!departmentDocsThatHaveAtLeastOneSubjectHavingEightPeriodAllcoation.isEmpty()) {
            for (DepartmentDoc departmentDoc : departmentDocsThatHaveAtLeastOneSubjectHavingEightPeriodAllcoation) {
                String subjectDocIdThatHasEightPeriodsAsSubjectAllocation = null;
                try {
                    subjectDocIdThatHasEightPeriodsAsSubjectAllocation =
                            this.findSubjectDocIdThatHasEightPeriodsAsSubjectAllocation(departmentDoc);
                } catch (SubjectWithEightPeriodsInDepartmentNotFoundException e) {
                    subjectDocIdThatHasEightPeriodsAsSubjectAllocation = null; //reset to null for further operation
                }
                if (!Objects.isNull(subjectDocIdThatHasEightPeriodsAsSubjectAllocation)) {
                    List<TutorDoc> tutorDocsThatBelongToDepartment =
                            tutorDocRepository.findByDepartmentId(departmentDoc.getId());
                    for (TutorDoc tutorDoc : tutorDocsThatBelongToDepartment) {
                        List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjsList =
                                tutorDoc.getTutorSubjectsAndProgrammeCodesList();

                        for (TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj : tutorSubjectIdAndProgrammeCodesListObjsList) {
                            String currentSubjectId = tutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId();
                            if (Objects.equals(currentSubjectId, subjectDocIdThatHasEightPeriodsAsSubjectAllocation)) {
                                List<String> programmeCodeList = tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList();
                                for (String programmeCode : programmeCodeList) {
                                    ProgrammeGroupDoc programmeGroupDoc = programmeGroupDocRepository.findByProgrammeCode(programmeCode);
                                    if (Objects.equals(programmeGroupDoc.getYearGroup(), yearGroupNumberToCheck)) {
                                        this.updateProgrammeGroupDayPeriodSetDocWithTwoPeriodsOnlyForDay(programmeGroupDoc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return 0;//just to denote finished,this is because mongo behaves strangely in void methods.
    }

    /**
     * update programmeGroup day periodset with only 2 periods for day {2,2,2,2,2}.
     *
     * @param programmeGroupDoc
     */
    private synchronized void updateProgrammeGroupDayPeriodSetDocWithTwoPeriodsOnlyForDay(ProgrammeGroupDoc programmeGroupDoc) {


        ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDoc =
                programmeGroupDayPeriodSetsDocRepository.findByProgrammeCode(programmeGroupDoc.getProgrammeCode());
        Map<String, List<PeriodSetForProgrammeDay>> mapOfProgrammeDayNamesAndListOfPeriodSets =
                programmeGroupDayPeriodSetsDoc.getMapOfProgDayNameAndTheListOfPeriodSets();

        //todo NICE TO HAVE SOME RANDOMIZATION IN HERE LATER!,NOR PRIORITY THOUGH

        mapOfProgrammeDayNamesAndListOfPeriodSets.forEach((programmeDayName, periodSetForProgrammeDays) -> {
            if (isProgrammeDayFullyUnAllocatedForProgrammeGroup(programmeGroupDoc, programmeDayName)) {
                mapOfProgrammeDayNamesAndListOfPeriodSets.replace(programmeDayName, this.createAndRetrieveTwoPeriodSetsOnlyForDay());
            }
        });

        //do this because,strangely,if we just change set the List of periodSetForProgrammeDaysList,mongo or whatever does not save it,hence just create a new one with updated values,to avoid any hogwash.
        ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDocUpdated = new ProgrammeGroupDayPeriodSetsDoc();
        programmeGroupDayPeriodSetsDocUpdated.setId(programmeGroupDayPeriodSetsDoc.getId());
        programmeGroupDayPeriodSetsDocUpdated.setProgrammeCode(programmeGroupDayPeriodSetsDoc.getProgrammeCode());
        programmeGroupDayPeriodSetsDocUpdated.setMapOfProgDayNameAndTheListOfPeriodSets(mapOfProgrammeDayNamesAndListOfPeriodSets);

        programmeGroupDayPeriodSetsDocRepository.save(programmeGroupDayPeriodSetsDocUpdated);
    }

    /**
     * create and retrieve two periods set only for the day ie 2,2,2,2,2 for day
     *
     * @return {@link List} of {@link PeriodSetForProgrammeDay}
     */
    private List<PeriodSetForProgrammeDay> createAndRetrieveTwoPeriodSetsOnlyForDay() {
        int totalNumberOfPeriodsForEach = 2;

        PeriodSetForProgrammeDay periodSetDay1 = new PeriodSetForProgrammeDay();
        periodSetDay1.setPeriodStartingNumber(1);
        periodSetDay1.setPeriodEndingNumber(2);
        periodSetDay1.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForEach);

        PeriodSetForProgrammeDay periodSetDay2 = new PeriodSetForProgrammeDay();
        periodSetDay2.setPeriodStartingNumber(3);
        periodSetDay2.setPeriodEndingNumber(4);
        periodSetDay2.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForEach);

        PeriodSetForProgrammeDay periodSetDay3 = new PeriodSetForProgrammeDay();
        periodSetDay3.setPeriodStartingNumber(5);
        periodSetDay3.setPeriodEndingNumber(6);
        periodSetDay3.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForEach);

        PeriodSetForProgrammeDay periodSetDay4 = new PeriodSetForProgrammeDay();
        periodSetDay4.setPeriodStartingNumber(7);
        periodSetDay4.setPeriodEndingNumber(8);
        periodSetDay4.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForEach);

        PeriodSetForProgrammeDay periodSetDay5 = new PeriodSetForProgrammeDay();
        periodSetDay5.setPeriodStartingNumber(9);
        periodSetDay5.setPeriodEndingNumber(10);
        periodSetDay5.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForEach);

        List<PeriodSetForProgrammeDay> periodSetForProgrammeDaysList =
                new ArrayList<>(
                        Arrays.asList(periodSetDay1,
                                periodSetDay2,
                                periodSetDay3,
                                periodSetDay4,
                                periodSetDay5));
        return periodSetForProgrammeDaysList;
    }

    /**
     * is programmeDay fully <b>UNALLOCATED</b>,NOT ALLOCATED!, for the particular programmeGroup?
     *
     * @param programmeGroupDoc
     * @param programmeDayName
     * @return {@link Boolean}
     */
    private boolean isProgrammeDayFullyUnAllocatedForProgrammeGroup(ProgrammeGroupDoc programmeGroupDoc, String programmeDayName) {

        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc =
                programmeGroupPersonalTimeTableDocRepository.findByProgrammeCodeIgnoreCase(programmeGroupDoc.getProgrammeCode());
        List<ProgrammeDay> programmeDaysList = programmeGroupPersonalTimeTableDoc.getProgrammeDaysList();

        boolean isProgrammeDayFullyUnAllocatedForProgrammeGroup = false;
        for (ProgrammeDay programmeDay : programmeDaysList) {
            if (programmeDay.getDayName().equalsIgnoreCase(programmeDayName)) {
                if (programmeDayServices.isProgrammeDayFullyUnAllocated(programmeDay)) {
                    isProgrammeDayFullyUnAllocatedForProgrammeGroup = true;
                    break;
                }
            }
        }
        return isProgrammeDayFullyUnAllocatedForProgrammeGroup;
    }

    /**
     * find {@link SubjectDoc#id} that has 8 periods as subject allocation in department provided
     *
     * @param departmentDoc
     * @return {@link SubjectDoc#id} of the subject
     * @throws {@link SubjectWithEightPeriodsInDepartmentNotFoundException} to show that no subject has 8 periods allocation in department provided
     */
    private String findSubjectDocIdThatHasEightPeriodsAsSubjectAllocation(DepartmentDoc departmentDoc) throws SubjectWithEightPeriodsInDepartmentNotFoundException {
        String subjectDocIdThatHasEightPeriodsAsSubjectAllocation = null;
        List<String> subjectsDocIdList = departmentDoc.getProgrammeSubjectsDocIdList();
        for (String subjectDocId : subjectsDocIdList) {
            SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectDocId);
            int yearGroupNumberToCheck = 3;
            int numberOfPeriodsToMatchAgainst = 8;
            if (subjectDoc.getSubjectYearGroupList().contains(yearGroupNumberToCheck)) { //if the yearGroups contain 3rd years,
                SubjectAllocationDoc subjectAllocationDoc =
                        subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectDoc.getSubjectCode(), yearGroupNumberToCheck);
                if (Objects.equals(subjectAllocationDoc.getTotalSubjectAllocation(), numberOfPeriodsToMatchAgainst)) {
                    subjectDocIdThatHasEightPeriodsAsSubjectAllocation = subjectDocId;
                    break; //we expect only one subject to be 8 periods,TODO refactor and throw exception if more than one subject is 8periods in department later
                }
            }
        }
        if (!Objects.isNull(subjectDocIdThatHasEightPeriodsAsSubjectAllocation)) {
            return subjectDocIdThatHasEightPeriodsAsSubjectAllocation;
        } else {
            throw new SubjectWithEightPeriodsInDepartmentNotFoundException("No subject found in " + departmentDoc.getDeptName() +
                    " with id of(" + departmentDoc.getId() + ") that has 8 periods as subject allocation");
        }
    }

    /**
     * allocate periods for all tutors in database,both core and elective tutors.
     *
     * @param timeTableSuperDocWithDefaultPeriodsSetAlready
     * @return
     * @throws PracticalSubjectForDayNotFoundException
     * @throws NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException
     */
    public TimeTableSuperDoc partFourAllocatePeriodsForAllTutors(TimeTableSuperDoc timeTableSuperDocWithDefaultPeriodsSetAlready) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException {
        TimeTableSuperDoc timetableSuperDocAfterCoreTutorsAllocated =
                this.allocatePeriodsForCoreTutors(timeTableSuperDocWithDefaultPeriodsSetAlready);
        TimeTableSuperDoc finalTimeTableSuperDocAfterElectiveTutorsAllocated =
                this.allocatePeriodsForElectiveTutors(timetableSuperDocAfterCoreTutorsAllocated);

        return finalTimeTableSuperDocAfterElectiveTutorsAllocated;
    }

    /**
     * this will generate and allocate all periods for supplied {@link TutorDoc}s ie all {@link TutorDoc#tutorSubjectsAndProgrammeCodesList}
     *
     * @param timeTableSuperDocWithDefaultPeriodsSetAlready the {@link TimeTableSuperDoc} with default periods set already.
     * @return final fully generated {@link TimeTableSuperDoc}
     */
    private TimeTableSuperDoc allocatePeriodsForSuppliedTutors(TimeTableSuperDoc timeTableSuperDocWithDefaultPeriodsSetAlready, List<TutorDoc> tutorDocsSupplied) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException {
        tutorSubjectAndProgrammeGroupCombinationDocAllocator = tutorSubjectAndProgrammeGroupCombinationDocAllocatorDefault;

        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDocWithDefaultPeriodsSetAlready);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class);

        for (TutorDoc tutorDoc : tutorDocsSupplied) {
            List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjs =
                    tutorDoc.getTutorSubjectsAndProgrammeCodesList();

            for (TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj : tutorSubjectIdAndProgrammeCodesListObjs) {
                String tutorUniqueIdInDb = tutorDoc.getId();
                String subjectUniqueIdInDb = tutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId();
                List<String> tutorProgrammeCodeList =
                        tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList();


                for (String programmeCode : tutorProgrammeCodeList) {
                    int totalSubjectAllocationForSubjectInDb =
                            this.getTotalSubjectAllocationForSubject(subjectUniqueIdInDb, programmeCode);

                    TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc =
                            tutorSubjectAndProgrammeGroupCombinationDocRepository.
                                    findBySubjectUniqueIdAndProgrammeCode(subjectUniqueIdInDb, programmeCode);


                    timeTableSuperDocGeneratedFromString = tutorSubjectAndProgrammeGroupCombinationDocAllocator.
                            allocateTutorSubjectAndProgrammeGroupCombinationCompletely
                                    (tutorUniqueIdInDb,
                                            totalSubjectAllocationForSubjectInDb,
                                            tutorSubjectAndProgrammeGroupCombinationDoc,
                                            timeTableSuperDocGeneratedFromString);
                }

            }
        }

        return timeTableSuperDocGeneratedFromString;
    }

    protected TimeTableSuperDoc allocatePeriodsForCoreTutors(TimeTableSuperDoc timeTableSuperDoc) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException {
        List<TutorDoc> coreTutorDocs =
                tutorDocRepository.findByTutorSubjectSpeciality(TutorDoc.CORE_TUTOR);
        if (coreTutorDocs.isEmpty()) {
            return timeTableSuperDoc;
        } else {
            return this.allocatePeriodsForSuppliedTutors(timeTableSuperDoc, coreTutorDocs);
        }
    }

    protected TimeTableSuperDoc allocatePeriodsForElectiveTutors(TimeTableSuperDoc timeTableSuperDoc) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException {
        List<TutorDoc> electiveTutorDocs =
                tutorDocRepository.findByTutorSubjectSpeciality(TutorDoc.ELECTIVE_TUTOR);
        if (electiveTutorDocs.isEmpty()) {
            return timeTableSuperDoc;
        } else {
            return this.allocatePeriodsForSuppliedTutors(timeTableSuperDoc, electiveTutorDocs);
        }
    }

    /**
     * get the total subject allocation of a subject based on the {@link SubjectDoc#id} passed in and the {@link ProgrammeGroupDoc#programmeCode}
     *
     * @param subjectUniqueIdInDb the {@link SubjectDoc#id}
     * @param programmeCode       the {@link ProgrammeGroupDoc#programmeCode}
     * @return {@link Integer}
     */
    private synchronized int getTotalSubjectAllocationForSubject(String subjectUniqueIdInDb, String programmeCode) {
        SubjectDoc subjectDoc =
                subjectDocRepository.findOne(subjectUniqueIdInDb);
        String subjectCode = subjectDoc.getSubjectCode();

        ProgrammeGroupDoc programmeGroupDoc =
                programmeGroupDocRepository.findByProgrammeCode(programmeCode);
        int yearGroupOfProgrammeGroup = programmeGroupDoc.getYearGroup();

        SubjectAllocationDoc subjectAllocationDoc =
                subjectAllocationDocRepository.
                        findBySubjectCodeAndYearGroup(subjectCode, yearGroupOfProgrammeGroup);
        return subjectAllocationDoc.getTotalSubjectAllocation();
    }

    private List<ProgrammeGroupDoc> getAllProgrammeGroupDocsListInDb() {
        return programmeGroupDocRepository.findAll();
    }

    /**
     * get all subjects offered by department,if english department,only english department unique id will be returned ,if auto engineering ,physics ,chem and others may be returned.
     *
     * @param programmeGroupDoc
     * @return
     */
    private List<String> getProgrammeGroupSubjectUniqueIdList(ProgrammeGroupDoc programmeGroupDoc) {
        DepartmentDoc departmentDoc = departmentDocRepository.
                findByDeptProgrammeInitials(programmeGroupDoc.getProgrammeInitials());
        return departmentDoc.getProgrammeSubjectsDocIdList();
    }
}
