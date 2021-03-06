/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.services.GeneralAbstractServices;
import com.swiftpot.timetable.services.ProgrammeGroupDocCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 2:26 PM
 */
@RestController
@RequestMapping(path = "/api/v1/programmegroup")
public class ProgrammeGroupController {

    @Autowired
    ProgrammeGroupDocCreatorService programmeGroupDocCreatorService;
    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    GeneralAbstractServices generalAbstractServices;

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload createProgrammeGroup(@RequestBody List<ProgrammeGroupDoc> programmeGroupDocs) throws Exception {
        if (!programmeGroupDocs.isEmpty()) {
            List<ProgrammeGroupDoc> programmeGroupDocsWithProgrammeInitialsInCaps = convertAllProgrammeInitialsToUpperCase(programmeGroupDocs);
            String programmeInitials = programmeGroupDocsWithProgrammeInitialsInCaps.get(0).getProgrammeInitials();//assume all the programmeInitials are the same since that's how we are bringing it from UI
            List<ProgrammeGroupDoc> programmeGroupDocsInDbCurrentlyWithCurrentProgInitials = programmeGroupDocRepository.findByProgrammeInitials(programmeInitials);
            if ((!programmeGroupDocsInDbCurrentlyWithCurrentProgInitials.isEmpty()) ||
                    Objects.isNull(programmeGroupDocsInDbCurrentlyWithCurrentProgInitials)) {
                return new ErrorOutgoingPayload("Error Occured,A Programme with programmeInitials of " + programmeInitials + " exists already");
            } else {
                List<ProgrammeGroupDoc> programmeGroupDocsSaved =
                        programmeGroupDocCreatorService.createProgramGroupDocWithConstraintsCateredForAndSave(programmeGroupDocsWithProgrammeInitialsInCaps);
                if (Objects.isNull(programmeGroupDocsSaved) || (programmeGroupDocsSaved.isEmpty())) {
                    return new ErrorOutgoingPayload("Error Occured,could not create ProgrammeGroup");
                } else {
                    return new SuccessfulOutgoingPayload(programmeGroupDocsSaved);
                }
            }
        } else {
            return new ErrorOutgoingPayload("Programme List is Empty");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    private OutgoingPayload getProgrammeGroup(@PathVariable String id) {
        if (programmeGroupDocRepository.exists(id)) {
            return new SuccessfulOutgoingPayload(programmeGroupDocRepository.findOne(id));
        } else {
            return new ErrorOutgoingPayload("ProgrammeGroup Id does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private OutgoingPayload getProgrammeGroupByYearGroupNumber(@RequestParam(value = "yearGroupNo", required = true) int yearGroupNo) {
        List<ProgrammeGroupDoc> programmeGroupDocs = programmeGroupDocRepository.findByYearGroup(yearGroupNo);
        if (Objects.isNull(programmeGroupDocs)) {
            return new ErrorOutgoingPayload("Yeargroup number does not exist");
        } else {
            return new SuccessfulOutgoingPayload(programmeGroupDocs);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    private OutgoingPayload getAllProgrammeGroups() {
        List<ProgrammeGroupDoc> programmeGroupDocs = programmeGroupDocRepository.findAll();
        if (Objects.isNull(programmeGroupDocs)) {
            return new ErrorOutgoingPayload();
        } else {
            return new SuccessfulOutgoingPayload(programmeGroupDocs);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    private OutgoingPayload deleteProgrammeGroup(@PathVariable String id) {
        if (programmeGroupDocRepository.exists(id)) {
            programmeGroupDocRepository.delete(id);
            generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
            return new SuccessfulOutgoingPayload("Deleted Successfully!");
        } else {
            return new ErrorOutgoingPayload("Programme Group id does not exist");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    private OutgoingPayload updateProgrammeGroup(@PathVariable String id,
                                                 @RequestBody ProgrammeGroupDoc programmeGroupDoc) {
        if (programmeGroupDocRepository.exists(id)) {
            programmeGroupDoc.setProgrammeInitials(programmeGroupDoc.getProgrammeInitials().toUpperCase());
            programmeGroupDoc.setId(id);
            ProgrammeGroupDoc programmeGroupDocUpdated = programmeGroupDocRepository.save(programmeGroupDoc);
            generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
            return new SuccessfulOutgoingPayload(programmeGroupDocUpdated);
        } else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }

    private List<ProgrammeGroupDoc> convertAllProgrammeInitialsToUpperCase(List<ProgrammeGroupDoc> programmeGroupDocs) {
        programmeGroupDocs.forEach(programmeGroupDoc -> programmeGroupDoc.setProgrammeInitials(programmeGroupDoc.getProgrammeInitials().toUpperCase()));
        programmeGroupDocs.forEach(programmeGroupDoc -> System.out.println("ProgrammeInitials in caps =" + programmeGroupDoc.getProgrammeInitials()));
        return programmeGroupDocs;
    }
}
