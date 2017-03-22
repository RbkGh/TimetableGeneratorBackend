/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.*;
import com.swiftpot.timetable.factory.TutorResponsibleForSubjectRetrieverFactory;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.*;
import com.swiftpot.timetable.repository.db.model.*;
import com.swiftpot.timetable.services.ProgrammeDayServices;
import com.swiftpot.timetable.services.ProgrammeGroupPersonalTimeTableDocServices;
import com.swiftpot.timetable.services.TutorPersonalTimeTableDocServices;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import com.swiftpot.timetable.util.ProgrammeDayHelperUtilDefaultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 12:39 PM
 */
@Component
public class TimeTableDefaultPeriodsAllocatorDefaultImpl implements TimeTableDefaultPeriodsAllocator {

    @Autowired
    TutorDocRepository tutorDocRepository;
    @Autowired
    SubjectDocRepository subjectDocRepository;
    @Autowired
    BusinessLogicConfigurationProperties businessLogicConfigurationProperties;
    @Autowired
    TutorResponsibleForSubjectRetrieverFactory tutorResponsibleForSubjectRetrieverFactory;
    @Autowired
    TutorSubjectAndProgrammeGroupCombinationDocRepository tutorSubjectAndProgrammeGroupCombinationDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    IProgrammeDayHelper iProgrammeDayHelper;
    @Autowired
    ProgrammeDayHelperUtilDefaultImpl programmeDayHelperUtilDefault;
    @Autowired
    TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    TutorPersonalTimeTableDocServices tutorPersonalTimeTableDocServices;
    @Autowired
    ProgrammeGroupPersonalTimeTableDocServices programmeGroupPersonalTimeTableDocServices;
    @Autowired
    ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    ProgrammeDayServices programmeDayServices;

    private static final String CLASS_MEETING_DAY_NUMBER_KEY = "CLASS_MEETING_DAYNUMBER";
    private static final String CLASS_MEETING_PERIOD_NUMBER_KEY = "CLASS_MEETING_PERIODNUMBER";

    private static final String CLASS_WORSHIP_DAY_NUMBER_KEY = "worshipDayNumber";
    private static final String CLASS_WORSHIP_PERIOD_NUMBER_KEY = "worshipPeriodNumber";

    /**
     * pass in non empty value
     */
    private static final String DUMMY_WORSHIP_PERIOD_SUBJECT_UNIQUE_ID = "WORSHIPPERIOD";
    /**
     * pass in non empty value
     */
    private static final String DUMMY_CLASSMEETING_PERIOD_SUBJECT_UNIQUE_ID = "CLASSMEEETINGPERIOD";
    /**
     * pass in non empty value
     */
    private static final String DUMMY_CLASSMEETING_TUTOR_UNIQUE_ID = "TUTORCLASSMEETING";
    /**
     * pass in non empty value
     */
    private static final String DUMMY_WORSHIP_TUTOR_UNIQUE_ID = "TUTORWORSHIPPERIOD";

    @Override
    public TimeTableSuperDoc allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) throws Exception {

        //use atomic reference to achieve higher throughput, which means higher application server performance.
        AtomicReference<TimeTableSuperDoc> timeTableSuperDocAfterWorshipPeriodsSet =
                new AtomicReference<>(this.allocateWorshipPeriodForAllProgrammeGroups(timeTableSuperDocWithInitialDefaultDataSet, DUMMY_WORSHIP_PERIOD_SUBJECT_UNIQUE_ID));
        AtomicReference<TimeTableSuperDoc> timeTableSuperDocAfterClassMeetingPeriodsSet =
                new AtomicReference<>(this.allocateClassMeetingPeriodForAllProgrammeGroups(timeTableSuperDocAfterWorshipPeriodsSet.get(), DUMMY_CLASSMEETING_PERIOD_SUBJECT_UNIQUE_ID));
        AtomicReference<TimeTableSuperDoc> finalTimeTableSuperDocAfterPracticalsPeriodsSetForEachProgrammeGroupRequiringIt =
                new AtomicReference<>(this.allocatePracticalsSubjectPeriodForAllProgrammeGroupsRequiringIt(timeTableSuperDocAfterClassMeetingPeriodsSet.get()));

        return finalTimeTableSuperDocAfterPracticalsPeriodsSetForEachProgrammeGroupRequiringIt.get();
    }

    /**
     * TODO FIND A WAY TO RANDOMIZE THE PROGRAMMEdAY FOR PRACTICALS FOR EACH PROGRAMMEgORUP
     *
     * @param timeTableSuperDoc
     * @return
     */
    public TimeTableSuperDoc allocatePracticalsSubjectPeriodForAllProgrammeGroupsRequiringIt(TimeTableSuperDoc timeTableSuperDoc) {
        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class);

        List<YearGroup> yearGroupList = timeTableSuperDocGeneratedFromString.getYearGroupsList();
        int numberOfYearGroups = yearGroupList.size();
        for (int currentNo = 0; currentNo < numberOfYearGroups; currentNo++) {
            //now we need to get all ProgrammeGroups in yearGroupList
            List<ProgrammeGroup> programmeGroupList = yearGroupList.get(currentNo).getProgrammeGroupList();
            int programmeYearNo = yearGroupList.get(currentNo).getYearNumber();
            int numberOfProgrammeGroups = programmeGroupList.size();
            for (int currentProgrammeGroupNo = 0; currentProgrammeGroupNo < numberOfProgrammeGroups; currentProgrammeGroupNo++) {
                //now we need to get all the ProgrammeDays as list
                List<ProgrammeDay> programmeDaysList = programmeGroupList.get(currentProgrammeGroupNo).getProgrammeDaysList();
                ProgrammeGroup currentProgrammeGroup = programmeGroupList.get(currentProgrammeGroupNo);
                if (currentProgrammeGroup.getIsProgrammeRequiringPracticalsClassroom() == true) {
                    //set Practicals for all programmes that have practicals and set it to the same programme days list
                    List<ProgrammeDay> newlyCreatedProgrammeDaysList = this.allocatePracticalsPeriodsForProgrammeGroupRequiringIt(currentProgrammeGroup,
                            programmeDaysList,
                            programmeYearNo);
                    programmeGroupList.get(currentProgrammeGroupNo).setProgrammeDaysList(newlyCreatedProgrammeDaysList);
                    yearGroupList.get(currentNo).setProgrammeGroupList(programmeGroupList);
                    timeTableSuperDocGeneratedFromString.setYearGroupsList(yearGroupList);
                }
            }
        }

        return timeTableSuperDocGeneratedFromString;

    }

    /**
     * TODO find a way to inject subjectUniqueIdInDb,=>fix by utilizing optional in java so that a default of "None" or "" .
     * TODO DECIDE WETHER TO POPULATE WORKSHOP WITH SUBJECTS FIRST OR ALLOCATE WORSHIP PERIODS FIRST
     *
     * @param timeTableSuperDoc
     * @param subjectUnniqueIdInDbDummyValue Pass in a dummy value,same value is used for tutorUniqueId,just a dummy value pass in Empty "" for short
     * @return TimeTableSuperDoc
     */
    public TimeTableSuperDoc allocateWorshipPeriodForAllProgrammeGroups(TimeTableSuperDoc timeTableSuperDoc, String subjectUnniqueIdInDbDummyValue) throws Exception {
        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class);
        final int[] numberOfTimesSet = {0};
        int worshipDayNumberr = this.getWorshipPeriodDayNumberAndPeriodNumber().get(CLASS_WORSHIP_DAY_NUMBER_KEY);
        String worshipDayName = getProgrammeDayName(worshipDayNumberr);
        System.out.println("WorshipDay Number=" + worshipDayNumberr + "\nWorshipDayName=" + worshipDayName);
        int worshipPeriodNumber = this.getWorshipPeriodDayNumberAndPeriodNumber().get(CLASS_WORSHIP_PERIOD_NUMBER_KEY);
        System.out.println("WorshipPeriodNumber=" + worshipPeriodNumber + "\n\n");
        timeTableSuperDocGeneratedFromString.getYearGroupsList().stream().parallel().forEach(yearGroup -> {
            yearGroup.getProgrammeGroupList().forEach(programmeGroup -> {
                programmeGroup.getProgrammeDaysList().forEach(programmeDay -> {
                    String programmeDayName = programmeDay.getDayName().toUpperCase().trim();
                    programmeDay.getPeriodList().forEach(periodOrLecture -> {
                        if (Objects.equals(periodOrLecture.getPeriodNumber(), worshipPeriodNumber) &&
                                Objects.equals(programmeDayName, (worshipDayName.toUpperCase().trim()))) {
                            periodOrLecture.setIsAllocated(true);
                            periodOrLecture.setSubjectUniqueIdInDb(subjectUnniqueIdInDbDummyValue);
                            periodOrLecture.setTutorUniqueId(DUMMY_WORSHIP_TUTOR_UNIQUE_ID);
                            numberOfTimesSet[0] = numberOfTimesSet[0] + 1;
                        }
                    });
                });
            });
        });
        System.out.println("numberOfTimesSet%%%%%%%%%%%======" + numberOfTimesSet[0]);
        //update programmeGrouppersonal timetable in db
        this.updateProgrammeGroupTimeTableWithWorshipPeriods(subjectUnniqueIdInDbDummyValue);
        return timeTableSuperDocGeneratedFromString;
    }

    /**
     * allocate class meetings For all ProgrammeGroups
     *
     * @param timeTableSuperDoc
     * @param subjectUniqueIdInDb pass in a dummy value ie NON EMPTY "WHATEVER" FOR SHORT
     * @return TimeTableSuperDoc
     */
    public TimeTableSuperDoc allocateClassMeetingPeriodForAllProgrammeGroups(TimeTableSuperDoc timeTableSuperDoc, String subjectUniqueIdInDb) throws Exception {
        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        TimeTableSuperDoc timeTableSuperDocGeneratedFromString = new Gson().fromJson(timeTableSuperDocString, TimeTableSuperDoc.class); //CONVERT BACK TO OBJECT FROM JSON TO PREVENT PROBLEMS OF WRONG WRITES IN UNSOLICITED PLACES

        final int[] numberOfTimesPeriodIsSet = {0};
        int classMeetingDayNumber = this.getClassMeetingPeriodDayNumberAndPeriodNumber().get(CLASS_MEETING_DAY_NUMBER_KEY);
        String classMeetingDayName = this.getProgrammeDayName(classMeetingDayNumber);
        System.out.println("classMeetingDayNumber=" + classMeetingDayNumber + "\nclassMeetingDayName=" + classMeetingDayName);
        int classMeetingPeriodNumber = this.getClassMeetingPeriodDayNumberAndPeriodNumber().get(CLASS_MEETING_PERIOD_NUMBER_KEY);
        System.out.println("classMeetingPeriodNumber=" + classMeetingPeriodNumber + "\n\n");
        timeTableSuperDocGeneratedFromString.getYearGroupsList().stream().parallel().forEach(yearGroup -> {
            yearGroup.getProgrammeGroupList().forEach(programmeGroup -> {
                programmeGroup.getProgrammeDaysList().forEach(programmeDay -> {
                    String programmeDayName = programmeDay.getDayName().toUpperCase().trim();
                    programmeDay.getPeriodList().forEach(periodOrLecture -> {
                        if (Objects.equals(periodOrLecture.getPeriodNumber(), classMeetingPeriodNumber) &&
                                Objects.equals(programmeDayName, (classMeetingDayName.toUpperCase().trim()))) {
                            periodOrLecture.setIsAllocated(true);
                            periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb);
                            periodOrLecture.setTutorUniqueId(DUMMY_CLASSMEETING_TUTOR_UNIQUE_ID);
                            numberOfTimesPeriodIsSet[0]++;
                        }
                    });
                });
            });
        });

        System.out.println("numberOfTimesSet%%%%%%%%%%%======" + numberOfTimesPeriodIsSet[0]);

        //allocate class meeting periods to programmeGroup personal timetable
        this.updateProgrammeGroupTimeTableWithClassMeetingPeriods(subjectUniqueIdInDb);
        return timeTableSuperDocGeneratedFromString;
    }

    private Map<String, Integer> getWorshipPeriodDayNumberAndPeriodNumber() {
        int worshipDayNumber = businessLogicConfigurationProperties.TIMETABLE_DAY_WORSHIP;
        int worshipPeriodNumber = businessLogicConfigurationProperties.TIMETABLE_PERIOD_WORSHIP;
        Map<String, Integer> worshipDayAndPeriodMap = new HashMap<>();
        worshipDayAndPeriodMap.put(CLASS_WORSHIP_DAY_NUMBER_KEY, worshipDayNumber);
        worshipDayAndPeriodMap.put(CLASS_WORSHIP_PERIOD_NUMBER_KEY, worshipPeriodNumber);
        return worshipDayAndPeriodMap;
    }

    private Map<String, Integer> getClassMeetingPeriodDayNumberAndPeriodNumber() {
        int classMeetingDayNumber = businessLogicConfigurationProperties.TIMETABLE_DAY_CLASS_MEETING;
        int classMeetingPeriodNumber = businessLogicConfigurationProperties.TIMETABLE_PERIOD_CLASS_MEETING;
        Map<String, Integer> classMeetingDayAndPeriodMap = new HashMap<>();
        classMeetingDayAndPeriodMap.put(CLASS_MEETING_DAY_NUMBER_KEY, classMeetingDayNumber);
        classMeetingDayAndPeriodMap.put(CLASS_MEETING_PERIOD_NUMBER_KEY, classMeetingPeriodNumber);
        return classMeetingDayAndPeriodMap;
    }

    /**
     * allocate practicals period for a specific day in the programmeGroup passed in
     *
     * @param currentProgrammeGroup the {@link ProgrammeGroup} object to allocate practicals period for.
     * @param programmeDaysList     List of {@link ProgrammeDay}s of the programmeGroup
     * @param programmeYearNo       the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc#yearGroup} ie. the yearGroup of the programme group
     * @return
     */
    private List<ProgrammeDay> allocatePracticalsPeriodsForProgrammeGroupRequiringIt(ProgrammeGroup currentProgrammeGroup,
                                                                                     List<ProgrammeDay> programmeDaysList, int programmeYearNo) {

        //we iterate through all programmeDaysList
        int numberOfProgrammeDays = programmeDaysList.size();
        for (int currentProgrammeDayNumber = 0; currentProgrammeDayNumber < numberOfProgrammeDays; currentProgrammeDayNumber++) {
            ProgrammeDay currentProgrammeDay = programmeDaysList.get(currentProgrammeDayNumber);
            //now for each currentProgrammeDay,we check if it's allocated already,if its allocated,we do not touch it ,else
            //we setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice by passing that to another method to handle that ,then it returns a
            //new programmeDay with all periods set with the subjects,then we set it to the current list at same index
            iProgrammeDayHelper = programmeDayHelperUtilDefault; //instantiate interface with implementation
            int practicalsTotalPeriods = this.getTotalPeriodsForPracticalSubjectInProgrammeGroupSubjectsList(currentProgrammeGroup, programmeYearNo);

            if (iProgrammeDayHelper.
                    isProgrammeDayCapableOfAcceptingTheIncomingNumberOfPeriodsAssumingUnallocatedDaysAreSequential
                            (currentProgrammeDay, practicalsTotalPeriods)) {
                //double check ,but double check is better than once.
                ProgrammeDay newCurrentProgrammeDay = this.
                        setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice(currentProgrammeDay, currentProgrammeGroup, practicalsTotalPeriods);
                //we set the newlycreatedprogrammeDay with all periods subject set to the programmeDaysList
                programmeDaysList.set(currentProgrammeDayNumber, newCurrentProgrammeDay);
                break;
            }
        }
        return programmeDaysList;
    }

    private ProgrammeDay setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice(ProgrammeDay programmeDay,
                                                                                          ProgrammeGroup currentProgrammeGroup,
                                                                                          int totalPeriodForPracticalCourse) {
        List<PeriodOrLecture> periodOrLecturesInProgDay = programmeDay.getPeriodList();

        //to get totalNumberOfProgrammeSubjectsUniqueIdInDbList practicalCourseTotalPeriod,scan and check that SubjectDoc property isSubjectAPracticalSubject = true
        List<String> programmeSubjectsUniqueIdInDbList = currentProgrammeGroup.getProgrammeSubjectsUniqueIdInDbList();
        int totalNumberOfProgrammeSubjectsUniqueIdInDbList = programmeSubjectsUniqueIdInDbList.size();
        String programmeCode = currentProgrammeGroup.getProgrammeCode();
        String practicalSubjectId = null;
        for (int currentSubjectCodeNo = 0; currentSubjectCodeNo < totalNumberOfProgrammeSubjectsUniqueIdInDbList; currentSubjectCodeNo++) {
            String currentSubjectUniqueIdInDb = programmeSubjectsUniqueIdInDbList.get(currentSubjectCodeNo);
            //fetch the subjectDoc from db using the subjectCode
            SubjectDoc currentSubjectDoc = subjectDocRepository.findOne(currentSubjectUniqueIdInDb);
            if (currentSubjectDoc.isSubjectAPracticalSubject() == true) {
                practicalSubjectId = currentSubjectDoc.getId();
                break;
            }
        }
        //now we can set the practical subjectId and tutorid to the periods and update dayIsAllocated if the existing day is exhausted

        int periodToStartSettingSubjectFrom = this.getIndexToStartSettingPeriodsFrom(periodOrLecturesInProgDay);
        List<PeriodOrLecture> newPeriodOrLectureListToSetToProgrammeDay =
                this.setSubjectUniqueIdInDbAndTutorUniqueIdForAllAffectedPeriodsOfferingPracticals(programmeDay, periodToStartSettingSubjectFrom,
                        periodOrLecturesInProgDay, practicalSubjectId, programmeCode, totalPeriodForPracticalCourse);
        //now we set the new periodOrLectureList to the programmeDay and return it
        programmeDay.setPeriodList(newPeriodOrLectureListToSetToProgrammeDay);

        return programmeDay;
    }

    /**
     * get total periods for practical subject in programmeGroup Subjects .practical subject is one for each programmeGroup that does practical work requiring a workshop or kitchen
     *
     * @param programmeGroup  the programmeGroup
     * @param programmeYearNo the yearGroup of the programmeGroup
     * @return int
     */
    int getTotalPeriodsForPracticalSubjectInProgrammeGroupSubjectsList(ProgrammeGroup programmeGroup, int programmeYearNo) {
        //to get totalNumberOfProgrammeSubjectsUniqueIdInDbList practicalCourseTotalPeriod,scan and check that SubjectDoc property isSubjectAPracticalSubject = true
        List<String> programmeSubjectsUniqueIdInDbList = programmeGroup.getProgrammeSubjectsUniqueIdInDbList();
        int totalNumberOfProgrammeSubjectsUniqueIdInDbList = programmeSubjectsUniqueIdInDbList.size();
        int totalPeriodForPracticalCourse = 0;
        for (int currentSubjectCodeNo = 0; currentSubjectCodeNo < totalNumberOfProgrammeSubjectsUniqueIdInDbList; currentSubjectCodeNo++) {
            String currentSubjectUniqueIdInDb = programmeSubjectsUniqueIdInDbList.get(currentSubjectCodeNo);
            //fetch the subjectDoc from db using the subjectCode
            SubjectDoc currentSubjectDoc = subjectDocRepository.findOne(currentSubjectUniqueIdInDb);
            if (currentSubjectDoc.isSubjectAPracticalSubject() == true) {
                //we get the totalPeriods for the practicals course from subjectAllocationDocRepository and use getTotalSubjectAllocation() to retrieve the int value
                String currentSubjectCode = currentSubjectDoc.getSubjectCode();
                totalPeriodForPracticalCourse = subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(currentSubjectCode, programmeYearNo).getTotalSubjectAllocation();
                break;
            }
        }
        return totalPeriodForPracticalCourse;
    }

    public List<PeriodOrLecture> setSubjectUniqueIdInDbAndTutorUniqueIdForAllAffectedPeriodsOfferingPracticals(ProgrammeDay programmeDay,
                                                                                                               int periodToStartSettingSubjectFrom,
                                                                                                               List<PeriodOrLecture> periodOrLecturesList,
                                                                                                               String practicalSubjectId,
                                                                                                               String programmeCode,
                                                                                                               int totalPeriodForPracticalCourse) {
        /**
         set subjectCode and tutorCode ,make sure tutorCode has enough periods left,and also ensure totalPeriodAllocation left is enough
         get Tutor Responsible by using the practicalSubjectId to retrieve tutor from db
         remember todo FIXED!!!!!! the implementation of the {@link com.swiftpot.timetable.base.TutorResponsibleForSubjectRetriever} Interface,as it's not done yet
         */
        String tutorIdResponsibleForSubject =
                tutorResponsibleForSubjectRetrieverFactory.getTutorResponsibleForSubjectRetrieverImpl()
                        .getTutorObjectUniqueIdResponsibleForSubject(practicalSubjectId);
        int totalPeriodsToIterateThrough = periodOrLecturesList.size();
        int totalPeriodsThatHasBeenSet = 0;
        for (int i = 0; i < totalPeriodsToIterateThrough; i++) {
            if ((periodOrLecturesList.get(i).getPeriodNumber() >= periodToStartSettingSubjectFrom) && (totalPeriodsThatHasBeenSet <= totalPeriodForPracticalCourse)) {
                //we increment totalPeriodsThatHasBeenSet by 1 setSubjectUniqueIdInDbAndTutorUniqueIdForAllAffectedPeriodsOfferingPracticals
                totalPeriodsThatHasBeenSet++;
                PeriodOrLecture currentPeriodOrLecture = periodOrLecturesList.get(i);
                currentPeriodOrLecture.setSubjectUniqueIdInDb(practicalSubjectId);
                currentPeriodOrLecture.setTutorUniqueId(tutorIdResponsibleForSubject);
                currentPeriodOrLecture.setIsAllocated(true);
                //now set the new currentPeriodOrLecture to the same position in the list
                periodOrLecturesList.set(i, currentPeriodOrLecture);
            }
        }

        int startingPeriod = periodToStartSettingSubjectFrom;
        int stoppingPeriod = startingPeriod + totalPeriodForPracticalCourse - 1; //this should automatically amount to the period number that the allocation ends,-1 because if starting period starts from 2,and the total periods set is 1,without subtracting 1,it will mean that the period will end at period 3 instead of the same period which is 2
        //updateDb Accordingly to reflect totalSubjectsPeriodLeft in db for both tutorDoc and periods left for the subject to be exhausted
        this.updateDbWithTotalPeriodsThatHasBeenSet(programmeDay, programmeCode, practicalSubjectId, totalPeriodForPracticalCourse, tutorIdResponsibleForSubject, startingPeriod, stoppingPeriod);

        return periodOrLecturesList;
    }

    /**
     * TODO DONE!!!!! Remember to initialize the {@link TutorSubjectAndProgrammeGroupCombinationDoc} <br> immediately during first initialization so that we can search for it and retrieve and set periods left for a tutor's subject and programmeGroup {@link TutorPersonalTimeTableInitialGenerator#generatePersonalTimeTableForAllTutorsInDbAndSaveIntoDb()} handles that <br><br>
     * TODO DONE!!!!! Remember to initialize the {@link com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc} <br> immediately during first initialization so that we can search for it and update the timetable of each programmeGroup upon every write on {@link TimeTableSuperDoc}<br>{@link ProgrammeGroupPersonalTimeTableDocInitialGenerator#generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsInDbAndSaveInDb()} handles that. <br><br>
     * TODO DONE!!!!! Remember to initialize the {@link com.swiftpot.timetable.repository.db.model.TutorSubjectAndProgrammeGroupCombinationDoc} <br> immediately during first initialization so that we can search for it and update the {@link TutorSubjectAndProgrammeGroupCombinationDoc#totalPeriodLeftToBeAllocated} for that tutor for that subject upon every write on {@link TimeTableSuperDoc} object <br> {@link TutorSubjectAndProgrammeGroupInitialGenerator#generateAllInitialSubjectAndProgrammeGroupCombinationDocsForAllTutorsInDBAndSaveInDb()} handles that <br><br>
     * <br><b>THE LOAD OF PARAMETERS ON THIS METHOD IS ABSURD,VERY STUPID,BUT IT HAPPENED BY ACCIDENT,SO ENJOY UNTIL THIS SIDE GETS REFACTORED =D :P</b> <br><br>
     *
     * @param programmeCode
     * @param subjectUniqueIdInDb
     * @param totalPeriodForPracticalCourse
     * @param tutorIdResponsibleForSubject
     * @param startingPeriod
     * @param stoppingPeriod
     */
    void updateDbWithTotalPeriodsThatHasBeenSet(ProgrammeDay programmeDay,
                                                String programmeCode,
                                                String subjectUniqueIdInDb,
                                                int totalPeriodForPracticalCourse,
                                                String tutorIdResponsibleForSubject,
                                                int startingPeriod,
                                                int stoppingPeriod) {
        String programmeDayName = programmeDay.getDayName();

        //now we decrement the value of the totalSubjectsPeriodLeft in db by the totalPeriodsThatHasBeenSet
        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDoc = tutorSubjectAndProgrammeGroupCombinationDocRepository.
                findBySubjectUniqueIdAndProgrammeCode(subjectUniqueIdInDb, programmeCode);
        int totalPeriodLoadLeftToBeAllocatedForTutor = tutorSubjectAndProgrammeGroupCombinationDoc.getTotalPeriodLeftToBeAllocated();
        int periodLoadLeftAfterDeduction = totalPeriodLoadLeftToBeAllocatedForTutor - totalPeriodForPracticalCourse;
        tutorSubjectAndProgrammeGroupCombinationDoc.setTotalPeriodLeftToBeAllocated(periodLoadLeftAfterDeduction);

        TutorSubjectAndProgrammeGroupCombinationDoc tutorSubjectAndProgrammeGroupCombinationDocUpdated =
                new TutorSubjectAndProgrammeGroupCombinationDoc(subjectUniqueIdInDb, programmeCode, periodLoadLeftAfterDeduction);
        tutorSubjectAndProgrammeGroupCombinationDocUpdated.setId(tutorSubjectAndProgrammeGroupCombinationDoc.getId());

        tutorSubjectAndProgrammeGroupCombinationDocRepository.save(tutorSubjectAndProgrammeGroupCombinationDocUpdated);

        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc = programmeGroupPersonalTimeTableDocServices.
                updateProgrammeGroupPersonalTimeTableDocWithPeriodsAndSaveInDb
                        (tutorIdResponsibleForSubject, programmeCode, subjectUniqueIdInDb, programmeDayName, startingPeriod, stoppingPeriod);


        //now we have to update the tutorDoc's personal timetable too..
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc =
                tutorPersonalTimeTableDocServices.
                        updateTutorPersonalTimeTableDocWithPeriodsAndSaveInDb
                                (tutorIdResponsibleForSubject, subjectUniqueIdInDb, programmeDayName, startingPeriod, stoppingPeriod);

    }

    /**
     * get the first index of the location where getIsAllocated is equal to false
     *
     * @param periodOrLecturesList {@link List<PeriodOrLecture>}
     * @return int
     */
    public int getIndexToStartSettingPeriodsFrom(List<PeriodOrLecture> periodOrLecturesList) {
        return programmeDayServices.getFirstIndexPositionOfPeriodWhereAllocationIsFalseInListOfPeriods(periodOrLecturesList);
    }

    public String getProgrammeDayName(int programmeDayNumber) throws Exception {
        String programmeDayName;
        switch (programmeDayNumber) {
            case 1:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_1;
                break;
            case 2:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_2;
                break;
            case 3:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_3;
                break;
            case 4:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_4;
                break;
            case 5:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_5;
                break;
            case 6:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_6;
                break;
            case 7:
                programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_7;
                break;
            default:
                throw new Exception("Only 7days available,any value not within 1-7 will fail");
        }

        return programmeDayName;
    }

    /**
     * update all list of {@link ProgrammeGroupPersonalTimeTableDoc}s in db with the worship periods
     *
     * @param subjectUniqueIdInDb
     * @throws Exception
     */
    private void updateProgrammeGroupTimeTableWithWorshipPeriods(String subjectUniqueIdInDb) throws Exception {
        int worshipDayNumberr = this.getWorshipPeriodDayNumberAndPeriodNumber().get(CLASS_WORSHIP_DAY_NUMBER_KEY);
        String worshipDayName = getProgrammeDayName(worshipDayNumberr);
        System.out.println("WorshipDay Number=" + worshipDayNumberr + "\nWorshipDayName=" + worshipDayName);
        int worshipPeriodNumber = this.getWorshipPeriodDayNumberAndPeriodNumber().get(CLASS_WORSHIP_PERIOD_NUMBER_KEY);

        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocs =
                programmeGroupPersonalTimeTableDocRepository.findAll();

        programmeGroupPersonalTimeTableDocs.forEach(programmeGroupPersonalTimeTableDoc -> {
            programmeGroupPersonalTimeTableDoc.getProgrammeDaysList().forEach(programmeDay -> {
                if (Objects.equals(worshipDayName, programmeDay.getDayName())) {
                    programmeDay.getPeriodList().forEach(periodOrLecture -> {
                        if (Objects.equals(worshipPeriodNumber, periodOrLecture.getPeriodNumber())) {
                            periodOrLecture.setIsAllocated(true);
                            periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb);
                            periodOrLecture.setTutorUniqueId(DUMMY_WORSHIP_TUTOR_UNIQUE_ID);
                        }
                    });
                }
            });
        });

        programmeGroupPersonalTimeTableDocRepository.save(programmeGroupPersonalTimeTableDocs);
    }

    /**
     * update all list of {@link ProgrammeGroupPersonalTimeTableDoc}s in db with the class meeting periods.
     *
     * @param subjectUniqueIdInDb
     * @throws Exception
     */
    private void updateProgrammeGroupTimeTableWithClassMeetingPeriods(String subjectUniqueIdInDb) throws Exception {
        int classMeetingDayNumber = this.getClassMeetingPeriodDayNumberAndPeriodNumber().get(CLASS_MEETING_DAY_NUMBER_KEY);
        String classMeetingDayName = this.getProgrammeDayName(classMeetingDayNumber);
        System.out.println("classMeetingDayNumber=" + classMeetingDayNumber + "\nclassMeetingDayName=" + classMeetingDayName);
        int classMeetingPeriodNumber = this.getClassMeetingPeriodDayNumberAndPeriodNumber().get(CLASS_MEETING_PERIOD_NUMBER_KEY);

        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocs =
                programmeGroupPersonalTimeTableDocRepository.findAll();

        programmeGroupPersonalTimeTableDocs.forEach(programmeGroupPersonalTimeTableDoc -> {
            programmeGroupPersonalTimeTableDoc.getProgrammeDaysList().forEach(programmeDay -> {
                if (Objects.equals(classMeetingDayName, programmeDay.getDayName())) {
                    programmeDay.getPeriodList().forEach(periodOrLecture -> {
                        if (Objects.equals(classMeetingPeriodNumber, periodOrLecture.getPeriodNumber())) {
                            periodOrLecture.setIsAllocated(true);
                            periodOrLecture.setSubjectUniqueIdInDb(subjectUniqueIdInDb);
                            periodOrLecture.setTutorUniqueId(DUMMY_CLASSMEETING_TUTOR_UNIQUE_ID);
                        }
                    });
                }
            });
        });

        programmeGroupPersonalTimeTableDocRepository.save(programmeGroupPersonalTimeTableDocs);
    }
}
