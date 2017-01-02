package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 1:48 PM
 */
@RequestMapping(path = "/subject")
public class SubjectsController {

    @Autowired
    SubjectDocRepository subjectDocRepository;

    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload createSubjectDoc(@RequestBody SubjectDoc subjectDoc) {
        SubjectDoc subjectDocSaved = subjectDocRepository.save(subjectDoc);
        if (Objects.isNull(subjectDocSaved)) {
            return new ErrorOutgoingPayload();
        }else{
            return new SuccessfulOutgoingPayload(subjectDocSaved);
        }
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload getSubjectDoc(@PathVariable String id) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(id);
        if (Objects.isNull(subjectDoc)) {
            return new ErrorOutgoingPayload();
        }else{
            return new SuccessfulOutgoingPayload(subjectDoc);
        }
    }
}
