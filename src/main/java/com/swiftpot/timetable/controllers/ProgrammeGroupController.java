package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.services.ProgrammeGroupDocCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 2:26 PM
 */
@RestController
@RequestMapping(path = "/programme")
public class ProgrammeGroupController {

    @Autowired
    ProgrammeGroupDocCreator programmeGroupDocCreator;

    @RequestMapping(path = "",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    private String createProgrammeGroup(@RequestBody ProgrammeGroupDoc programmeGroupDoc) throws Exception{
        programmeGroupDocCreator.createProgramGroupDocWithConstraintsCateredForAndSave(programmeGroupDoc);
        return "Hello";
    }
}
