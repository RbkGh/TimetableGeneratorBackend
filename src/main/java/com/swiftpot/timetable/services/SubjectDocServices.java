package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         24-Feb-17 @ 6:45 PM
 */
@Service
public class SubjectDocServices {

    @Autowired
    SubjectDocRepository subjectDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;


    /**
     * get all total Periods For a {@link SubjectDoc subject} by summing all {@link List<SubjectAllocationDoc> subjectsAllocations} <br>
     * {@link SubjectAllocationDoc#totalSubjectAllocation totalSubjectAllocation} in each yearGroup
     *
     * @param subjectUniqueIdInDB {@link SubjectDoc#id id} of {@link SubjectDoc SubjectDoc}
     * @return
     */
    int getTotalPeriodsForSubjectConsideringAllYearGroups(String subjectUniqueIdInDB) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
        List<SubjectAllocationDoc> subjectAllocationDocs = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
        int totalPeriodsForSubjectAccrossAllYearGroups = 0;
        for (int i = 0; i < subjectAllocationDocs.size(); i++) {
            totalPeriodsForSubjectAccrossAllYearGroups = totalPeriodsForSubjectAccrossAllYearGroups + subjectAllocationDocs.get(i).getTotalSubjectAllocation();
        }
        System.out.println("Total Number of periods for " + subjectDoc.getSubjectFullName() + " =" + totalPeriodsForSubjectAccrossAllYearGroups);
        return totalPeriodsForSubjectAccrossAllYearGroups;
    }
}
