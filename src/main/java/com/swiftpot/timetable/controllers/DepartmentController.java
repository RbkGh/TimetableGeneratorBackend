package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload createDepartment(@RequestBody DepartmentDoc departmentDoc) {
        DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
        return new SuccessfulOutgoingPayload(departmentDocSavedInDb);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload updateDepartment(@PathVariable String id,
                                             @RequestBody DepartmentDoc departmentDoc) {
        if (departmentDocRepository.exists(id)) {
            departmentDoc.setId(id);
            DepartmentDoc departmentDocSavedInDb = departmentDocRepository.save(departmentDoc);
            return new SuccessfulOutgoingPayload(departmentDocSavedInDb);
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

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    private OutgoingPayload deleteDepartment(@PathVariable String id) {
        if (departmentDocRepository.exists(id)) {
            departmentDocRepository.delete(id);
            return new SuccessfulOutgoingPayload(null);
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
}
