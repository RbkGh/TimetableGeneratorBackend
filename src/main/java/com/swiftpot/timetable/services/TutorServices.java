package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         19-Feb-17 @ 8:59 AM
 */
@Service
public class TutorServices {

    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    SubjectDocRepository subjectDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    DepartmentDocRepository departmentDocRepository;
    @Autowired
    Gson gson;

    protected boolean isTutorAssignedSubjectsOK(TutorDoc tutorDocWithAssignedSubjectsToBeUpdated) {
        boolean isTutorAssignedSubjectsOK = false;
        tutorDocWithAssignedSubjectsToBeUpdated.getTutorSubjectsAndProgrammeCodesList();
        return isTutorAssignedSubjectsOK;
    }

    public Map<Boolean, String> isAllSubjectClassesActuallyOfferingEachSubjectSpecified(TutorDoc tutorDoc) throws Exception {
        boolean isTutorsAssignedSubjectsLessThanMinimumButLessOrEqualToTheMaxPeriods = false;
        Map<Boolean, String> isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap = new HashMap<Boolean, String>();
        int minPeriodsForTutor = tutorDoc.getMinPeriodLoad();
        int maxPeriodsForTutor = tutorDoc.getMaxPeriodLoad();

        List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjs = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
        if (!tutorSubjectIdAndProgrammeCodesListObjs.isEmpty()) {
            int tutorSubjectIdAndProgrammeCodesListObjsSize = tutorSubjectIdAndProgrammeCodesListObjs.size();
            for (int i = 0; i < tutorSubjectIdAndProgrammeCodesListObjsSize; i++) {
                TutorSubjectIdAndProgrammeCodesListObj currentTutorSubjectIdAndProgrammeCodesListObj = tutorSubjectIdAndProgrammeCodesListObjs.get(i);
                List<ProgrammeGroupDoc> allProgrammeGroupDocsOfSubject =
                        this.getProgrammeGroupsOfferingParticularSubject(currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId());
                List<String> allProgrammeGroupDocsProgrammeCodeStringsList = new ArrayList<>();
                for (int progGroups = 0; progGroups < allProgrammeGroupDocsOfSubject.size(); progGroups++) {
                    allProgrammeGroupDocsProgrammeCodeStringsList.add(allProgrammeGroupDocsOfSubject.get(progGroups).getProgrammeCode());
                }
                String currentSubjectUniqueIdInDb = currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId();
                List<String> currentProgrammeGroupsIdsToBeUpdated = currentTutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList();
                for (int iCurrPgGroupIdIndex = 0; iCurrPgGroupIdIndex < currentProgrammeGroupsIdsToBeUpdated.size(); iCurrPgGroupIdIndex++) {
                    String currentProgrammeCode = currentProgrammeGroupsIdsToBeUpdated.get(iCurrPgGroupIdIndex);
                    if (!allProgrammeGroupDocsProgrammeCodeStringsList.contains(currentProgrammeCode)) {
                        SubjectDoc subjectDocWithIncorrectProgrammeGroupSet = subjectDocRepository.findOne(currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId());
                        isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.put(false, currentProgrammeCode + "is not part of the classes offering " + subjectDocWithIncorrectProgrammeGroupSet.getSubjectFullName());
                        break;
                    }
                }

                if (isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.containsKey(false)) {
                    break;//break out as false is inside
                }
            }
            if (isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.containsKey(false)) {
                return isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap;//set already so return it.
            } else {
                isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap = new HashMap<Boolean, String>(); //set new one to ensure there is not more than one boolean
                isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.put(true, "Everything ok");
                return isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap;
            }

        } else {
            isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap = new HashMap<Boolean, String>(); //set new one to ensure there is not more than one boolean
            isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.put(false, "Subjects cannot be empty");
            return isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap;
        }
    }

    /**
     * get all programmeGroups /classes that partake in all subjects in Department without duplicates
     *
     * @param departmentUniqueIdInDB
     * @return
     * @throws Exception
     */
    List<ProgrammeGroupDoc> getAllProgrammeGroupDocsThatPartakeInAllSubjectsInDeptWithoutDuplicates(String departmentUniqueIdInDB) throws Exception {
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

    List<Integer> getYearGroupsPartakingInSubject(String subjectUniqueIdInDB) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
        List<SubjectAllocationDoc> subjectAllocationDocs = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
        List<Integer> yearGroupsPartakingInSubject = new ArrayList<>();
        for (int i = 0; i < subjectAllocationDocs.size(); i++) {
            yearGroupsPartakingInSubject.add(subjectAllocationDocs.get(i).getYearGroup());
        }
        System.out.println("YearGroups Partaking in subject =" + Arrays.toString(yearGroupsPartakingInSubject.toArray()));
        return yearGroupsPartakingInSubject;
    }

    int getTotalPeriodsForSubject(String subjectUniqueIdInDB) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
        List<SubjectAllocationDoc> subjectAllocationDocs = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
        int totalPeriodsForSubjectAccrossAllYearGroups = 0;
        for (int i = 0; i < subjectAllocationDocs.size(); i++) {
            totalPeriodsForSubjectAccrossAllYearGroups = totalPeriodsForSubjectAccrossAllYearGroups + subjectAllocationDocs.get(i).getTotalSubjectAllocation();
        }
        System.out.println("Total Number of periods for " + subjectDoc.getSubjectFullName() + " =" + totalPeriodsForSubjectAccrossAllYearGroups);
        return totalPeriodsForSubjectAccrossAllYearGroups;
    }

    List<ProgrammeGroupDoc> getProgrammeGroupsOfferingParticularSubject(String subjectUniqueIdInDB) throws Exception {
        if (subjectDocRepository.exists(subjectUniqueIdInDB)) {
            SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
            List<Integer> yearGroupsOfferingCourse = subjectDoc.getSubjectYearGroupList();
            DepartmentDoc departmentDoc = getDepartmentThatSpecificSubjectBelongsTo(subjectUniqueIdInDB);
            //now fetch all the programmeGroups in department that this subject Belong to.
            String departmentProgrammeInitials = departmentDoc.getDeptProgrammeInitials();
            //now get all classes according to the programmeInitials of the department
            List<ProgrammeGroupDoc> programmeGroupDocsInDepartment = programmeGroupDocRepository.findByProgrammeInitials(departmentProgrammeInitials);
            return programmeGroupDocsInDepartment;
        } else {
            throw new NoSuchElementException("Subject with id " + subjectUniqueIdInDB + " does not exist");
        }
    }

    /**
     * get department that subject belongs to,if it does not belong to any department , {@link NoSuchElementException} will be thrown.
     *
     * @param subjectUniqueIdInDB
     * @return
     * @throws NoSuchElementException
     */
    DepartmentDoc getDepartmentThatSpecificSubjectBelongsTo(String subjectUniqueIdInDB) throws NoSuchElementException {
        List<DepartmentDoc> departmentDocsAllInDb = departmentDocRepository.findAll();
        String departmentDocIdForSubjectUniqueIdInDb = "";//set to empty,until it is found and it is no more empty. then return it,otherwise,throw exception.
        if (!departmentDocsAllInDb.isEmpty()) {
            int departmentDocsAllInDbLength = departmentDocsAllInDb.size();
            for (int i = 0; i < departmentDocsAllInDbLength; i++) {
                DepartmentDoc currentDepartmentDoc = departmentDocsAllInDb.get(i);
                List<String> subjectsDocIdList = currentDepartmentDoc.getProgrammeSubjectsDocIdList();
                if (!subjectsDocIdList.isEmpty()) {
                    for (int iCurrentSubjectDocIdIndex = 0; iCurrentSubjectDocIdIndex < subjectsDocIdList.size(); iCurrentSubjectDocIdIndex++) {
                        if (Objects.equals(subjectUniqueIdInDB, subjectsDocIdList.get(iCurrentSubjectDocIdIndex))) {
                            departmentDocIdForSubjectUniqueIdInDb = subjectsDocIdList.get(iCurrentSubjectDocIdIndex);
                            break;
                        }
                    }
                }
                if (!departmentDocIdForSubjectUniqueIdInDb.isEmpty()) {
                    break;
                }
            }
            //since the id is not empty,we can fetch it in db and return it.
            if (!departmentDocIdForSubjectUniqueIdInDb.isEmpty()) {
                DepartmentDoc departmentDoc = departmentDocRepository.findOne(departmentDocIdForSubjectUniqueIdInDb);
                return departmentDoc;
            } else {
                throw new NoSuchElementException("There is no department found for the subject with id of " + subjectUniqueIdInDB);
            }
        } else {
            throw new NoSuchElementException("There is no department found for the subject with id of " + subjectUniqueIdInDB);
        }
    }
}
