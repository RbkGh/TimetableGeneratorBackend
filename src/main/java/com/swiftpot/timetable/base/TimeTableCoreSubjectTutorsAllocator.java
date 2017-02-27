package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;

/**
 * Super Class To Assign All Core Subject Tutors on to {@link com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Feb-17 @ 11:51 AM
 */
public interface TimeTableCoreSubjectTutorsAllocator {

    /**
     * <p>assign Subjects and classes of  all core tutors <br>
     * to the {@linkplain TimeTableSuperDoc} object</p>
     * <a><b>Preferrably,call </b>{@link TimeTableDefaultPeriodsAllocator#allocateDefaultPeriodsOnTimeTable(TimeTableSuperDoc)} <b>before </b><br>
     * <b>calling this method</b></a>
     *
     * @param timeTableSuperDocWithDefaultPeriodsSet
     * @return {@link TimeTableSuperDoc} with all core subject tutors assigned onto it.
     */
    TimeTableSuperDoc assignAllCoreSubjectTutorsOntoTimeTableSuperObject(TimeTableSuperDoc timeTableSuperDocWithDefaultPeriodsSet);
}
