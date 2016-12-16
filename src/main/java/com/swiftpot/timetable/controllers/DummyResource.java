package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.util.BusinessLogicConfigValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 9:01 PM
 */
@RestController
public class DummyResource {
    @Autowired
    BusinessLogicConfigValues businessLogicConfigValues;

    @RequestMapping(path = "/dummy",method= RequestMethod.GET)
    public String getDummyText(){
        System.out.println(businessLogicConfigValues.PERIOD_DURATION_IN_SECONDS);
        return "Hello";
    }

}
