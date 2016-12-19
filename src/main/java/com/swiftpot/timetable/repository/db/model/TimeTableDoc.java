package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.PeriodOrLecture;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 6:58 AM
 */
public class TimeTableDoc {

    @Id
    private String id;

    private String yearGroupName;

    private String yearGroupNumber;

    private String programmeCode;

    private String programmeName;

    private List<PeriodOrLecture> periodOrLecture;

    public TimeTableDoc() {

    }

    public String getId() {
        return id;
    }

    public TimeTableDoc setId(String id) {
        this.id = id;
        return this;
    }

    public String getYearGroupName() {
        return yearGroupName;
    }

    public TimeTableDoc setYearGroupName(String yearGroupName) {
        this.yearGroupName = yearGroupName;
        return this;
    }

    public String getYearGroupNumber() {
        return yearGroupNumber;
    }

    public TimeTableDoc setYearGroupNumber(String yearGroupNumber) {
        this.yearGroupNumber = yearGroupNumber;
        return this;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public TimeTableDoc setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
        return this;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public TimeTableDoc setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
        return this;
    }

    public List<PeriodOrLecture> getPeriodOrLecture() {
        return periodOrLecture;
    }

    public TimeTableDoc setPeriodOrLecture(List<PeriodOrLecture> periodOrLecture) {
        this.periodOrLecture = periodOrLecture;
        return this;
    }
}
