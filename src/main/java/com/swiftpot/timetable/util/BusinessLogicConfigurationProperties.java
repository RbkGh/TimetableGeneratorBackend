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
}
