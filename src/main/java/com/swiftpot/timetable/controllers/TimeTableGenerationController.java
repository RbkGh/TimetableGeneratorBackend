/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.sun.istack.Nullable;
import com.swiftpot.timetable.command.TimeTableGenerationClient;
import com.swiftpot.timetable.exception.NoPeriodsFoundInProgrammeDaysThatSatisfiesElectiveTutorTimeTableException;
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
    @Autowired
    TotalNumberOfTimesTutorSubjectWasUnallocatedDocRepository totalNumberOfTimesTutorSubjectWasUnallocatedDocRepository;

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
            return new SuccessfulOutgoingPayload(timeTableMainDoc);
        } catch (NoPeriodsFoundInProgrammeDaysThatSatisfiesElectiveTutorTimeTableException e) {
            return new ErrorOutgoingPayload("Marshmallow Error,Please Try Again.");
        } catch (PracticalSubjectForDayNotFoundException e) {
            return new ErrorOutgoingPayload("Practical Subject For Day Not found.Please Check to ensure all input is fully correct and try again.");
        } catch (Exception e) {
            return new ErrorOutgoingPayload("Serious Error Occured.Ensure all input is correct and Try Again.");
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private OutgoingPayload getAllTimeTableInDb() throws Exception {
        List<TimeTableMainDoc> timeTableMainDocs = timeTableMainDocRepository.findAll();
        return new SuccessfulOutgoingPayload(timeTableMainDocs);
    }

    private TimeTableMainDoc generateFullTimeTableObject(TimeTableGenerationRequest timeTableGenerationRequest) throws PracticalSubjectForDayNotFoundException, NoPeriodsFoundInProgrammeDaysThatSatisfiesElectiveTutorTimeTableException, Exception {

        TotalNumberOfTimesTutorSubjectWasUnallocatedDoc totalNumberOfTimesTutorSubjectWasUnallocatedDocUpdated =
                new TotalNumberOfTimesTutorSubjectWasUnallocatedDoc(); //new instance all together because of some bug in mongo or my db driver,not really sure,so i just have to do this to save my document in peace!!
        totalNumberOfTimesTutorSubjectWasUnallocatedDocUpdated.setId(businessLogicConfigurationProperties.TOTAL_NUMBER_OF_UNALLOCATED_SUBJECTS_ID_FOR_SAVING_INTO_DB);
        totalNumberOfTimesTutorSubjectWasUnallocatedDocUpdated.setTotalNumberOfTimesTutorSubjectWasUnallocatedBestValue
                (25000); //set to absurd value in the beginning
        totalNumberOfTimesTutorSubjectWasUnallocatedDocUpdated.setTotalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue(0);//set to 0,so that if it remains 0 after generation,it means the perfect solution was found

        totalNumberOfTimesTutorSubjectWasUnallocatedDocRepository.save(totalNumberOfTimesTutorSubjectWasUnallocatedDocUpdated);


        int numberOfTimeTableGenerationRetriesFromFile = Integer.valueOf
                (businessLogicConfigurationProperties.NUMBER_OF_TIMES_TO_GENERATE_IF_MATCH_IS_NOT_FOUND);

        int numberOfTimeTableGenerationRetries = 0;

        while (!Objects.equals(numberOfTimeTableGenerationRetries, numberOfTimeTableGenerationRetriesFromFile)) {
            numberOfTimeTableGenerationRetries += 1;
            //generate timetable and pick result from db.
            timeTableGenerationClient.generateTimeTable();
            TotalNumberOfTimesTutorSubjectWasUnallocatedDoc totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterGeneration =
                    totalNumberOfTimesTutorSubjectWasUnallocatedDocRepository.findOne(businessLogicConfigurationProperties.TOTAL_NUMBER_OF_UNALLOCATED_SUBJECTS_ID_FOR_SAVING_INTO_DB);
            int currentValue = totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterGeneration.getTotalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue();
            int bestValue = totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterGeneration.getTotalNumberOfTimesTutorSubjectWasUnallocatedBestValue();

            if (Objects.equals
                    (currentValue, 0)) {
                logger.info("This is the best and PERFECT SOLUTION TO BE EVER GENERATED.tHIS IS DIAMOND!!!!!!!!!!!!!!POOOF POOOOOF POOOOOF!!!!!!!!!!!!!");
                return this.saveAndReturnTimeTableMainDoc(timeTableGenerationRequest);
                //BREAK BECAUSE WE FOUND THE PERFECT SOLUTION,THERE WAS NO SUBJECT UNALLOCATED FOR ANY TUTOR!!!!,THIS IS DIAMOND
            } else if (currentValue < bestValue) {
                TotalNumberOfTimesTutorSubjectWasUnallocatedDoc totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterBetterAlternative =
                        new TotalNumberOfTimesTutorSubjectWasUnallocatedDoc();
                totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterBetterAlternative.setId(totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterGeneration.getId());
                totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterBetterAlternative.setTotalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue(currentValue);
                totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterBetterAlternative.setTotalNumberOfTimesTutorSubjectWasUnallocatedBestValue(currentValue);

                totalNumberOfTimesTutorSubjectWasUnallocatedDocRepository.save(totalNumberOfTimesTutorSubjectWasUnallocatedDocAfterBetterAlternative);
                //save and overwrite currentValue
                this.saveAndReturnTimeTableMainDoc(timeTableGenerationRequest);
            }

        }
        return this.returnAlreadySavedTimeTableMainDoc(timeTableGenerationRequest);
    }

    /**
     * return this when we are really sure that there has been a final save of timeTAblemainDoc already
     *
     * @param timeTableGenerationRequest
     * @return
     */
    TimeTableMainDoc returnAlreadySavedTimeTableMainDoc(TimeTableGenerationRequest timeTableGenerationRequest) {
        return
                timeTableMainDocRepository.findByYearAndTimeTableName
                        (timeTableGenerationRequest.getYearGroup(), timeTableGenerationRequest.getTimeTableName());
    }

    /**
     * save and return saved timetableMain Doc
     *
     * @param timeTableGenerationRequest
     * @return {@link TimeTableMainDoc}
     */
    TimeTableMainDoc saveAndReturnTimeTableMainDoc(TimeTableGenerationRequest timeTableGenerationRequest) {
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


        TimeTableMainDoc timeTableMainDocExistingAlready =
                timeTableMainDocRepository.findByYearAndTimeTableName
                        (timeTableGenerationRequest.getYearGroup(), timeTableGenerationRequest.getTimeTableName());
        TimeTableMainDoc timeTableMainDocFinallySaved = null;
        if (Objects.isNull(timeTableMainDocExistingAlready)) {
            timeTableMainDocFinallySaved = timeTableMainDocRepository.save(timeTableMainDoc);
        } else {
            timeTableMainDoc.setId(timeTableMainDocExistingAlready.getId());
            timeTableMainDocFinallySaved = timeTableMainDocRepository.save(timeTableMainDoc);
        }
        return timeTableMainDocFinallySaved;
    }
}
