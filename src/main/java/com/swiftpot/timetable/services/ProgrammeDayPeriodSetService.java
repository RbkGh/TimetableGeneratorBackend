/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private List<Integer> getListFromCustomProperties(String periodAllocationCombinationNumber) {
        List<String> finalStringList = Arrays.asList(periodAllocationCombinationNumber.split(","));
        List<Integer> finalIntegerList = new ArrayList<>(0);
        //convert to Integer list using streams
        finalIntegerList.addAll(finalStringList.stream().map(Integer::valueOf).collect(Collectors.toList()));
        return finalIntegerList;
    }
}
