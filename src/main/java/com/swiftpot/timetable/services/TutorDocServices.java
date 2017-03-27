/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.google.gson.Gson;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         19-Feb-17 @ 8:59 AM
 */
@Service
public class TutorDocServices {

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
    ProgrammeGroupDocServices programmeGroupDocServices;
    @Autowired
    Gson gson;


    /**
     * super Method to ensure that all the validations on the subjects and classes assigned to tutor are passed .<br>
     * 1st check => {@link TutorDocServices#isAllSubjectClassesActuallyOfferingEachSubjectSpecified(TutorDoc)}<br>
     * 2nd check => {@link TutorDocServices#isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDeptwITHSAMESUBJECT(TutorDoc)}<br>
     * 3rd check => {@link TutorDocServices#isTotalSubjectsAndClassesAssignedOKAccordingToTutorsMinAndMaxPeriodLoad(TutorDoc)}<br>
     *
     * @param tutorDoc
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> isEverythingOkWithClassesAndSubjectsAssignedToTutor(TutorDoc tutorDoc) throws Exception {
        Map<Boolean, String> booleanStringMap = this.isAllSubjectClassesActuallyOfferingEachSubjectSpecified(tutorDoc);
        if (booleanStringMap.containsKey(true)) {
            Map<Boolean, String> booleanStringMapIsTutorClassesNotAssignedAlready = this.isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDeptwITHSAMESUBJECT(tutorDoc);
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


    public Map<Boolean, String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDeptwITHSAMESUBJECT(TutorDoc tutorDoc) {
        Map<Boolean, String> isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept = new HashMap<>();
        String tutorDepartmentId = tutorDoc.getDepartmentId();
        List<TutorSubjectIdAndProgrammeCodesListObj> idAndProgrammeCodesListObjsList = tutorDoc.getTutorSubjectsAndProgrammeCodesList();
        for (TutorSubjectIdAndProgrammeCodesListObj idAndProgrammeCodesListObj : idAndProgrammeCodesListObjsList) {
            if (isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.containsKey(false)) {
                break;
            }
            for (String programmeCodeToBeCompared : idAndProgrammeCodesListObj.getTutorProgrammeCodesList()) {
                String subjectUniqueIdInDb = idAndProgrammeCodesListObj.getTutorSubjectId();
                if (this.isProgrammeCodeAlreadyAssignedInDepartment(tutorDepartmentId, subjectUniqueIdInDb, programmeCodeToBeCompared) == true) {
                    isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.put(false, subjectDocRepository.findOne(subjectUniqueIdInDb).getSubjectFullName() + " is already assigned to a tutor in "
                            + programmeCodeToBeCompared + " class");
                    break;
                }
            }
        }
        if (isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.containsKey(false)) {
            return isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept;
        } else {//isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDeptwITHSAMESUBJECT
            isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept.put(true, "Everything ok");
            return isEachClassAssignedToTutorNotAlreadyAssignedToADifferentTutorInDept;
        }
    }

    /**
     * make sure where this is called does not have empty tutors in department
     *
     * @param tutorDepartmentId
     * @param subjectUniqueIdInDb
     * @param programmeCodeToBeCompared
     * @return
     */
    protected boolean isProgrammeCodeAlreadyAssignedInDepartment(String tutorDepartmentId, String subjectUniqueIdInDb, String programmeCodeToBeCompared) throws NoSuchElementException {

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
                        if (tutorSubjectIdAndProgrammeCodesListObj.getTutorProgrammeCodesList().contains(programmeCodeToBeCompared) &&
                                (Objects.equals(tutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId(), subjectUniqueIdInDb))) {
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
                        programmeGroupDocServices.getProgrammeGroupsOfferingParticularSubject(currentTutorSubjectIdAndProgrammeCodesListObj.getTutorSubjectId());
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

}
