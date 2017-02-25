package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         24-Feb-17 @ 10:01 PM
 */
public class DepartmentDocServices {

    @Autowired
    DepartmentDocRepository departmentDocRepository;

    /**
     * get department that subject belongs to,if it does not belong to any department , {@link NoSuchElementException} will be thrown.
     *
     * @param subjectUniqueIdInDB string of type {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id SubjectDoc.id}
     * @return
     * @throws NoSuchElementException
     */
    public DepartmentDoc getDepartmentThatSpecificSubjectBelongsTo(String subjectUniqueIdInDB) throws NoSuchElementException {
        List<DepartmentDoc> departmentDocsAllInDb = departmentDocRepository.findAll();
        DepartmentDoc departmentDoc = null;//set to null,until it is found and it is no more null. then return it,otherwise,throw exception.
        if (!departmentDocsAllInDb.isEmpty()) {
            int departmentDocsAllInDbLength = departmentDocsAllInDb.size();
            for (int i = 0; i < departmentDocsAllInDbLength; i++) {
                if (departmentDoc != null) {
                    break;
                }
                DepartmentDoc currentDepartmentDoc = departmentDocsAllInDb.get(i);
                List<String> subjectsDocIdList = currentDepartmentDoc.getProgrammeSubjectsDocIdList();
                if (!subjectsDocIdList.isEmpty()) {
                    for (int iCurrentSubjectDocIdIndex = 0; iCurrentSubjectDocIdIndex < subjectsDocIdList.size(); iCurrentSubjectDocIdIndex++) {
                        if (Objects.equals(subjectUniqueIdInDB, subjectsDocIdList.get(iCurrentSubjectDocIdIndex))) {
                            departmentDoc = currentDepartmentDoc;
                            break;
                        }
                    }
                }
            }
            //since the id is not empty,we can fetch it in db and return it.
            if (departmentDoc != null) {
                System.out.println("Returned departmentDoc ===>" + departmentDoc.toString());
                return departmentDoc;
            } else {
                throw new NoSuchElementException("There is no department found for the subject with id of " + subjectUniqueIdInDB);
            }
        } else {
            throw new NoSuchElementException("There is no department found for the subject with id of " + subjectUniqueIdInDB);
        }
    }
}
