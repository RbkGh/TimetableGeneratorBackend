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


}
