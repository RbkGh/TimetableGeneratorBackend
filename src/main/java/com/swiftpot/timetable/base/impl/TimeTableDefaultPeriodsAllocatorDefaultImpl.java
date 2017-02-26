package com.swiftpot.timetable.base.impl;

import com.google.gson.Gson;
import com.swiftpot.timetable.base.IProgrammeDayHelper;
import com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator;
import com.swiftpot.timetable.factory.TutorResponsibleForSubjectRetrieverFactory;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.SubjectPeriodLoadLeftForProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.TutorDocRepository;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import com.swiftpot.timetable.repository.db.model.SubjectPeriodLoadLeftForProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import com.swiftpot.timetable.util.ProgrammeDayHelperUtilDefaultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
    SubjectPeriodLoadLeftForProgrammeGroupDocRepository subjectPeriodLoadLeftForProgrammeGroupDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    IProgrammeDayHelper iProgrammeDayHelper;
    @Autowired
    ProgrammeDayHelperUtilDefaultImpl programmeDayHelperUtilDefault;


    @Override
    public TimeTableSuperDoc allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) {
        List<YearGroup> yearGroupList = timeTableSuperDocWithInitialDefaultDataSet.getYearGroupsList();
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
                    List<ProgrammeDay> newlyCreatedProgrammeDaysList = setPracticalsDaysForAllProgrammeGroupsRequiringIt(currentProgrammeGroup,
                            programmeDaysList,
                            programmeYearNo);
                    programmeGroupList.get(currentProgrammeGroupNo).setProgrammeDaysList(newlyCreatedProgrammeDaysList);
                    yearGroupList.get(currentNo).setProgrammeGroupList(programmeGroupList);
                    timeTableSuperDocWithInitialDefaultDataSet.setYearGroupsList(yearGroupList);
                }
            }
        }

        return timeTableSuperDocWithInitialDefaultDataSet;
    }

    /**
     * TODO find a way to inject subjectCodeForWorship,=>fix by utilizing optional in java so that a default of "None" or "" .
     * TODO DECIDE WETHER TO POPULATE WORKSHOP WITH SUBJECTS FIRST OR ALLOCATE WORSHIP PERIODS FIRST
     *
     * @param timeTableSuperDoc
     * @param subjectCodeForWorship
     * @return
     */
    public TimeTableSuperDoc allocateWorshipPeriodForAllProgrammeGroups(TimeTableSuperDoc timeTableSuperDoc, String subjectCodeForWorship) throws Exception {
        String timeTableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        timeTableSuperDoc = new Gson().fromJson(timeTableSuperDocString,TimeTableSuperDoc.class);
        final int[] numberOfTimesSet = {0};
        int worshipDayNumberr = getWorshipPeriodDayNumberAndPeriodNumber().get("worshipDayNumber");
        String worshipDayName = getProgrammeDayName(worshipDayNumberr);
        System.out.println("WorshipDay Number=" + worshipDayNumberr + "\nWorshipDayName=" + worshipDayName);
        int worshipPeriodNumber = getWorshipPeriodDayNumberAndPeriodNumber().get("worshipPeriodNumber");
        System.out.println("WorshipPeriodNumber=" + worshipPeriodNumber + "\n\n");
        timeTableSuperDoc.getYearGroupsList().stream().parallel().forEach(yearGroup -> {
            yearGroup.getProgrammeGroupList().forEach(programmeGroup -> {
                programmeGroup.getProgrammeDaysList().forEach(programmeDay -> {
                    String programmeDayName = programmeDay.getDayName().toUpperCase().trim();
                    programmeDay.getPeriodList().forEach(periodOrLecture -> {
                        if (Objects.equals(periodOrLecture.getPeriodNumber(), worshipPeriodNumber) &&
                                Objects.equals(programmeDayName, (worshipDayName.toUpperCase().trim()))) {
                            periodOrLecture.setIsAllocated(true);
                            periodOrLecture.setSubjectCode(subjectCodeForWorship);
                            numberOfTimesSet[0]++;
                        }
                    });
                });
            });
        });

        System.out.println("numberOfTimesSet%%%%%%%%%%%======" + numberOfTimesSet[0]);
        return timeTableSuperDoc;
    }

    private Map<String, Integer> getWorshipPeriodDayNumberAndPeriodNumber() {
        int worshipDayNumber = businessLogicConfigurationProperties.TIMETABLE_DAY_WORSHIP;
        int worshipPeriodNumber = businessLogicConfigurationProperties.TIMETABLE_PERIOD_WORSHIP;
        Map<String, Integer> worshipDayAndPeriodMap = new HashMap<>();
        worshipDayAndPeriodMap.put("worshipDayNumber", worshipDayNumber);
        worshipDayAndPeriodMap.put("worshipPeriodNumber", worshipPeriodNumber);
        return worshipDayAndPeriodMap;
    }

    private List<ProgrammeDay> setPracticalsDaysForAllProgrammeGroupsRequiringIt(ProgrammeGroup currentProgrammeGroup,
                                                                                 List<ProgrammeDay> programmeDaysList, int programmeYearNo) {

        //we iterate through all programmeDaysList
        int numberOfProgrammeDays = programmeDaysList.size();
        for (int currentProgrammeDayNumber = 0; currentProgrammeDayNumber < numberOfProgrammeDays; currentProgrammeDayNumber++) {
            ProgrammeDay currentProgrammeDay = programmeDaysList.get(currentProgrammeDayNumber);
            //now for each currentProgrammeDay,we check if it's allocated already,if its allocated,we do not touch it ,else
            //we setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice by passing that to another method to handle that ,then it returns a
            //new programmeDay with all periods set with the subjects,then we set it to the current list at same index
            iProgrammeDayHelper = programmeDayHelperUtilDefault; //instantiate interface with implementation
            if (iProgrammeDayHelper.isProgrammeDayAlloted(currentProgrammeDay) == false) {
                ProgrammeDay newCurrentProgrammeDay = setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice(currentProgrammeDay, currentProgrammeGroup, programmeYearNo);
                //we set the newlycreatedprogrammeDay with all periods subject set to the programmeDaysList
                programmeDaysList.set(currentProgrammeDayNumber, newCurrentProgrammeDay);
            }
        }
        return programmeDaysList;
    }

    private ProgrammeDay setPracticalsPeriodsByCheckingIfRemainingPeriodsForDayCanSuffice(ProgrammeDay programmeDay,
                                                                                          ProgrammeGroup currentProgrammeGroup,
                                                                                          int programmeYearNo) {
        //to calculate if periods for day will be enough,check the practicals total period against the total available
        //period for the day
        List<PeriodOrLecture> periodOrLecturesInProgDay = programmeDay.getPeriodList();
        int totalNumberOfPeriods = periodOrLecturesInProgDay.size();
        int totalPeriodsAvailableForDay = 0;
        for (int currentPeriodOrLectureNo = 0; currentPeriodOrLectureNo < totalNumberOfPeriods; currentPeriodOrLectureNo++) {
            PeriodOrLecture currentPeriodOrLecture = periodOrLecturesInProgDay.get(currentPeriodOrLectureNo);
            if (currentPeriodOrLecture.getIsAllocated() == false) {
                totalPeriodsAvailableForDay++;
            }
        }
        //now if totalPeriodsAvailableForDay is >= totalPeriodForPracticalCourse,then we can set it,otherwise do nothing
        //to get practicalCourseTotalPeriod,scan and check subjectName that contains "PRACTICALS" equalsIgnorCase,very important it contains this word
        List<String> programmeSubjectsCodeList = currentProgrammeGroup.getProgrammeSubjectsCodeList();
        int totalNumberOfProgrammeSubjectsCodeList = programmeSubjectsCodeList.size();
        int totalPeriodForPracticalCourse = 0;
        String programmeCode = currentProgrammeGroup.getProgrammeCode();
        String practicalSubjectCode = null;
        for (int currentSubjectCodeNo = 0; currentSubjectCodeNo < totalNumberOfProgrammeSubjectsCodeList; currentSubjectCodeNo++) {
            String currentSubjectCode = programmeSubjectsCodeList.get(currentSubjectCodeNo);
            //fetch the subjectDoc from db using the subjectCode
            SubjectDoc currentSubjectDoc = subjectDocRepository.findBySubjectCodeAllIgnoreCase(currentSubjectCode);
            if (currentSubjectDoc.getSubjectFullName().contains("PRACTICALS")) {
                //we get the totalPeriods for the practicals course from subjectAllocationDocRepository and use getTotalSubjectAllocation() to retrieve the int value
                totalPeriodForPracticalCourse = subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(currentSubjectCode, programmeYearNo).getTotalSubjectAllocation();
                practicalSubjectCode = currentSubjectDoc.getSubjectCode();
            }
        }
        //now we can actually compare if the totalPeriodsAvailableForDay >= totalPeriodForPracticalCourse
        if (totalPeriodsAvailableForDay >= totalPeriodForPracticalCourse) {
            //now we can set the practical subjectCode and tutorCode to the periods and update dayIsAllocated if the existing day is exhausted
            int periodToStartSettingSubjectFrom = getIndexToStartSettingPeriodsFrom(periodOrLecturesInProgDay);
            List<PeriodOrLecture> newPeriodOrLectureListToSetToProgrammeDay =
                    setSubjectCodeAndTutorCodeForAllAffectedPeriodsOfferingPracticals(periodToStartSettingSubjectFrom,
                            periodOrLecturesInProgDay, practicalSubjectCode, programmeCode, totalPeriodForPracticalCourse);
            //now we set the new periodOrLectureList to the programmeDay and return it
            programmeDay.setPeriodList(newPeriodOrLectureListToSetToProgrammeDay);
        }

        return programmeDay;
    }

    public List<PeriodOrLecture> setSubjectCodeAndTutorCodeForAllAffectedPeriodsOfferingPracticals(int periodToStartSettingSubjectFrom,
                                                                                                   List<PeriodOrLecture> periodOrLecturesList,
                                                                                                   String practicalSubjectCode,
                                                                                                   String programmeCode,
                                                                                                   int totalPeriodForPracticalCourse) {
        /*
        set subjectCode and tutorCode ,make sure tutorCode has enough periods left,and also ensure totalPeriodAllocation left is enough
        get Tutor Responsible by using the practicalSubjectCode to retrieve tutor from db
        remember todo the implementation of the {@link com.swiftpot.timetable.base.TutorResponsibleForSubjectRetriever} Interface,as it's not done yet
        */
        String tutorCodeResponsibleForSubject = tutorResponsibleForSubjectRetrieverFactory.getTutorResponsibleForSubjectRetrieverImpl().getTutorCodeResponsibleForSubject(practicalSubjectCode);
        int totalPeriodsToIterateThrough = periodOrLecturesList.size();
        int totalPeriodsThatHasBeenSet = 0;
        for (int i = 0; i < totalPeriodsToIterateThrough; i++) {
            if (i >= periodToStartSettingSubjectFrom) {
                //we increment totalPeriodsThatHasBeenSet by 1
                totalPeriodsThatHasBeenSet++;
                PeriodOrLecture currentPeriodOrLecture = periodOrLecturesList.get(i);
                currentPeriodOrLecture.setSubjectCode(practicalSubjectCode);
                currentPeriodOrLecture.setTutorCode(tutorCodeResponsibleForSubject);
                currentPeriodOrLecture.setIsAllocated(true);
                //now set the new currentPeriodOrLecture to the same position in the list
                periodOrLecturesList.set(i, currentPeriodOrLecture);
            }
        }
        //updateDb Accordingly to reflect totalSubjectsPeriodLeft in db for both tutorDoc and periods left for the subject to be exhausted
        updateDbWithTotalPeriodsThatHasBeenSet(programmeCode, practicalSubjectCode, totalPeriodForPracticalCourse, totalPeriodsThatHasBeenSet, tutorCodeResponsibleForSubject);

        return periodOrLecturesList;
    }

    /**
     * TODO Remember to initialize the {@link SubjectPeriodLoadLeftForProgrammeGroupDoc} immediately during first initialization so that we can search for it and retrieve and set periods left for a particular subject for a particular programmegroup
     *
     * @param programmeCode
     * @param practicalSubjectCode
     * @param totalPeriodForPracticalCourse
     * @param totalPeriodsThatHasBeenSet
     * @param tutorCodeResponsibleForSubject
     */
    void updateDbWithTotalPeriodsThatHasBeenSet(String programmeCode,
                                                String practicalSubjectCode,
                                                int totalPeriodForPracticalCourse,
                                                int totalPeriodsThatHasBeenSet,
                                                String tutorCodeResponsibleForSubject) {
        //now we decrement the value of the totalSubjectsPeriodLeft in db by the totalPeriodsThatHasBeenSet
        SubjectPeriodLoadLeftForProgrammeGroupDoc subjectPeriodLoadLeftForProgrammeGroupDoc = subjectPeriodLoadLeftForProgrammeGroupDocRepository.
                findByProgrammeCodeAndSubjectCode(programmeCode, practicalSubjectCode);
        int periodLoadLeft = totalPeriodForPracticalCourse - totalPeriodsThatHasBeenSet;
        subjectPeriodLoadLeftForProgrammeGroupDoc.setPeriodLoadLeft(periodLoadLeft);
        subjectPeriodLoadLeftForProgrammeGroupDocRepository.save(subjectPeriodLoadLeftForProgrammeGroupDoc);

        //now we have to update the tutorDoc too..
        TutorDoc tutorDoc = tutorDocRepository.findByTutorCode(tutorCodeResponsibleForSubject);
        tutorDoc.setCurrentPeriodLoadLeft(periodLoadLeft);
        tutorDocRepository.save(tutorDoc);
    }

    /**
     * get the first index of the location where getIsAllocated is equal to false
     *
     * @param periodOrLecturesList {@link List<PeriodOrLecture>}
     * @return int
     */
    public int getIndexToStartSettingPeriodsFrom(List<PeriodOrLecture> periodOrLecturesList) {
        int totalPeriodsToIterateThrough = periodOrLecturesList.size();
        List<Integer> listOfIndexesWhereFalseWasSeen = new ArrayList<>();
        for (int i = 0; i < totalPeriodsToIterateThrough; i++) {
            if (periodOrLecturesList.get(i).getIsAllocated() == false) {
                listOfIndexesWhereFalseWasSeen.add(i);
            }
        }
        //first will be first element in list
        int indexWhereFalseWasFirstSeen = listOfIndexesWhereFalseWasSeen.get(0);
        return indexWhereFalseWasFirstSeen;
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
}
