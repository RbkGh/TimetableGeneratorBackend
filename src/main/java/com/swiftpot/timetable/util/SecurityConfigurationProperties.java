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
 *         17-Apr-17 @ 5:04 AM
 */
@Component
@PropertySource("classpath:securityconfig.properties")
public class SecurityConfigurationProperties {
    /**
     * JWT SECRET MASTER KEY
     */
    @Value("${jwt.secret.key}")
    public String JWT_SECRET_MASTER_KEY;
}
