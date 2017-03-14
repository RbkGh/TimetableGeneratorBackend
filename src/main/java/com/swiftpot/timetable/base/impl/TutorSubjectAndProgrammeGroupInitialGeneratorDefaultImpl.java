/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorSubjectAndProgrammeGroupInitialGenerator;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         28-Feb-17 @ 7:54 AM
 */
@Component
public class TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl implements TutorSubjectAndProgrammeGroupInitialGenerator {

    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    SubjectDocRepository subjectDocRepository;
    @Autowired
    TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;

    @Override
    public void generateAllInitialSubjectAndProgrammeGroupCombinationDocsForAllTutorsInDBAndSaveInDb() {
        List<TutorDoc> allTutorDocsInDb = tutorDocRepository.findAll();
        List<TutorSubjectAndProgrammeGroupCombinationDoc> tutorSubjectAndProgrammeGroupCombinationDocsList =
                this.getAllInitialSubjectAndProgrammeGroupCombinationDocsGenerated(allTutorDocsInDb);

        tutorSubjectAndProgrammeGroupCombinationDocRepository.save(tutorSubjectAndProgrammeGroupCombinationDocsList);
    }

    /**
     * generate a list of {@link TutorSubjectAndProgrammeGroupCombinationDoc} based on the list of {@link TutorDoc} supplied.
     *
     * @param tutorDocs a list of {@link TutorDoc} that will be used to generate the corresponding list of {@link TutorSubjectAndProgrammeGroupCombinationDoc}
     * @return a list of {@link TutorSubjectAndProgrammeGroupCombinationDoc}
     */
    public List<TutorSubjectAndProgrammeGroupCombinationDoc> getAllInitialSubjectAndProgrammeGroupCombinationDocsGenerated(List<TutorDoc> tutorDocs) {
        List<TutorSubjectAndProgrammeGroupCombinationDoc> tutorSubjectAndProgrammeGroupCombinationDocsList = new ArrayList<>();
        for (TutorDoc tutorDoc : tutorDocs) {
            List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjList = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
            for (TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj : tutorSubjectIdAndProgrammeCodesListObjList) {
                String tutorSubjectUniqueId = tutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId();
                List<String> tutorProgrammeCodesList = tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList();
                for (String tutorProgrammeCode : tutorProgrammeCodesList) {
                    int totalPeriodsOfSubjectForParticularProgrammeGroup =
                            this.getTotalSubjectAllocationPeriodsForSubjectInProgrammeGroup(tutorSubjectUniqueId, tutorProgrammeCode);
                    TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc =
                            new TutorSubjectAndProgrammeGroupCombinationDoc(tutorSubjectUniqueId, tutorProgrammeCode, totalPeriodsOfSubjectForParticularProgrammeGroup);
                    //add to final list
                    tutorSubjectAndProgrammeGroupCombinationDocsList.add(tutorSubjectAndProgrammeGroupCombinationDoc);
                }
            }
        }
        return tutorSubjectAndProgrammeGroupCombinationDocsList;
    }

    /**
     * get the total Subject Allocation Periods for the particular subject in a particular programme in a particular yearGroup.
     *
     * @param subjectUniqueId the {@link SubjectDoc#id} of the {@link SubjectDoc}
     * @param programmeCode   the {@link ProgrammeGroupDoc#programmeCode} of the {@link ProgrammeGroupDoc}
     * @return the total number of subjectAllocation periods for that subject in that particular programmeGroup
     */
    int getTotalSubjectAllocationPeriodsForSubjectInProgrammeGroup(String subjectUniqueId, String programmeCode) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueId);
        String subjectCode = subjectDoc.getSubjectCode();
        ProgrammeGroupDoc programmeGroupDoc = programmeGroupDocRepository.findByProgrammeCode(programmeCode);
        int yearGroupOfProgrammeGroupDoc = programmeGroupDoc.getYearGroup();
        SubjectAllocationDoc subjectAllocationDoc = subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectCode, yearGroupOfProgrammeGroupDoc);
        int totalSubjectAllocationPeriodsForYearGroupOfProgrammeGroup = subjectAllocationDoc.getTotalSubjectAllocation();
        return totalSubjectAllocationPeriodsForYearGroupOfProgrammeGroup;
    }
}
