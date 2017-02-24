package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         24-Feb-17 @ 6:18 PM
 */
@Service
public class ProgrammeGroupDocServices {

    @Autowired
    DepartmentDocRepository departmentDocRepository;
    @Autowired
    TutorDocServices tutorDocServices;
    @Autowired
    SubjectDocRepository subjectDocRepository;

    /**
     * get all programmeGroups /classes that partake in all subjects in Department without duplicates
     *
     * @param departmentUniqueIdInDB
     * @return
     * @throws Exception
     */
    public List<ProgrammeGroupDoc> getAllProgrammeGroupDocsThatPartakeInAllSubjectsInDeptWithoutDuplicates(String departmentUniqueIdInDB) throws Exception {
        DepartmentDoc departmentDoc = departmentDocRepository.findOne(departmentUniqueIdInDB);
        List<String> subjectDocIdInDeptList = departmentDoc.getProgrammeSubjectsDocIdList();
        //Iterable<SubjectDoc> subjectDocsIterable = subjectDocRepository.findAll(subjectDocIdInDeptList);
        //List<SubjectDoc> subjectDocsList = StreamSupport.stream(subjectDocsIterable.spliterator(),false).collect(Collectors.toList());//convert iterable to list
        List<ProgrammeGroupDoc> listOfAllProgrammeGroupDocsTHatOfferSubjectInDept = new ArrayList<>();
        for (int i = 0; i < subjectDocIdInDeptList.size(); i++) {
            List<ProgrammeGroupDoc> programmeGroupDocs = this.getProgrammeGroupsOfferingParticularSubject(subjectDocIdInDeptList.get(i));
            listOfAllProgrammeGroupDocsTHatOfferSubjectInDept.addAll(programmeGroupDocs);
        }

        //now we need to filter duplicates because there is a high likelihood of duplicates as one programme may be offered by two or more classes,like english
        List<ProgrammeGroupDoc> programmeGroupDocsWithoutDuplicates =
                new ArrayList<>(new LinkedHashSet<>(listOfAllProgrammeGroupDocsTHatOfferSubjectInDept)); //remove duplicates in one line aftr overriding ProgrammeGroupDoc's hashCode and equals methods.
        return programmeGroupDocsWithoutDuplicates;
    }

    /**
     * get ProgrammeGroupDocs/Classes offering a particular subject ie. the the {@link SubjectDoc#id id} of the {@linkplain SubjectDoc subject} passed in as a parameter
     *
     * @param subjectUniqueIdInDB a {@link SubjectDoc#id} string
     * @return
     * @throws Exception
     */
    public List<ProgrammeGroupDoc> getProgrammeGroupsOfferingParticularSubject(String subjectUniqueIdInDB) throws Exception {
        if (subjectDocRepository.exists(subjectUniqueIdInDB)) {
            SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
            //List<Integer> yearGroupsOfferingCourse = subjectDoc.getSubjectYearGroupList();
            DepartmentDoc departmentDoc = tutorDocServices.getDepartmentThatSpecificSubjectBelongsTo(subjectUniqueIdInDB);
            System.out.println("DepartmentDoc ====>" + departmentDoc.toString());
            //now fetch all the programmeGroups in department that this subject Belong to.,if a core subject we will not search ProgrammeeGroups in dept using the programmeInitials of Dept
            List<ProgrammeGroupDoc> programmeGroupDocsInDepartment = tutorDocServices.getAllProgrammeGroupDocsOfParticularSubject(subjectUniqueIdInDB);
            System.out.println("ProgrammeGroups For Subject " + subjectUniqueIdInDB + " ==>>>::\n\n");
            programmeGroupDocsInDepartment.forEach(programmeGroupDoc -> System.out.println(programmeGroupDoc.toString() + "\n"));
            System.out.println("\n\n");
            return programmeGroupDocsInDepartment;
        } else {
            throw new NoSuchElementException("Subject with id " + subjectUniqueIdInDB + " does not exist");
        }
    }
}
