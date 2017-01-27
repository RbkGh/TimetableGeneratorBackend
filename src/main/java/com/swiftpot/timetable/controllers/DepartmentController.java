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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 9:11 PM
 */
@RestController
@RequestMapping(path = "/department")
public class DepartmentController {

    @Autowired
    DepartmentDocRepository departmentDocRepository;
    @Autowired
    TutorDocRepository tutorDocRepository;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload createDepartment(@RequestBody DepartmentDoc departmentDoc) {
        if (departmentDoc.getDeptHODdeputyTutorId() != "" || departmentDoc.getDeptHODdeputyTutorId() != null) {
            if (tutorDocRepository.exists(departmentDoc.getDeptHODtutorId())) {
                DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
                TutorDoc tutorDoc = tutorDocRepository.findOne(departmentDoc.getDeptHODtutorId());
                tutorDoc.setDepartmentId(departmentDocSavedInDb.getId());//update departmentId field of tutor to the department id
                TutorDoc tutorDocUpdatedWithDepartmentId = tutorDocRepository.save(tutorDoc);//update in db
                return new SuccessfulOutgoingPayload(departmentDocSavedInDb);
            } else {
                return new ErrorOutgoingPayload("H.O.D does not exist");
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
                return new SuccessfulOutgoingPayload(tutorDocs);
            } else {
                return new ErrorOutgoingPayload("None of the Tutors existed");
            }
        } else {
            return new ErrorOutgoingPayload("Department does not exist");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload updateDepartment(@PathVariable String id,
                                             @RequestBody DepartmentDoc departmentDoc) {
        if (departmentDocRepository.exists(id)) {
            if (departmentDoc.getDeptHODdeputyTutorId() != "" || departmentDoc.getDeptHODdeputyTutorId() != null) {
                departmentDoc.setId(id);
                DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
                return new SuccessfulOutgoingPayload(departmentDocSavedInDb);
            } else {
                return new ErrorOutgoingPayload("HOD for the Department must be set");
            }
        } else {
            return new ErrorOutgoingPayload();
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
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    private OutgoingPayload deleteDepartment(@PathVariable String id) {
        if (departmentDocRepository.exists(id)) {
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
                tutorDoc.setDepartmentId(null);
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
                    tutorDoc.setDepartmentId(null);
                });
                List<TutorDoc> tutorDocsUpdated = tutorDocRepository.save(tutorDocs);
                return new SuccessfulOutgoingPayload(tutorDocsUpdated);
            }
        } else {
            return new ErrorOutgoingPayload("Department does not Exist");
        }
    }

}
