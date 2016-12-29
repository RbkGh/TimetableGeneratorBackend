package com.swiftpot.timetable.base;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 10:08 AM
 */
@Component
public interface IProgrammeDayHelper {

    boolean isProgrammeDayAlloted(ProgrammeDay programmeDay);
}
