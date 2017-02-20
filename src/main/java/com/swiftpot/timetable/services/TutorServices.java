package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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



    public Map<Boolean, String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept(TutorDoc tutorDoc) {
        Map<Boolean,String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept = new HashMap<>();
        String tutorDepartmentId = tutorDoc.getDepartmentId();
        List<TutorSubjectIdAndProgrammeCodesListObj> idAndProgrammeCodesListObjsList = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
        for (TutorSubjectIdAndProgrammeCodesListObj idAndProgrammeCodesListObj : idAndProgrammeCodesListObjsList) {
            if (isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.containsKey(false)) {
                break;
            }
            for (String programmeCodeToBeCompared : idAndProgrammeCodesListObj.getTutorProgrammeCodesList()) {
                if (this.isProgrammeCodeAlreadyAssignedInDepartment(tutorDepartmentId, programmeCodeToBeCompared) == true) {
                    isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.put(false, programmeCodeToBeCompared + " is already assigned to a tutor");
                    break;
                }
            }
        }
        if (isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.containsKey(false)) {
            return isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept;
        }else{//isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept
            isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.put(true,"Everything ok");
            return isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept;
        }
    }

    /**
     * make sure where this is called does not have empty tutors in department
     *
     * @param tutorDepartmentId
     * @param programmeCodeToBeCompared
     * @return
     */
    protected boolean isProgrammeCodeAlreadyAssignedInDepartment(String tutorDepartmentId, String programmeCodeToBeCompared) throws NoSuchElementException {

            boolean isProgrammeGroupAlreadyAssignedInDepartment = false;
            //get all department tutors
            List<TutorDoc> tutorDocsWithThisDepartmentIdList = tutorDocRepository.findByDepartmentId(tutorDepartmentId);
            if (tutorDocsWithThisDepartmentIdList.isEmpty()) {
                return false; //as the number of tutors is empty,which is highly unlikely,then we can return false
            } else {
                for (TutorDoc tutorDocWithThisDepartmentId : tutorDocsWithThisDepartmentIdList) {
                    if (!tutorDocWithThisDepartmentId.getTutorSubjectsAndProgrammeCodesList().isEmpty()) {
                        //if its true,breakout of loop and return the value,otherwise,continue scanning through.
                        if (isProgrammeGroupAlreadyAssignedInDepartment == true) {
                            break;
                        }
                        List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjList =
                                tutorDocWithThisDepartmentId.getTutorSubjectsAndProgrammeCodesList();
                        for (TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj : tutorSubjectIdAndProgrammeCodesListObjList) {
                            if (tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList().contains(programmeCodeToBeCompared)) {
                                isProgrammeGroupAlreadyAssignedInDepartment = true; //exists ,therefore return true by setting isProgrammeGroupAlreadyAssignedInDepartment to true and breaking out of loop
                                break;
                            }
                        }
                    }
                }
                return isProgrammeGroupAlreadyAssignedInDepartment;
            }
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
                    System.out.println("AllProgrammeGrooupDocs programme code to check against =" + String.join(",", allProgrammeGroupDocsProgrammeCodeStringsList));
                    System.out.println("Current ProgrammeGroupDoc programmeCode=" + currentProgrammeCode);
                    if (!allProgrammeGroupDocsProgrammeCodeStringsList.contains(currentProgrammeCode)) {
                        SubjectDoc subjectDocWithIncorrectProgrammeGroupSet = subjectDocRepository.findOne(currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId());
                        isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.put(false, currentProgrammeCode + " is not part of the classes offering " + subjectDocWithIncorrectProgrammeGroupSet.getSubjectFullName());
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
                isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap.put(true, "Everything ok");
                return isAllClassesSpecifiedOfferingSpecifiedSubjectHashMap;
            }

        } else {
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

    public List<Integer> getYearGroupsPartakingInSubject(String subjectUniqueIdInDB) {
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

    public List<ProgrammeGroupDoc> getProgrammeGroupsOfferingParticularSubject(String subjectUniqueIdInDB) throws Exception {
        if (subjectDocRepository.exists(subjectUniqueIdInDB)) {
            SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
            //List<Integer> yearGroupsOfferingCourse = subjectDoc.getSubjectYearGroupList();
            DepartmentDoc departmentDoc = this.getDepartmentThatSpecificSubjectBelongsTo(subjectUniqueIdInDB);
            System.out.println("DepartmentDoc ====>" + departmentDoc.toString());
            //now fetch all the programmeGroups in department that this subject Belong to.,if a core subject we will not search ProgrammeeGroups in dept using the programmeInitials of Dept
            List<ProgrammeGroupDoc> programmeGroupDocsInDepartment = this.getAllProgrammeGroupDocsOfParticularSubject(subjectUniqueIdInDB);
            System.out.println("ProgrammeGroups For Subject " + subjectUniqueIdInDB + " ==>>>::\n\n");
            programmeGroupDocsInDepartment.forEach(programmeGroupDoc -> System.out.println(programmeGroupDoc.toString() + "\n"));
            System.out.println("\n\n");
            return programmeGroupDocsInDepartment;
        } else {
            throw new NoSuchElementException("Subject with id " + subjectUniqueIdInDB + " does not exist");
        }
    }

    /**
     * use when we need to get all department programme group docs of particular subjectuniqueId passed in
     *
     * @param subjectUniqueId
     * @return
     */
    List<ProgrammeGroupDoc> getAllProgrammeGroupDocsOfParticularSubject(String subjectUniqueId) {
        Set<ProgrammeGroupDoc> finalProgrammeGroupDocsInDeptSet = new HashSet<>();
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueId);
        List<Integer> yearGroups = subjectDoc.getSubjectYearGroupList();
        for (Integer yearGroup : yearGroups) {
            List<ProgrammeGroupDoc> programmeGroupsForParticularYearGroup = programmeGroupDocRepository.findByYearGroup(yearGroup);
            //add each programmeGroupDoc ignoring duplicates and overriding toString and hashcode of ProgrammeGroupDoc class
            programmeGroupsForParticularYearGroup.forEach(finalProgrammeGroupDocsInDeptSet::add);
        }

        List<ProgrammeGroupDoc> finalProgrammeGroupDocsToReturn = finalProgrammeGroupDocsInDeptSet.stream().collect(Collectors.toList());//convert set to List
        return finalProgrammeGroupDocsToReturn;
    }


    /**
     * get department that subject belongs to,if it does not belong to any department , {@link NoSuchElementException} will be thrown.
     *
     * @param subjectUniqueIdInDB
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
