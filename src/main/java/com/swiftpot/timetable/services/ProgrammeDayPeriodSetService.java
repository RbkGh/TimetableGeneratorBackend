/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.swiftpot.timetable.exception.PracticalSubjectForDayNotFoundException;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 2:38 PM
 */
@Service
public class ProgrammeDayPeriodSetService {

    @Autowired
    private BusinessLogicConfigurationProperties propFile;
    @Autowired
    ProgrammeDayServices programmeDayServices;

    /**
     * only difference with {@link ProgrammeDayPeriodSetService#getPeriodAllocationForDayAsList(int)} <br>
     * is that this returns a {@link List} of {@link PeriodSetForProgrammeDay} instead of {@link Integer}
     *
     * @param periodAllocationCombinationNumber
     * @return {@link List} of {@link PeriodSetForProgrammeDay}
     * @see ProgrammeDayPeriodSetService#getPeriodAllocationForDayAsList(int)
     */
    public List<PeriodSetForProgrammeDay> getPeriodAllocationForDayAsProgDayPeriodSetList(int periodAllocationCombinationNumber) {
        List<PeriodSetForProgrammeDay> finalPeriodSetForProgrammeDays = new ArrayList<>(0);


        List<Integer> finalPeriodAllocationForDayList = this.getPeriodAllocationForDayAsList(periodAllocationCombinationNumber);
        int totalfinalPeriodAllocationForDayListSize = finalPeriodAllocationForDayList.size();


        for (int i = 0; i < totalfinalPeriodAllocationForDayListSize; i++) {
            int currentTotalPeriodAllocation = finalPeriodAllocationForDayList.get(i);
            if (finalPeriodSetForProgrammeDays.isEmpty()) {
                //periodStartingNumber will be 1 since the list of programmeDayPeriodSets is empty
                int periodStartingNumber = 1;
                int periodEndingNumber = periodStartingNumber + (currentTotalPeriodAllocation - 1);
                PeriodSetForProgrammeDay periodSetForProgrammeDay = new PeriodSetForProgrammeDay();
                periodSetForProgrammeDay.setPeriodStartingNumber(periodStartingNumber);//set starting number;
                periodSetForProgrammeDay.setPeriodEndingNumber(periodEndingNumber); //set ending number
                periodSetForProgrammeDay.setTotalNumberOfPeriodsForSet(currentTotalPeriodAllocation);//totalNumberOfPeriodsForSet

                finalPeriodSetForProgrammeDays.add(periodSetForProgrammeDay); //add to finalList
            } else {
                PeriodSetForProgrammeDay previousPeriodSetForProgrammeDay = finalPeriodSetForProgrammeDays.get(i - 1);
                int previousProgrammeDaySetEndingPeriodNumber = previousPeriodSetForProgrammeDay.getPeriodEndingNumber();

                int currentStartingPeriodNumber = previousProgrammeDaySetEndingPeriodNumber + 1;//where the last ProgrammeDayPeriodSetEnded,we continue from the next period after the last period where it ended
                int currentEndingPeriodNumber = currentStartingPeriodNumber + (currentTotalPeriodAllocation - 1);
                int totalNumberOfPeriodsForSet = currentTotalPeriodAllocation;

                PeriodSetForProgrammeDay periodSetForProgrammeDay = new PeriodSetForProgrammeDay();
                periodSetForProgrammeDay.setPeriodStartingNumber(currentStartingPeriodNumber);//set starting number;
                periodSetForProgrammeDay.setPeriodEndingNumber(currentEndingPeriodNumber); //set ending number
                periodSetForProgrammeDay.setTotalNumberOfPeriodsForSet(totalNumberOfPeriodsForSet);//totalNumberOfPeriodsForSet

                finalPeriodSetForProgrammeDays.add(periodSetForProgrammeDay); //add to finalList
            }
        }

        return finalPeriodSetForProgrammeDays;
    }

    /**
     * GET PERIOD ALLOCATION FOR A DAY BY PROVIDING A COMBINATION NUMBER eg,{@link BusinessLogicConfigurationProperties#DAY_PERIOD_ALLOCATION_COMBINATION_1}=2,3,3,2
     * meaning the periods for the day will be 2,3,3,2
     *
     * @param periodAllocationCombinationNumber
     * @return
     */
    public List<Integer> getPeriodAllocationForDayAsList(int periodAllocationCombinationNumber) {

        List<Integer> finalPeriodAllocationForDayList;

        switch (periodAllocationCombinationNumber) {
            case 1:
                finalPeriodAllocationForDayList = getListFromCustomProperties(propFile.DAY_PERIOD_ALLOCATION_COMBINATION_1);
                break;
            case 2:
                finalPeriodAllocationForDayList = getListFromCustomProperties(propFile.DAY_PERIOD_ALLOCATION_COMBINATION_2);
                break;
            case 3:
                finalPeriodAllocationForDayList = getListFromCustomProperties(propFile.DAY_PERIOD_ALLOCATION_COMBINATION_3);
                break;
            default:
                throw new AssertionError("Only 1-3 combinations allowed");
        }
        return finalPeriodAllocationForDayList;
    }

    /**
     * TODO USELESS CODE CHUNK AFTER ALL ,FOUND A NEATER AND BETTER WAY!!!!!.KNOW THAT PRACTICAL PERIODS CAN BE ONLY 6 OR 10 PERIODS FOR THIS TO WORK.AND BECAUSE WE ASSIGN PRACTICALS PERIODS BEFORE GETTING HERE,VIOLATIONS WILL ONLY OCCUR AFTER PERIOD 6 AND OVER.
     *
     * @param programmeDay
     * @return
     */
    @Deprecated
    public List<PeriodSetForProgrammeDay> getNewListOfPeriodSetForProgrammeDayIgnoringTheOneFromDb(ProgrammeDay programmeDay) throws PracticalSubjectForDayNotFoundException {

        String periodNumber1SubjectUniqueIdExpectedToBePractical = null;
        for (PeriodOrLecture periodOrLecture : programmeDay.getPeriodList()) {
            if (periodOrLecture.getPeriodNumber() == 1 &&
                    (Objects.nonNull(periodOrLecture.getSubjectUniqueIdInDb()) &&
                            (!periodOrLecture.getSubjectUniqueIdInDb().isEmpty()))) {
                periodNumber1SubjectUniqueIdExpectedToBePractical = periodOrLecture.getSubjectUniqueIdInDb();
            }
        }
        if (Objects.isNull(periodNumber1SubjectUniqueIdExpectedToBePractical)) {
            throw new PracticalSubjectForDayNotFoundException("Practical Subject Id can not be null in the programmeDay");
        } else {
            List<Integer> periodNumbersExpectedToBePracticalSubject = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
            int numberOfTimesPracticalSubjectFound = 0;
            for (PeriodOrLecture periodOrLecture : programmeDay.getPeriodList()) {
                if (periodNumbersExpectedToBePracticalSubject.contains(periodOrLecture.getPeriodNumber()) &&
                        Objects.equals(periodOrLecture.getSubjectUniqueIdInDb(), periodNumber1SubjectUniqueIdExpectedToBePractical)) {
                    numberOfTimesPracticalSubjectFound += 1;
                }
            }

            if (numberOfTimesPracticalSubjectFound != periodNumbersExpectedToBePracticalSubject.size()) {
                throw new PracticalSubjectForDayNotFoundException("Practical Subject Must be At least 6 types in day for the assumption to hold before programme can continue.");
            } else {
                List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList = new ArrayList<>();

                PeriodSetForProgrammeDay periodSetForProgrammeDay1 = new PeriodSetForProgrammeDay();
                periodSetForProgrammeDay1.setPeriodStartingNumber(1);
                periodSetForProgrammeDay1.setPeriodEndingNumber(6);
                periodSetForProgrammeDay1.setTotalNumberOfPeriodsForSet(6);

                PeriodSetForProgrammeDay periodSetForProgrammeDay2 = new PeriodSetForProgrammeDay();
                periodSetForProgrammeDay2.setPeriodStartingNumber(7);
                periodSetForProgrammeDay2.setPeriodEndingNumber(8);
                periodSetForProgrammeDay2.setTotalNumberOfPeriodsForSet(2);

                PeriodSetForProgrammeDay periodSetForProgrammeDay3 = new PeriodSetForProgrammeDay();
                periodSetForProgrammeDay3.setPeriodStartingNumber(9);
                periodSetForProgrammeDay3.setPeriodEndingNumber(10);
                periodSetForProgrammeDay3.setTotalNumberOfPeriodsForSet(2);

                periodSetForProgrammeDayList.addAll(new ArrayList<>(Arrays.asList(periodSetForProgrammeDay1, periodSetForProgrammeDay2, periodSetForProgrammeDay3)));
                return periodSetForProgrammeDayList;

            }
        }
    }

    /**
     * is programmeDay violated?
     * 1.if a periodSet in the programmeDay has one of the periods to be allocated,then all the periods in that period set must also be allocated,otherwise there is a violation.
     *
     * @param programmeDay                 the {@link ProgrammeDay} to check.
     * @param periodSetForProgrammeDayList the {@link List} of {@link PeriodSetForProgrammeDay} to check against
     * @return {@link Boolean#TRUE} if there is a violation.{@link Boolean#FALSE} if there is no violation.
     */
    @Deprecated
    public boolean isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay(ProgrammeDay programmeDay, List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList) {
        boolean isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay = false;
        if (programmeDayServices.isProgrammeDayFullyAllocated(programmeDay)) {
            //not violated because the programme day is fully allocated ,hence the check will be unnecessary .
            return isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay;
        }
        for (PeriodSetForProgrammeDay periodSetForProgrammeDay : periodSetForProgrammeDayList) {
            if (this.isProgrammeGroupDayPeriodSetViolatedInProgrammeDay(periodSetForProgrammeDay, programmeDay)) {
                isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay = true;
                break;
            }
        }
        return isProgrammeDayPeriodsViolatedAccordingToPeriodSetForDay;
    }

    /**
     * is {@link PeriodSetForProgrammeDay} violated in anyway?
     *
     * @param periodSetForProgrammeDay
     * @param programmeDay
     * @return {@link Boolean}
     */
    @Deprecated
    private boolean isProgrammeGroupDayPeriodSetViolatedInProgrammeDay(PeriodSetForProgrammeDay periodSetForProgrammeDay, ProgrammeDay programmeDay) {
        boolean isProgrammeGroupDayPeriodSetViolatedInProgrammeDay = false;

        int periodStartingNumber = periodSetForProgrammeDay.getPeriodStartingNumber();
        int periodEndingNumber = periodSetForProgrammeDay.getPeriodEndingNumber();
        int totalNumberOfPeriodsForSet = periodSetForProgrammeDay.getTotalNumberOfPeriodsForSet();

        List<PeriodOrLecture> periodOrLectureListThatFallWithinStartAndEndingNumberOfPeriodSet = new ArrayList<>();
        for (PeriodOrLecture currentPeriodOrLecture : programmeDay.getPeriodList()) {
            int currentPeriodOrLectureNumber = currentPeriodOrLecture.getPeriodNumber();
            if ((currentPeriodOrLectureNumber >= periodStartingNumber) &&
                    (currentPeriodOrLectureNumber <= periodEndingNumber)) {
                periodOrLectureListThatFallWithinStartAndEndingNumberOfPeriodSet.add(currentPeriodOrLecture);
            }
        }

        //check if any of the periods is allocated.
        boolean isAnyOfThePeriodsAllocated = false;
        for (PeriodOrLecture periodOrLectureInPeriodSetForProgrammeDay : periodOrLectureListThatFallWithinStartAndEndingNumberOfPeriodSet) {
            if (periodOrLectureInPeriodSetForProgrammeDay.getIsAllocated()) {
                isAnyOfThePeriodsAllocated = true;
                break;
            }
        }

        if (isAnyOfThePeriodsAllocated == true) {
            //if at least one is allocated,then check that all the other periods are also allocated.
            int numberOfTimesPeriodListIsAllocated = 0;
            for (PeriodOrLecture periodOrLectureInPeriodSetForProgrammeDay : periodOrLectureListThatFallWithinStartAndEndingNumberOfPeriodSet) {
                if (periodOrLectureInPeriodSetForProgrammeDay.getIsAllocated()) {
                    numberOfTimesPeriodListIsAllocated += 1;
                }
            }

            if (numberOfTimesPeriodListIsAllocated == totalNumberOfPeriodsForSet) {
                //numberOfTimesPeriodListIsAllocated is equal to totalNumberOfPeriodsForSet ,hence there is no violation so we return false;
                return isProgrammeGroupDayPeriodSetViolatedInProgrammeDay;
            } else {
                //number of times does not tally with totalNumberOfPeriodsForSet,hence set isProgrammeGroupDayPeriodSetViolatedInProgrammeDay = true,meaning there is a violation
                isProgrammeGroupDayPeriodSetViolatedInProgrammeDay = true;
                return isProgrammeGroupDayPeriodSetViolatedInProgrammeDay;
            }
        } else {
            //none of the periods are allocated ,hence no violation
            return isProgrammeGroupDayPeriodSetViolatedInProgrammeDay;
        }
    }

    private List<Integer> getListFromCustomProperties(String periodAllocationCombinationNumber) {
        List<String> finalStringList = Arrays.asList(periodAllocationCombinationNumber.split(","));
        List<Integer> finalIntegerList = new ArrayList<>(0);
        //convert to Integer list using streams
        finalIntegerList.addAll(finalStringList.stream().map(Integer::valueOf).collect(Collectors.toList()));
        return finalIntegerList;
    }
}
