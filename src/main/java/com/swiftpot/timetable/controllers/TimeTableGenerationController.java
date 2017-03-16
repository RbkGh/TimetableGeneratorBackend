/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.sun.istack.internal.Nullable;
import com.swiftpot.timetable.command.TimeTableGenerationClient;
import com.swiftpot.timetable.model.*;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Mar-17 @ 2:56 PM
 */
@RestController
@RequestMapping(path = "/timetable")
public class TimeTableGenerationController {

    @Autowired
    private TimeTableGenerationClient timeTableGenerationClient;
    @Autowired
    private TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    private ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    private TutorDocRepository tutorDocRepository;
    @Autowired
    private SubjectDocRepository subjectDocRepository;
    @Autowired
    private TimeTableMainDocRepository timeTableMainDocRepository;


    /**
     * So tired that I wrote stupid code here!!
     *
     * @param timeTableGenerationRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload generateTimeTable(@RequestBody TimeTableGenerationRequest timeTableGenerationRequest) throws Exception {
        TimeTableMainDoc timeTableMainDoc = this.generateFullTimeTableObject(timeTableGenerationRequest);
        timeTableMainDocRepository.save(timeTableMainDoc);
        return new SuccessfulOutgoingPayload(timeTableMainDoc);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload getAllTimeTableInDb() throws Exception {
        List<TimeTableMainDoc> timeTableMainDocs = timeTableMainDocRepository.findAll();
        return new SuccessfulOutgoingPayload(timeTableMainDocs);
    }

    private TimeTableMainDoc generateFullTimeTableObject(TimeTableGenerationRequest timeTableGenerationRequest) throws Exception {
        //generate timetable and pick result from db.
        timeTableGenerationClient.generateTimeTable();

        //get list of all tutors personal timetable from database.
        List<TutorPersonalTimeTableDoc> tutorPersonalTimeTableDocsList =
                tutorPersonalTimeTableDocRepository.findAll();

        //get list of all programmeGroup personal timetable from database.
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocsList =
                programmeGroupPersonalTimeTableDocRepository.findAll();

        for (TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc : tutorPersonalTimeTableDocsList) {
            List<ProgrammeDay> programmeDays = tutorPersonalTimeTableDoc.getProgrammeDaysList();
            for (ProgrammeDay programmeDay : programmeDays) {
                List<PeriodOrLecture> periodOrLectureList = programmeDay.getPeriodList();
                for (PeriodOrLecture periodOrLecture : periodOrLectureList) {
                    if (Objects.nonNull(periodOrLecture.getSubjectUniqueIdInDb())) {
                        if (!periodOrLecture.getSubjectUniqueIdInDb().isEmpty()) {
                            @Nullable SubjectDoc subjectDoc = subjectDocRepository.findOne(periodOrLecture.getTutorUniqueId());
                            @Nullable TutorDoc tutorDoc = tutorDocRepository.findOne(periodOrLecture.getTutorUniqueId());
                            if (Objects.nonNull(subjectDoc)) {
                                periodOrLecture.setSubjectFullName(subjectDoc.getSubjectFullName());
                            }
                            if (Objects.nonNull(tutorDoc)) {
                                periodOrLecture.setTutorFullName(tutorDoc.getFirstName() + " " + tutorDoc.getSurName());
                            }
                        }
                    }
                }
            }
        }


        for (ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc : programmeGroupPersonalTimeTableDocsList) {
            List<ProgrammeDay> programmeDays = programmeGroupPersonalTimeTableDoc.getProgrammeDaysList();
            for (ProgrammeDay programmeDay : programmeDays) {
                List<PeriodOrLecture> periodOrLectureList = programmeDay.getPeriodList();
                for (PeriodOrLecture periodOrLecture : periodOrLectureList) {
                    if (Objects.nonNull(periodOrLecture.getSubjectUniqueIdInDb())) {
                        if (!periodOrLecture.getSubjectUniqueIdInDb().isEmpty()) {
                            @Nullable SubjectDoc subjectDoc = subjectDocRepository.findOne(periodOrLecture.getTutorUniqueId());
                            @Nullable TutorDoc tutorDoc = tutorDocRepository.findOne(periodOrLecture.getTutorUniqueId());
                            if (Objects.nonNull(subjectDoc)) {
                                periodOrLecture.setSubjectFullName(subjectDoc.getSubjectFullName());
                            }
                            if (Objects.nonNull(tutorDoc)) {
                                periodOrLecture.setTutorFullName(tutorDoc.getFirstName() + " " + tutorDoc.getSurName());
                            }
                        }
                    }
                }
            }
        }

        TimeTableMainDoc timeTableMainDoc = new TimeTableMainDoc();

        timeTableMainDoc.setTimeTableName(timeTableGenerationRequest.getTimeTableName());
        timeTableMainDoc.setYear(timeTableGenerationRequest.getYearGroup());
        timeTableMainDoc.setProgrammeGroupPersonalTimeTableDocs(programmeGroupPersonalTimeTableDocsList);
        timeTableMainDoc.setTutorPersonalTimeTableDocs(tutorPersonalTimeTableDocsList);

        return timeTableMainDoc;
    }
}
