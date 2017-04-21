/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.security;

import com.swiftpot.timetable.util.SecurityConfigurationProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         19-Apr-17 @ 6:28 AM
 */
@Component
public class JwtCreator {

    @Autowired
    private SecurityConfigurationProperties securityConfigurationProperties;

    /**
     * create jwt with userName and master key
     *
     * @param userName
     * @return
     */
    public String createJWT(String userName) throws UnsupportedEncodingException {
        String secretMasterKey = securityConfigurationProperties.JWT_SECRET_MASTER_KEY;
        String encodedMasterKeyString = Base64.getEncoder().encodeToString(secretMasterKey.getBytes("utf-8"));
//        Date dateOfExpiry = Date.from(LocalDate.now().plus(Period.ofYears(1)).atStartOfDay(ZoneId.systemDefault()).toInstant());//1 year from current date
        return Jwts.builder().setSubject(userName)
                .setIssuedAt(new Date()).claim(JWTFilter.CLAIMS_ATTRIBUTE, userName)
//                .setExpiration(dateOfExpiry)
                .signWith(SignatureAlgorithm.HS256, encodedMasterKeyString).compact();
    }
}
