/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Mar-17 @ 1:01 PM
 */
public class CustomServletInitializer extends SpringBootServletInitializer {
    /**
     * allow war file generation by extending {@link SpringBootServletInitializer} and
     * overriding configure,apply plugin 'war' in gradle config too
     *
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TimetableTechnicalApplication.class);
    }
}
