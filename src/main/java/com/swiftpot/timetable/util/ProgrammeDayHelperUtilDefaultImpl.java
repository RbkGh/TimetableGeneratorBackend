package com.swiftpot.timetable.util;

import com.swiftpot.timetable.base.IProgrammeDayHelper;
import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 10:31 AM
 */
@Component
public class ProgrammeDayHelperUtilDefaultImpl implements IProgrammeDayHelper {
    @Override
    public boolean isProgrammeDayAlloted(ProgrammeDay programmeDay) {
        boolean isProgrammeDayAlloted;
        for (PeriodOrLecture periodOrLecture : programmeDay.getPeriodList()) {
            if (periodOrLecture.getIsAllocated() == false) {
                isProgrammeDayAlloted = false;
                return isProgrammeDayAlloted;
            }
        }
        isProgrammeDayAlloted = true;
        return isProgrammeDayAlloted;
    }
}
