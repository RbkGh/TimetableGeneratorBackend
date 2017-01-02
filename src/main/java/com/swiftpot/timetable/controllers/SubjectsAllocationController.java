package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 6:55 PM
 */
@RequestMapping(path = "/subject/allocation")
public class SubjectsAllocationController {

    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload updateSubjectAllocation(@RequestParam String id,
                                                   @RequestBody SubjectAllocationDoc subjectAllocationDoc) {
        if (subjectAllocationDocRepository.exists(id)) {
            subjectAllocationDoc.setId(id);
            return new SuccessfulOutgoingPayload(subjectAllocationDocRepository.save(subjectAllocationDoc));
        } else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }
}
