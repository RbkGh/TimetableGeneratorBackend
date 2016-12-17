package com.swiftpot.timetable.repository.db.model;


import com.swiftpot.timetable.util.YearGroupNamesAndNumber;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 11:30 AM
 */
@Document
public class PeriodAndTimeAndSubjectAndTutorAssignedDoc {

    @Id
    private String id;

    private List<YearGroup> yearGroup = null;

    public List<YearGroup> getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(List<YearGroup> yearGroup) {
        this.yearGroup = yearGroup;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    /**
     * YearGroup with yearName as property
     * yearName-> eg. FORM2 OR YEAR2
     * yearNumber -> 1,2,3,or 4
     * programme -> list of programmes object,ie programmes offered in that particular year group
     */
    public class YearGroup {

        private YearGroupNamesAndNumber yearName;
        private int yearNumber;
        private List<Programme> programme = null;

        public YearGroupNamesAndNumber getYearName() {
            return yearName;
        }

        public void setYearName(YearGroupNamesAndNumber yearName) {
            this.yearName = yearName;
        }

        public int getYearNumber() {
            return yearNumber;
        }

        public void setYearNumber(int yearNumber) {
            this.yearNumber = yearNumber;
        }

        public List<Programme> getProgramme() {
            return programme;
        }

        public void setProgramme(List<Programme> programme) {
            this.programme = programme;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    /**
     * Programme Object,with
     * programCode,and periods List
     */
    public class Programme {

        private String programmeCode;
        private List<Period> periods = null;

        public String getProgrammeCode() {
            return programmeCode;
        }

        public void setProgrammeCode(String programmeCode) {
            this.programmeCode = programmeCode;
        }

        public List<Period> getPeriods() {
            return periods;
        }

        public void setPeriods(List<Period> periods) {
            this.periods = periods;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

    /**
     * Period Object
     * with periodName,periodNumber,
     * periodStartAndEndTime->eg 13:00-13:40GMT format,isAllocated type of boolean
     * subjectCode->SubjectInitials,
     * tutorCode->tutorCode eg 23,to retrieve Tutor info,like Name and details
     */
    public class Period {

        private String periodName;
        private int periodNumber;
        private String periodStartandEndTime;
        private boolean isAllocated;
        private String subjectCode;
        private String tutorCode;

        public String getPeriodName() {
            return periodName;
        }

        public void setPeriodName(String periodName) {
            this.periodName = periodName;
        }

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public String getPeriodStartandEndTime() {
            return periodStartandEndTime;
        }

        public void setPeriodStartandEndTime(String periodStartandEndTime) {
            this.periodStartandEndTime = periodStartandEndTime;
        }

        public boolean getIsAllocated() {
            return isAllocated;
        }

        public void setIsAllocated(boolean isAllocated) {
            this.isAllocated = isAllocated;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getTutorCode() {
            return tutorCode;
        }

        public void setTutorCode(String tutorCode) {
            this.tutorCode = tutorCode;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }
}
