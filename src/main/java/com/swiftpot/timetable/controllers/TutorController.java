package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 9:29 PM
 */
@RestController
@RequestMapping(path = "/tutor")
public class TutorController {

    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    DepartmentDocRepository departmentDocRepository;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload createTutor(@RequestBody TutorDoc tutorDoc) {
        TutorDoc tutorDocSavedInDb = tutorDocRepository.save(tutorDoc);
        if (Objects.nonNull(tutorDocSavedInDb)) {
            return new SuccessfulOutgoingPayload(tutorDocSavedInDb);
        } else {
            return new ErrorOutgoingPayload();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    private OutgoingPayload getTutor(@PathVariable String id) {
        if (tutorDocRepository.exists(id)) {
            TutorDoc tutorDocFromDb = tutorDocRepository.findOne(id);
            return new SuccessfulOutgoingPayload(tutorDocFromDb);
        } else {
            return new ErrorOutgoingPayload("Id does not Exist");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    private OutgoingPayload getAllTutors() {
        List<TutorDoc> tutorDocs = tutorDocRepository.findAll();
        if (Objects.nonNull(tutorDocs)) {
            return new SuccessfulOutgoingPayload(tutorDocs);
        } else {
            return new ErrorOutgoingPayload();
        }
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload updateTutor(@PathVariable String id,
                                        @RequestBody TutorDoc tutorDoc) {
        if (tutorDocRepository.exists(id)) {
            tutorDoc.setId(id);
            TutorDoc tutorDocUpdated = tutorDocRepository.save(tutorDoc);
            return new SuccessfulOutgoingPayload(tutorDocUpdated);
        } else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }

    @RequestMapping(path="/{id}",method = RequestMethod.DELETE)
    private OutgoingPayload deleteTutor(@PathVariable String id) {
        if (tutorDocRepository.exists(id)) {
            tutorDocRepository.delete(id);
            return new SuccessfulOutgoingPayload("Deleted Successfully");
        }else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    private OutgoingPayload deleteAllTutors() {
        if (tutorDocRepository.count() > 1) {
            tutorDocRepository.deleteAll();
            return new SuccessfulOutgoingPayload("Deleted Successfully");
        }else {
            return new ErrorOutgoingPayload("No Tutors To Delete Currently");
        }
    }
}
