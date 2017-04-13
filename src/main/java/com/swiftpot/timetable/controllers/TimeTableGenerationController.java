/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.sun.istack.Nullable;
import com.swiftpot.timetable.command.TimeTableGenerationClient;
import com.swiftpot.timetable.exception.NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException;
import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.model.*;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    @Autowired
    private BusinessLogicConfigurationProperties businessLogicConfigurationProperties;

    public static final Logger logger = LogManager.getLogger();

    /**
     * So tired that I wrote stupid code here!!
     *
     * @param timeTableGenerationRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload generateTimeTable(@RequestBody TimeTableGenerationRequest timeTableGenerationRequest) throws Exception {
        try {
            TimeTableMainDoc timeTableMainDoc = this.generateFullTimeTableObject(timeTableGenerationRequest);
            timeTableMainDocRepository.save(timeTableMainDoc);
            return new SuccessfulOutgoingPayload(timeTableMainDoc);
        } catch (NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException e) {
            return new ErrorOutgoingPayload("Could not generate timetable.Sad!.Please Try Again.");
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload getAllTimeTableInDb() throws Exception {
        List<TimeTableMainDoc> timeTableMainDocs = timeTableMainDocRepository.findAll();
        return new SuccessfulOutgoingPayload(timeTableMainDocs);
    }

    private TimeTableMainDoc generateFullTimeTableObject(TimeTableGenerationRequest timeTableGenerationRequest) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException, Exception {
        //generate timetable and pick result from db.
        try {
            timeTableGenerationClient.generateTimeTable();
        } catch (NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException e) {
            int numberOfTimeTableGenerationRetriesFromFile = Integer.valueOf
                    (businessLogicConfigurationProperties.NUMBER_OF_TIMES_TO_GENERATE_IF_MATCH_IS_NOT_FOUND);

            int numberOfTimeTableGenerationRetries = 0;

            while (numberOfTimeTableGenerationRetries != numberOfTimeTableGenerationRetriesFromFile) {
                numberOfTimeTableGenerationRetries += 1;
                try {
                    timeTableGenerationClient.generateTimeTable();
                    logger.debug("Generated in " + numberOfTimeTableGenerationRetries + " different number of retries!");
                    break;//if there was no error thrown,we can break out of it.
                } catch (NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException ex) {
                    if (numberOfTimeTableGenerationRetries == numberOfTimeTableGenerationRetriesFromFile) {
                        logger.debug(ex + " \nNumber of timetable generation retries before unsuccessful = " + numberOfTimeTableGenerationRetries);
                        throw new NoPeriodsFoundInProgrammeDaysThatSatisfiesTutorTimeTableException("Timetable Generation Unsuccessful");
                    }
                }
            }
        }

        //get list of all tutors personal timetable from database.
        List<TutorPersonalTimeTableDoc> tutorPersonalTimeTableDocsList =
                tutorPersonalTimeTableDocRepository.findAll();

        //get list of all programmeGroup personal timetable from database.
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocsList =
                programmeGroupPersonalTimeTableDocRepository.findAll();

        for (TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc : tutorPersonalTimeTableDocsList) {
            TutorDoc tutorDoc = tutorDocRepository.findOne(tutorPersonalTimeTableDoc.getTutorUniqueIdInDb());
            tutorPersonalTimeTableDoc.setTutorDoc(tutorDoc); //set tutorDoc

            List<ProgrammeDay> programmeDays = tutorPersonalTimeTableDoc.getProgrammeDaysList();
            for (ProgrammeDay programmeDay : programmeDays) {
                List<PeriodOrLecture> periodOrLectureList = programmeDay.getPeriodList();
                for (PeriodOrLecture periodOrLecture : periodOrLectureList) {
                    if (Objects.nonNull(periodOrLecture.getSubjectUniqueIdInDb())) {
                        if (!periodOrLecture.getSubjectUniqueIdInDb().isEmpty()) {
                            @Nullable SubjectDoc subjectDoc = subjectDocRepository.findOne(periodOrLecture.getSubjectUniqueIdInDb());
                            if (Objects.nonNull(subjectDoc)) {
                                periodOrLecture.setSubjectFullName(subjectDoc.getSubjectFullName());
                            } else {
                                periodOrLecture.setSubjectFullName(periodOrLecture.getSubjectUniqueIdInDb());//at this point only class meetings and worship periods have null subjectDoc as those are not registered as actual subjects.
                            }
                            if (Objects.nonNull(tutorDoc)) {
                                periodOrLecture.setTutorFullName(tutorDoc.getFirstName() + " " + tutorDoc.getSurName());
                            } else {
                                periodOrLecture.setTutorFullName(periodOrLecture.getTutorUniqueId());//the tutor unique id and subjectUniqueId has been set with default values already.
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
                            @Nullable SubjectDoc subjectDoc = subjectDocRepository.findOne(periodOrLecture.getSubjectUniqueIdInDb());
                            @Nullable TutorDoc tutorDoc = tutorDocRepository.findOne(periodOrLecture.getTutorUniqueId());
                            if (Objects.nonNull(subjectDoc)) {
                                periodOrLecture.setSubjectFullName(subjectDoc.getSubjectFullName());
                            } else {
                                periodOrLecture.setSubjectFullName(periodOrLecture.getSubjectUniqueIdInDb());//at this point only class meetings and worship periods have null subjectDoc as those are not registered as actual subjects.
                            }
                            if (Objects.nonNull(tutorDoc)) {
                                periodOrLecture.setTutorFullName(tutorDoc.getFirstName() + " " + tutorDoc.getSurName());
                            } else {
                                periodOrLecture.setTutorFullName(periodOrLecture.getTutorUniqueId());//the tutor unique id and subjectUniqueId has been set with default values already.
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
