package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         08-Feb-17 @ 2:56 PM
 */
@Service
public class GeneralAbstractServices {

    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    DepartmentDocRepository departmentDocRepository;

    /**
     * during every update and deletion of subject,& during every programmeGroup update and deletion,reset the tutor property references
     */
    public void resetAllTutorsSubjectIdAndProgrammeCodeListProperty() {
        new Thread(() -> {
            List<TutorDoc> allTutorDocsInDb = tutorDocRepository.findAll();
            if (!allTutorDocsInDb.isEmpty()) {
                allTutorDocsInDb.forEach(tutorDoc -> tutorDoc.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)));//set to zero or empty list
                tutorDocRepository.save(allTutorDocsInDb);
            }
        }).start();
    }

    /**
     * check if tutor's departmentId is equal to any DepartmentDoc's id.
     *
     * @param tutorDoc
     * @return
     */
    public boolean isTutorAssignedToAnyDepartment(TutorDoc tutorDoc) {
        String tutorDocDepartmentId = tutorDoc.getDepartmentId();
        List<DepartmentDoc> allDepartmentDocs = departmentDocRepository.findAll();
        if (allDepartmentDocs.size() > 0) {
            boolean isTutorAssignedToAnyDepartment = false;
            for (int i = 0; i < allDepartmentDocs.size(); i++) {
                DepartmentDoc currentDepartmentDoc = allDepartmentDocs.get(i);
                String departmentId = currentDepartmentDoc.getId();
                if (tutorDocDepartmentId.trim().equalsIgnoreCase(departmentId.trim())) {
                    isTutorAssignedToAnyDepartment = true;
                    break;
                }
            }
            return isTutorAssignedToAnyDepartment;
        } else {
            return false;//no departments,hence it is automatically not assigned.
        }
    }
}
