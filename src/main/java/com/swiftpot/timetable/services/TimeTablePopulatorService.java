/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupCombinationDocAllocator;
import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupCombinationDocAllocatorDefaultImpl;
import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.util.YearGroupNumberAndNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        int numberOfProgrammeGroupDocs = allProgrammeGroupDocsListInDb.size();
        //go through ProgrammeGroupDoc list and set new YearGroup for each instance and add to yearGroupsList
        for (int currentNo = 0; currentNo < numberOfProgrammeGroupDocs; currentNo++) {
            int currentYearGroup = allProgrammeGroupDocsListInDb.get(currentNo).getYearGroup();
            String currentYearName = YearGroupNumberAndNames.getYearGroupName(currentYearGroup);

            YearGroup yearGroup = new YearGroup();
            yearGroup.setYearName(currentYearName);
            yearGroup.setYearNumber(currentYearGroup);

            //now to set programmeGroupList for YearGroup,fetch all ProgrammeGroupDoc from db with current yeargroup Number
            List<ProgrammeGroup> programmeGroupList = new ArrayList<>();
            ProgrammeGroup programmeGroup = new ProgrammeGroup();
            List<ProgrammeGroupDoc> programmeGroupDocForCurrentYearGroupList = programmeGroupDocRepository.findByYearGroup(currentYearGroup);
            //set each ProgrammeGroup's parameters from ProgramGroupDoc and add to ProgrammeGroup list
            for (ProgrammeGroupDoc x : programmeGroupDocForCurrentYearGroupList) {
                programmeGroup.setProgrammeCode(x.getProgrammeCode());
                //set wether the programmeCode Requires a technical Workshop or not
                programmeGroup.setIsProgrammeRequiringPracticalsClassroom(x.getIsTechnicalWorkshopOrLabRequired());
                //Now we need to set programmeDaysList ,Initialize it typically from Monday To Friday with the days and the periodsList
                programmeGroup.setProgrammeDaysList(programmeDaysGenerator.generateAllProgrammeDays(x.getProgrammeCode()));
                //now we set programme subjects unique ids List
                List<String> programmeGroupSubjectUniqueIdList = this.getProgrammeGroupSubjectUniqueIdList(x);
                if (programmeGroupSubjectUniqueIdList.isEmpty()) {
                    programmeGroup.setProgrammeSubjectsUniqueIdInDbList(null);
                }
                programmeGroup.setProgrammeSubjectsUniqueIdInDbList(programmeGroupSubjectUniqueIdList);
                //finally in this loop,finish off by adding the programmeGroup to the programmeGroupList
                programmeGroupList.add(programmeGroup);
            }
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
        return timeTableDefaultPeriodsAllocatorFactory.getTimeTableDefaultPeriodsAllocator(timeTableDefaultPeriodsAllocatorType).
                allocateDefaultPeriodsOnTimeTable(timeTableSuperDocWithInitialDefaultDataSet);
    }

    /**
     * the final stage of generation of {@link TimeTableSuperDoc} ,this will generate and allocate all periods for {@link TutorDoc} ie all {@link TutorDoc#tutorSubjectsAndProgrammeCodesList}
     *
     * @param timeTableSuperDocWithDefaultPeriodsSetAlready the {@link TimeTableSuperDoc} with default periods set already.
     * @return final fully generated {@link TimeTableSuperDoc}
     */
    public TimeTableSuperDoc partThreeAllocatePeriodsForEachTutor(TimeTableSuperDoc timeTableSuperDocWithDefaultPeriodsSetAlready) {
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
        if (Objects.isNull(departmentDoc)) {
            return new ArrayList<>(0);
        } else {
            return departmentDoc.getProgrammeSubjectsDocIdList();
        }
    }

}
