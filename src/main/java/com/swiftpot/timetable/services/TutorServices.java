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


    /**
     * super Method to ensure that all the validations on the subjects and classes assigned to tutor are passed .<br>
     * 1st check => {@link TutorServices#isAllSubjectClassesActuallyOfferingEachSubjectSpecified(TutorDoc)}<br>
     * 2nd check => {@link TutorServices#isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept(TutorDoc)}<br>
     * 3rd check => {@link TutorServices#isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad(TutorDoc)}<br>
     *
     * @param tutorDoc
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> isEverythingOkWithClassesAndSubjectsAssignedToTutor(TutorDoc tutorDoc) throws Exception {
        Map<Boolean, String> booleanStringMap = this.isAllSubjectClassesActuallyOfferingEachSubjectSpecified(tutorDoc);
        if (booleanStringMap.containsKey(true)) {
            Map<Boolean, String> booleanStringMapIsTutorClassesNotAssignedAlready = this.isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept(tutorDoc);
            if (booleanStringMapIsTutorClassesNotAssignedAlready.containsKey(true)) {
                Map<Boolean, String> booleanStringMapIsTotalSubjectsAndClassesAssignedToTutorOk =
                        this.isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad(tutorDoc);
                if (booleanStringMapIsTotalSubjectsAndClassesAssignedToTutorOk.containsKey(true)) {
                    return booleanStringMapIsTotalSubjectsAndClassesAssignedToTutorOk;
                } else {
                    return booleanStringMapIsTotalSubjectsAndClassesAssignedToTutorOk;
                }
            } else {
                return booleanStringMapIsTutorClassesNotAssignedAlready;
            }
        } else {
            return booleanStringMap;
        }
    }


    public Map<Boolean, String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept(TutorDoc tutorDoc) {
        Map<Boolean, String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept = new HashMap<>();
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
        } else {//isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept
            isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.put(true, "Everything ok");
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

    /**
     * check to see if assigned classes totalSubjectAllocationPeriods is >= tutor's {@link TutorDoc#minPeriodLoad} but <= {@link TutorDoc#maxPeriodLoad}
     *
     * @param tutorDoc
     * @return
     */
    public Map<Boolean, String> isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad(TutorDoc tutorDoc) {
        Map<Boolean, String> isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad = new HashMap<>();
        int minPeriodLoadOfTutor = tutorDoc.getMinPeriodLoad();
        int maxPeriodLoadOfTutor = tutorDoc.getMaxPeriodLoad();

        int totalPeriodLoadOfAssignedSubjectsAndClasses = this.getTotalSubjectAllocationForAllClassesAssignedToTutor(tutorDoc);

        if (totalPeriodLoadOfAssignedSubjectsAndClasses < minPeriodLoadOfTutor) {
            isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad.put
                    (false, "Total Classes Periods Assigned to Tutor Cannot Be Less Than " + tutorDoc.getFirstName() + " " + tutorDoc.getSurName() + " 's minimum periods of " + minPeriodLoadOfTutor);
        } else if (totalPeriodLoadOfAssignedSubjectsAndClasses > maxPeriodLoadOfTutor) {
            isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad.put
                    (false, "Total Classes Periods Assigned to Tutor Cannot Be More Than " + tutorDoc.getFirstName() + " " + tutorDoc.getSurName() + " 's maximum periods of " + maxPeriodLoadOfTutor);
        } else {
            isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad.put(true, "Everything ok.Good to Go!!");
        }
        return isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad;
    }

    /**
     * get total subjects allocation periods for all classes that have been assigned to tutor.
     *
     * @param tutorDoc
     * @return
     */
    int getTotalSubjectAllocationForAllClassesAssignedToTutor(TutorDoc tutorDoc) {
        int totalSubjectAllocationForAllClasses = 0;
        List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectsAndProgrammeCodesList = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
        int tutorSubjectsAndProgrammeCodesListSize = tutorSubjectsAndProgrammeCodesList.size();
        for (int i = 0; i < tutorSubjectsAndProgrammeCodesListSize; i++) {
            TutorSubjectIdAndProgrammeCodesListObj currentTutorSubjectIdAndProgrammeCodesListObj = tutorSubjectsAndProgrammeCodesList.get(i);
            List<String> tutorProgrammeCodesList = currentTutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList();
            String currentSubjectUniqueIdInDB = currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId();
            Map<Integer, Integer> yearGroupsAndTotalSubjectAllocationMap =
                    this.getYearGroupsPartakingInSubjectAndTheirTotalSubjectAllocations(currentSubjectUniqueIdInDB);

            for (int currentTutorProgrammeCodeIndex = 0; currentTutorProgrammeCodeIndex < tutorProgrammeCodesList.size(); currentTutorProgrammeCodeIndex++) {
                ProgrammeGroupDoc programmeGroupDoc =
                        programmeGroupDocRepository.findByProgrammeCode(tutorProgrammeCodesList.get(currentTutorProgrammeCodeIndex));
                int yearGroupOfProgrammeGroupDoc = programmeGroupDoc.getYearGroup();
                //now find the totalSubjectAllocation of This yearGroup and add it to #totalSubjectAllocationForAllClasses
                int totalSubjectAllocationForCurrentYearGroup = yearGroupsAndTotalSubjectAllocationMap.get(yearGroupOfProgrammeGroupDoc);
                System.out.println("Total subject allocation periods for " + yearGroupOfProgrammeGroupDoc + " =" + totalSubjectAllocationForCurrentYearGroup);
                totalSubjectAllocationForAllClasses = totalSubjectAllocationForAllClasses + yearGroupsAndTotalSubjectAllocationMap.get(yearGroupOfProgrammeGroupDoc);
            }

        }
        System.out.println("Total Periods Subject Allocation Being Returned is ===========================>>>>>>>>>>>>>>>>>>>>>" + totalSubjectAllocationForAllClasses);
        return totalSubjectAllocationForAllClasses;
    }

    /**
     * get all yearGroups partaking in a subject and their corresponding totalSubjectAllocation in a map
     *
     * @param subjectUniqueIdInDB
     * @return Map<Integer,Integer>
     */
    public Map<Integer, Integer> getYearGroupsPartakingInSubjectAndTheirTotalSubjectAllocations(String subjectUniqueIdInDB) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(subjectUniqueIdInDB);
        List<SubjectAllocationDoc> subjectAllocationDocs = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
        Map<Integer, Integer> yearGroupAndTotalSubjectAllocation = new HashMap<>();
        for (int i = 0; i < subjectAllocationDocs.size(); i++) {
            int currentYearGroup = subjectAllocationDocs.get(i).getYearGroup();
            int currentTotalSubjectAllocationForCurrentYearGroup = subjectAllocationDocs.get(i).getTotalSubjectAllocation();
            yearGroupAndTotalSubjectAllocation.put(currentYearGroup, currentTotalSubjectAllocationForCurrentYearGroup);
        }
        System.out.println("YearGroups And their Total subjectAllocation Partaking in subject =***********\n");
        for (int i = 0; i < yearGroupAndTotalSubjectAllocation.size(); i++) {
            System.out.println("\t \t \t Year" + (i + 1) + ":" + yearGroupAndTotalSubjectAllocation.get(i + 1)); //i+1 because yearGroups are starting from 1.
        }
        return yearGroupAndTotalSubjectAllocation;
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
