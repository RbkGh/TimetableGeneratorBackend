package com.swiftpot.timetable.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 1:58 PM
 */
public final class YearGroupNumberAndNames {

    public YearGroupNumberAndNames() {
    }

    public static String getYearGroupName(int yearGroupNumber) {
        Map<Integer, String> yearGroupNumberAndNames;
        yearGroupNumberAndNames = new HashMap<>();
        yearGroupNumberAndNames.put(1, "Form One");
        yearGroupNumberAndNames.put(2, "Form Two");
        yearGroupNumberAndNames.put(3, "Form Three");
        yearGroupNumberAndNames.put(4, "Form Four");
        yearGroupNumberAndNames.put(5, "Form Five");
        yearGroupNumberAndNames.put(6, "Form Six");
        return yearGroupNumberAndNames.get(yearGroupNumber);
    }

}
