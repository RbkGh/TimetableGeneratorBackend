package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.util.YearGroupNamesAndNumber;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 6:58 AM
 */
public class TimeTable {

    @Id
    private String id;

    private String yearGroupName;

    private String yearGroupNumber;

    private String programmeCode;

    private String programmeName;

    private List<PeriodOrLecture> periodOrLecture;

    public TimeTable() {

    }

    public String getId() {
        return id;
    }

    public TimeTable setId(String id) {
        this.id = id;
        return this;
    }

    public String getYearGroupName() {
        return yearGroupName;
    }

    public TimeTable setYearGroupName(String yearGroupName) {
        this.yearGroupName = yearGroupName;
        return this;
    }

    public String getYearGroupNumber() {
        return yearGroupNumber;
    }

    public TimeTable setYearGroupNumber(String yearGroupNumber) {
        this.yearGroupNumber = yearGroupNumber;
        return this;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public TimeTable setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
        return this;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public TimeTable setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
        return this;
    }

    public List<PeriodOrLecture> getPeriodOrLecture() {
        return periodOrLecture;
    }

    public TimeTable setPeriodOrLecture(List<PeriodOrLecture> periodOrLecture) {
        this.periodOrLecture = periodOrLecture;
        return this;
    }
}
