package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         21-Dec-16 @ 4:18 PM
 */
@Service
public class ProgrammeDaysGenerator {

    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;

    public List<ProgrammeDay> generateAllProgrammeDays(String programmeCode) throws Exception {

        return timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
    }
}
