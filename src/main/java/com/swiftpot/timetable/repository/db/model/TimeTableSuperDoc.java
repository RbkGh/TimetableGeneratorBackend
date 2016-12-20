package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.PeriodOrLecture;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         20-Dec-16 @ 7:41 PM
 */
@Document(collection = "TimeTableSuperDoc")
public class TimeTableSuperDoc {

    @Id
    private String id;

    private List<YearGroup> yearGroupsList;

    public TimeTableSuperDoc() {

    }

    public List<YearGroup> getYearGroupsList() {
        return yearGroupsList;
    }

    public void setYearGroupsList(List<YearGroup> yearGroupsList) {
        this.yearGroupsList = yearGroupsList;
    }

    public class YearGroup {
        private String yearName;
        private int yearNumber;
        private List<ProgrammeGroup> programmeGroupList;


        public String getYearName() {
            return yearName;
        }

        public void setYearName(String yearName) {
            this.yearName = yearName;
        }

        public int getYearNumber() {
            return yearNumber;
        }

        public void setYearNumber(int yearNumber) {
            this.yearNumber = yearNumber;
        }

        public List<ProgrammeGroup> getProgrammeGroupList() {
            return programmeGroupList;
        }

        public void setProgrammeGroupList(List<ProgrammeGroup> programmeGroupList) {
            this.programmeGroupList = programmeGroupList;
        }
    }

    public class ProgrammeGroup {
        private String programmeCode;
        private List<ProgrammeDays> programmeDaysList = null;


        public String getProgrammeCode() {
            return programmeCode;
        }

        public void setProgrammeCode(String programmeCode) {
            this.programmeCode = programmeCode;
        }

        public List<ProgrammeDays> getProgrammeDaysList() {
            return programmeDaysList;
        }

        public void setProgrammeDaysList(List<ProgrammeDays> programmeDaysList) {
            this.programmeDaysList = programmeDaysList;
        }
    }

    public class ProgrammeDays {
        private String dayName;
        private List<PeriodOrLecture> periodList;


        public String getDayName() {
            return dayName;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public List<PeriodOrLecture> getPeriodList() {
            return periodList;
        }

        public void setPeriodList(List<PeriodOrLecture> periodList) {
            this.periodList = periodList;
        }

    }

}
