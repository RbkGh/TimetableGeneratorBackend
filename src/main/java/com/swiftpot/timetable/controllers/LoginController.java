/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.*;
import com.swiftpot.timetable.repository.UserAccountDocRepository;
import com.swiftpot.timetable.repository.db.model.UserAccountDoc;
import com.swiftpot.timetable.security.JwtCreator;
import com.swiftpot.timetable.util.SecurityConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 9:01 PM
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    SecurityConfigurationProperties securityConfigurationProperties;
    @Autowired
    UserAccountDocRepository userAccountDocRepository;
    @Autowired
    private JwtCreator jwtCreator;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public OutgoingPayload login(@RequestBody UserAccountCredentials userAccountCredentials) throws ServletException, UnsupportedEncodingException {
        if (isUserCredentialsOK(userAccountCredentials)) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtCreator.createJWT(userAccountCredentials.getUserName()));
            return new SuccessfulOutgoingPayload(loginResponse);
        } else {
            return new ErrorOutgoingPayload("Invalid Credentials!");
        }
    }

    private boolean isUserCredentialsOK(UserAccountCredentials userAccountCredentials) {
        boolean isUserCredentialsOK = false;
        UserAccountDoc userAccountDoc =
                userAccountDocRepository.findByUserNameAndPassword(
                        userAccountCredentials.getUserName(), userAccountCredentials.getPassword()
                );
        if (Objects.nonNull(userAccountDoc)) {
            isUserCredentialsOK = true;
        }
        return isUserCredentialsOK;
    }
}
