/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.services;

import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Get subjects allocation as List
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 12:43 AM
 */
@Service
public class SubjectsAssignerService {

    @Autowired
    BusinessLogicConfigurationProperties propFile;

    /**
     * get subject periods allocation as a {@link List} of {@link Integer numbers},eg if 5 is passed in ,the allocation list will be {2,3}
     *
     * @param totalPeriodAllocationForSubject Period Allocation value to find the breakdown,eg 8 => 3,3,2
     * @return {@link List} of {@link Integer numbers} representing the breakdown of the subject allocations.
     */
    public List<Integer> getTotalSubjectPeriodAllocationAsList(int totalPeriodAllocationForSubject) {

        List<Integer> finalSubjectPeriodAllocationAsList;

        switch (totalPeriodAllocationForSubject) {
            case 1:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_1);
                break;
            case 2:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_2);
                break;
            case 3:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_3);
                break;
            case 4:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_4);
                break;
            case 5:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_5);
                break;
            case 6:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_6);
                break;
            case 7:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_7);
                break;
            case 8:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_8);
                break;
            case 9:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_9);
                break;
            case 10:
                finalSubjectPeriodAllocationAsList = getListFromCustomProperties(propFile.SUBJECT_TOTAL_PERIOD_10);
                break;
            default:
                throw new AssertionError("Only 1-10 accepted");

        }

        return finalSubjectPeriodAllocationAsList;
    }

    private List<Integer> getListFromCustomProperties(String totalPeriodAllocationForSubject) {
       List<String> finalStringList =   Arrays.asList(totalPeriodAllocationForSubject.split(","));
        List<Integer> finalIntegerList = new ArrayList<>(0);
        //convert to Integer list using streams
        finalIntegerList.addAll(finalStringList.stream().map(Integer::valueOf).collect(Collectors.toList()));
        return finalIntegerList;
    }

}
