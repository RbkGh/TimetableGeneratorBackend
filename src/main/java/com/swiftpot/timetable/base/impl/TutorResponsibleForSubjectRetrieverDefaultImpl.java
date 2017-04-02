/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.TutorResponsibleForSubjectRetriever;
import com.swiftpot.timetable.exception.PracticalTutorNotFoundException;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.services.DepartmentDocServices;
import com.swiftpot.timetable.services.TutorDocServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 10:39 PM
 */
@Component
public class TutorResponsibleForSubjectRetrieverDefaultImpl implements TutorResponsibleForSubjectRetriever {
    @Autowired
    DepartmentDocServices departmentDocServices;
    @Autowired
    TutorDocServices tutorDocServices;
    @Autowired
    TutorDocRepository tutorDocRepository;

    @Override
    public String getTutorObjectUniqueIdResponsibleForSubject(String subjectUniqueIdInDb, String programmeCode) throws PracticalTutorNotFoundException {
        DepartmentDoc departmentDocThatSubjectBelongsTo =
                departmentDocServices.getDepartmentThatSpecificSubjectBelongsTo(subjectUniqueIdInDb);
        String departmentId = departmentDocThatSubjectBelongsTo.getId();
        List<TutorDoc> tutorDocsInTheDepartment = tutorDocRepository.findByDepartmentId(departmentId);
        String tutorObjectUniqueIdResponsibleForSubject = "";
        boolean foundTutorResponsibleForSubject = false;
        for (TutorDoc tutorDoc : tutorDocsInTheDepartment) {
            if (foundTutorResponsibleForSubject) {
                break;
            }
            List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectsAndProgrammeCodesList = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
            for (TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj : tutorSubjectsAndProgrammeCodesList) {
                if (Objects.equals(tutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId(), subjectUniqueIdInDb)) {
                    if (tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList().contains(programmeCode)) {
                        foundTutorResponsibleForSubject = true;
                        tutorObjectUniqueIdResponsibleForSubject = tutorDoc.getId();
                        break;
                    }
                }
            }
        }
        if (!foundTutorResponsibleForSubject) {
            throw new PracticalTutorNotFoundException(" Subject with id " + subjectUniqueIdInDb + " and programmeCode of " + programmeCode + " not found.Practicals tutor is not found");
        }
        return tutorObjectUniqueIdResponsibleForSubject;
    }
}
