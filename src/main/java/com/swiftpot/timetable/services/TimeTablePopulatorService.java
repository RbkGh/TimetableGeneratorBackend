/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl;
import com.swiftpot.timetable.command.TimeTableGenerationClient;
import com.swiftpot.timetable.exception.NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException;
import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import com.swiftpot.timetable.util.YearGroupNumberAndNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private BusinessLogicConfigurationProperties businessLogicConfigurationProperties;
    @Autowired
    private TimeTableGenerationClient timeTableGenerationClient;

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
     * the final stage of generation of {@link TimeTableSuperDoc} ,this will generate and allocate all periods for {@link TutorDoc} ie all {@link TutorDoc#tutorSubjectsAndProgrammeCodesList}
     *
     * @param timeTableSuperDocWithDefaultPeriodsSetAlready the {@link TimeTableSuperDoc} with default periods set already.
     * @return final fully generated {@link TimeTableSuperDoc}
     */
    public TimeTableSuperDoc partThreeAllocatePeriodsForEachTutor(TimeTableSuperDoc timeTableSuperDocWithDefaultPeriodsSetAlready) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException {
        tutorSubjectAndProgrammeGroupCombinationDocAllocator = tutorSubjectAndProgrammeGroupCombinationDocAllocatorDefault;

        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDocWithDefaultPeriodsSetAlready);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class);

        List<TutorDoc> tutorDocsInDb =
                tutorDocRepository.findAll();
        for (TutorDoc tutorDoc : tutorDocsInDb) {
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
