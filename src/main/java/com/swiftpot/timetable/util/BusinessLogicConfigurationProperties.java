/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 4:13 PM
 */
@Component
@PropertySource("classpath:config.properties")
public class BusinessLogicConfigurationProperties {

    /**
     * period or duration in Seconds
     */
    @Value("${period.duration}")
    public String PERIOD_DURATION_IN_SECONDS;

    @Value("${subject.total.period.1}")
    public String SUBJECT_TOTAL_PERIOD_1;

    @Value("${subject.total.period.2}")
    public String SUBJECT_TOTAL_PERIOD_2;

    @Value("${subject.total.period.3}")
    public String SUBJECT_TOTAL_PERIOD_3;

    @Value("${subject.total.period.4}")
    public String SUBJECT_TOTAL_PERIOD_4;

    @Value("${subject.total.period.5}")
    public String SUBJECT_TOTAL_PERIOD_5;

    @Value("${subject.total.period.6}")
    public String SUBJECT_TOTAL_PERIOD_6;

    @Value("${subject.total.period.7}")
    public String SUBJECT_TOTAL_PERIOD_7;

    @Value("${subject.total.period.8}")
    public String SUBJECT_TOTAL_PERIOD_8;

    @Value("${subject.total.period.9}")
    public String SUBJECT_TOTAL_PERIOD_9;

    @Value("${subject.total.period.10}")
    public String SUBJECT_TOTAL_PERIOD_10;

    //timetable periods
    @Value("${timetable.period.1}")
    public String TIMETABLE_PERIO_1;

    @Value("${timetable.period.2}")
    public String TIMETABLE_PERIO_2;

    @Value("${timetable.period.3}")
    public String TIMETABLE_PERIO_3;

    @Value("${timetable.period.4}")
    public String TIMETABLE_PERIO_4;

    @Value("${timetable.period.5}")
    public String TIMETABLE_PERIO_5;

    @Value("${timetable.period.6}")
    public String TIMETABLE_PERIO_6;

    @Value("${timetable.period.7}")
    public String TIMETABLE_PERIO_7;

    @Value("${timetable.period.8}")
    public String TIMETABLE_PERIO_8;

    @Value("${timetable.period.9}")
    public String TIMETABLE_PERIO_9;

    @Value("${timetable.period.10}")
    public String TIMETABLE_PERIO_10;
    //timetable periods

    /**
     * total periods for day
     */
    @Value("${timetable.period.total}")
    public String TIMETABLE_PERIOD_TOTAL;

    @Value("${timetable.day.1}")
    public String TIMETABLE_DAY_1;

    @Value("${timetable.day.2}")
    public String TIMETABLE_DAY_2;

    @Value("${timetable.day.3}")
    public String TIMETABLE_DAY_3;

    @Value("${timetable.day.4}")
    public String TIMETABLE_DAY_4;

    @Value("${timetable.day.5}")
    public String TIMETABLE_DAY_5;

    @Value("${timetable.day.6}")
    public String TIMETABLE_DAY_6;

    @Value("${timetable.day.7}")
    public String TIMETABLE_DAY_7;

    /**
     * total days timetable is applicable
     */
    @Value("${timetable.day.total}")
    public String TIMETABLE_DAY_TOTAL;

    @Value("${timetable.day.worship}")
    public int TIMETABLE_DAY_WORSHIP;

    @Value("${timetable.period.worship}")
    public int TIMETABLE_PERIOD_WORSHIP;

    @Value("${timetable.day.classmeeting}")
    public int TIMETABLE_DAY_CLASS_MEETING;

    @Value("${timetable.period.classmeeting}")
    public int TIMETABLE_PERIOD_CLASS_MEETING;

    @Value("${day.period.allocation.combination.1}")
    public String DAY_PERIOD_ALLOCATION_COMBINATION_1;

    @Value("${day.period.allocation.combination.2}")
    public String DAY_PERIOD_ALLOCATION_COMBINATION_2;

    @Value("${day.period.allocation.combination.3}")
    public String DAY_PERIOD_ALLOCATION_COMBINATION_3;


}
