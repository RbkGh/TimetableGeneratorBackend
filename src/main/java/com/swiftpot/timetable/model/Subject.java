package com.swiftpot.timetable.model;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 3:28 PM
 */
public class Subject {

    private String subjectFullName;

    private String subjectCode;

    private int totalPeriodsForYearGroup;

    /**
     *@param subjectYearGroupList
     * YearGroups offering this Subject =>1,2,3 meaning all 3 year groups offer the subject
     */
    private List<Integer> subjectYearGroupList;

    public enum SubjectType {
        CORE_SUBJECT("Core Subject"),
        ELECTIVE_SUBJECT("Elective Subject");
        String subjectTypeFullName;

        SubjectType(String subjectTypeFullName) {
            this.subjectTypeFullName = subjectTypeFullName;
        }

    }


}
