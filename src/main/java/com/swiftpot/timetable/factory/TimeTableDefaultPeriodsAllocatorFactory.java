/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.factory;

import com.swiftpot.timetable.base.TimeTableDefaultPeriodsAllocator;
import com.swiftpot.timetable.base.impl.TimeTableDefaultPeriodsAllocatorDefaultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 12:54 PM
 */
@Component
public class TimeTableDefaultPeriodsAllocatorFactory {

    @Autowired
    private TimeTableDefaultPeriodsAllocator timeTableDefaultPeriodsAllocator;
    @Autowired
    TimeTableDefaultPeriodsAllocatorDefaultImpl timeTableDefaultPeriodsAllocatorDefault;
    public static final String DEFAULT_IMPLEMENTATION = "DEFAULT";

    /**
     *
     * <br>get the specific implementation to be used</br>
     *
     */
    public TimeTableDefaultPeriodsAllocator getTimeTableDefaultPeriodsAllocator() {
        return this.timeTableDefaultPeriodsAllocator;
    }

    /**
     * @param timeTableDefaultPeriodsAllocatorType {@link #DEFAULT_IMPLEMENTATION} is a candidate for this method.
     * <br>set the specific implementation to be used</br>
     * <p>the options are "DEFAULT",more will be added as and when needed</p>
     */
    public void setTimeTableDefaultPeriodsAllocator(String timeTableDefaultPeriodsAllocatorType) {
        if (Objects.equals(timeTableDefaultPeriodsAllocatorType, DEFAULT_IMPLEMENTATION)) {
            this.timeTableDefaultPeriodsAllocator = timeTableDefaultPeriodsAllocatorDefault;
        } else {
            this.timeTableDefaultPeriodsAllocator = timeTableDefaultPeriodsAllocatorDefault;
        }
    }
    public TimeTableDefaultPeriodsAllocatorFactory() {
    }
}
