/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 9:11 PM
 */
@RestController
@RequestMapping(path = "/api/v1/department")
public class DepartmentController {

    @Autowired
    DepartmentDocRepository departmentDocRepository;
    @Autowired
    TutorDocRepository tutorDocRepository;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload createDepartment(@RequestBody DepartmentDoc departmentDoc) {
        if (departmentDoc.getDeptHODtutorId().trim() != "" || departmentDoc.getDeptHODtutorId() != null) {
            String deptProgrammeInitials = departmentDoc.getDeptProgrammeInitials();
            String deptType = departmentDoc.getDeptType();
            if (Objects.equals(deptType, DepartmentDoc.DEPARTMENT_TYPE_CORE)) {
                //once it's a core subject,we do not check for existence of programmeInitials
                if (tutorDocRepository.exists(departmentDoc.getDeptHODtutorId())) {
                    if (this.isSaveDepartmentDocInDbSuccessful(departmentDoc).containsKey(true)) {
                        return new SuccessfulOutgoingPayload(isSaveDepartmentDocInDbSuccessful(departmentDoc).get(true));
                    } else {
                        return new ErrorOutgoingPayload("Something went wrong,department was not created.");
                    }
                } else {
                    return new ErrorOutgoingPayload("H.O.D does not exist");
                }

            } else { //else automatically,we treat it as an elective subject,hence we check the progrmmeInitials
                DepartmentDoc departmentDocWithThisProgrammeInitials = departmentDocRepository.findByDeptProgrammeInitials(deptProgrammeInitials);
                if (departmentDocWithThisProgrammeInitials != null) {
                    return new ErrorOutgoingPayload("ProgrammeGroup Has Been set already to a Different Department");
                } else {
                    if (tutorDocRepository.exists(departmentDoc.getDeptHODtutorId())) {
                        if (this.isSaveDepartmentDocInDbSuccessful(departmentDoc).containsKey(true)) {
                            return new SuccessfulOutgoingPayload(isSaveDepartmentDocInDbSuccessful(departmentDoc).get(true));
                        } else {
                            return new ErrorOutgoingPayload("Something went wrong,department was not created.");
                        }
                    } else {
                        return new ErrorOutgoingPayload("H.O.D does not exist");
                    }
                }
            }

        } else {
            return new ErrorOutgoingPayload("HOD for the Department must be set");
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "tutor/{departmentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload addTutorToDepartment(@PathVariable String departmentId, @RequestParam(value = "tutorId", required = true) String tutorId) {
        if (departmentDocRepository.exists(departmentId)) {
            if (tutorDocRepository.exists(tutorId)) {
                TutorDoc tutorDoc = tutorDocRepository.findOne(tutorId);
                tutorDoc.setDepartmentId(departmentId);
                TutorDoc tutorDocUpdatedWithDeptId = tutorDocRepository.save(tutorDoc);
                return new SuccessfulOutgoingPayload("Tutor Added to Department Successfully", tutorDocUpdatedWithDeptId);
            } else {
                return new ErrorOutgoingPayload("Tutor Does not exist");
            }
        } else {
            return new ErrorOutgoingPayload("Department does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "tutors/{departmentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload addTutorsToDepartment(@PathVariable String departmentId, @RequestBody List<String> tutorIds) {
        if (departmentDocRepository.exists(departmentId)) {
            //convert list to stream,filter by expression,collect the output and convert streams to a List
            tutorIds.removeIf((tutorId) -> !(tutorDocRepository.exists(tutorId)));
            if (tutorIds.size() != 0) {
                Iterable<TutorDoc> tutorDocsIterable = tutorDocRepository.findAll(tutorIds);
                List<TutorDoc> tutorDocs = StreamSupport.stream(tutorDocsIterable.spliterator(), false)
                        .collect(Collectors.toList());
                tutorDocs.forEach(
                        tutorDoc -> tutorDoc.setDepartmentId(departmentId)
                ); //set each tutorDoc's departmentId to the departmentId of department.
                List<TutorDoc> tutorDocsSaved = tutorDocRepository.save(tutorDocs);
                return new SuccessfulOutgoingPayload(tutorDocsSaved);
            } else {
                return new ErrorOutgoingPayload("Empty tutors.Please choose Tutors to add to Department.");
            }
        } else {
            return new ErrorOutgoingPayload("Department does not exist");
        }
    }

    /**
     * TODO DONE!! URGENT! DO NOT ALLOW USER TO UPDATE THE PROGRAMMEiNITIALS & PROGRAMMEsUBJECTcODEID LIST ON fRONTEND.!!
     *
     * @param id
     * @param departmentDoc
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload updateDepartment(@PathVariable String id,
                                             @RequestBody DepartmentDoc departmentDoc) {
        if (departmentDocRepository.exists(id)) {
            if (departmentDoc.getDeptHODtutorId().trim() != "" || departmentDoc.getDeptHODtutorId() != null) {
                departmentDoc.setId(id);
                DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
                return new SuccessfulOutgoingPayload(departmentDocSavedInDb);
            } else {
                return new ErrorOutgoingPayload("HOD for the Department must be set");
            }
        } else {
            return new ErrorOutgoingPayload("Department does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    private OutgoingPayload getAllDepartments() {
        List<DepartmentDoc> departmentDocs = departmentDocRepository.findAll();
        if (departmentDocs.size() == 0) {
            return new SuccessfulOutgoingPayload("Empty List", departmentDocs);
        } else {
            return new SuccessfulOutgoingPayload(departmentDocs);
        }
    }

    /**
     * TODO DONE! onDelete and onDeleteAll of department(s),retrieve hod id and set department id of hod tutor to null or empty
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    private OutgoingPayload deleteDepartment(@PathVariable String id) {
        if (departmentDocRepository.exists(id)) {
            List<TutorDoc> tutorDocs = tutorDocRepository.findByDepartmentId(id);
            tutorDocs.stream().forEach(
                    (tutorDocToRemoveDepartmentId -> {
                        tutorDocToRemoveDepartmentId.setDepartmentId("");//set each tutorDoc departmentId to empty
                        tutorDocToRemoveDepartmentId.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)); //set tutorSubjectandProgrammeCodesList to empty
                    })
            );

            //mongo behaves STRANGELY when the stream is saved back straight after making changes in the lambda
            List<TutorDoc> tutorDocsUpdated = tutorDocs.stream().collect(Collectors.toList());
            tutorDocRepository.save(tutorDocsUpdated);//now save back into db and delete department finally
            departmentDocRepository.delete(id);
            return new SuccessfulOutgoingPayload("Deleted Successfully");
        } else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    private OutgoingPayload deleteAllDepartments() {
        if (departmentDocRepository.findAll().isEmpty()) {
            return new ErrorOutgoingPayload("No departments to delete currently");
        } else {
            List<DepartmentDoc> departmentDocs = departmentDocRepository.findAll();
            List<TutorDoc> tutorDocsToRemoveDepartmentIds = new ArrayList<>();//find all the tutorDocs with this departmentId
            departmentDocs.forEach(
                    (departmentDoc) -> {
                        List<TutorDoc> tutorDocs = tutorDocRepository.findByDepartmentId(departmentDoc.getId());
                        tutorDocsToRemoveDepartmentIds.addAll(tutorDocs);
                    });
            tutorDocsToRemoveDepartmentIds.stream().forEach(
                    (tutorDocToRemoveDepartmentId -> {
                        tutorDocToRemoveDepartmentId.setDepartmentId("");//set each tutorDoc departmentId to empty
                        tutorDocToRemoveDepartmentId.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)); //set tutorSubjectandProgrammeCodesList to empty
                    })
            );
            tutorDocRepository.save(tutorDocsToRemoveDepartmentIds);//now save them back into db
            departmentDocRepository.deleteAll();
            return new SuccessfulOutgoingPayload("deleted");
        }
    }

    @RequestMapping(path = "/tutors/{departmentId}", method = RequestMethod.GET)
    private OutgoingPayload getAllTutorsByDepartmentId(@PathVariable String departmentId) {
        if (departmentDocRepository.exists(departmentId)) {
            List<TutorDoc> tutorDocs = tutorDocRepository.findByDepartmentId(departmentId);
            return new SuccessfulOutgoingPayload(tutorDocs);
        } else {
            return new ErrorOutgoingPayload("Id does not Exist");
        }
    }

    /**
     * NOTE!!!!!:DO NOT DELETE THE TUTOR ACTUALLY,JUST REMOVE HIM FROM DEPARTMENT BY SETTING departmentId to null or empty string=>"".
     *
     * @param departmentId
     * @param tutorId
     * @return
     */
    @RequestMapping(path = "/tutor/{departmentId}", method = RequestMethod.DELETE)
    private OutgoingPayload deleteTutorByDepartmentId(@PathVariable String departmentId, @RequestParam(value = "tutorId", required = true) String tutorId) {
        if (departmentDocRepository.exists(departmentId)) {
            if (tutorDocRepository.exists(tutorId)) {
                TutorDoc tutorDoc = tutorDocRepository.findOne(tutorId);
                tutorDoc.setDepartmentId("");
                tutorDoc.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)); //set tutorSubjectandProgrammeCodesList to empty
                TutorDoc tutorDocSaved = tutorDocRepository.save(tutorDoc);
                return new SuccessfulOutgoingPayload("Saved successfully", tutorDocSaved);
            } else {
                return new ErrorOutgoingPayload("Tutor does not exist");
            }
        } else {
            return new ErrorOutgoingPayload("Department does not Exist");
        }
    }

    @RequestMapping(path = "/tutors/{departmentId}", method = RequestMethod.DELETE)
    private OutgoingPayload deleteAllTutorsByDepartmentId(@PathVariable String departmentId) {
        if (departmentDocRepository.exists(departmentId)) {
            List<TutorDoc> tutorDocs = tutorDocRepository.findByDepartmentId(departmentId);
            if (tutorDocs.size() == 0) {
                return new ErrorOutgoingPayload("Nothing to delete");
            } else {
                tutorDocs.forEach((tutorDoc) -> {
                    tutorDoc.setDepartmentId("");
                    tutorDoc.setTutorSubjectsAndProgrammeCodesList(new ArrayList<>(0)); //set tutorSubjectandProgrammeCodesList to empty
                });
                List<TutorDoc> tutorDocsUpdated = tutorDocRepository.save(tutorDocs);
                return new SuccessfulOutgoingPayload(tutorDocsUpdated);
            }
        } else {
            return new ErrorOutgoingPayload("Department does not Exist");
        }
    }

    private Map<Boolean, DepartmentDoc> isSaveDepartmentDocInDbSuccessful(DepartmentDoc departmentDoc) {
        Map<Boolean, DepartmentDoc> booleanDepartmentDocMap = new HashMap<>();
        DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
        TutorDoc tutorDoc = tutorDocRepository.findOne(departmentDoc.getDeptHODtutorId());
        tutorDoc.setDepartmentId(departmentDocSavedInDb.getId());//update departmentId field of tutor to the department id
        TutorDoc tutorDocUpdatedWithDepartmentId = tutorDocRepository.save(tutorDoc);//update in db
        if (tutorDocUpdatedWithDepartmentId != null) {
            booleanDepartmentDocMap.put(true, departmentDocSavedInDb);
            return booleanDepartmentDocMap;
        } else {
            booleanDepartmentDocMap.put(false, null);
            return booleanDepartmentDocMap;
        }
    }

}
