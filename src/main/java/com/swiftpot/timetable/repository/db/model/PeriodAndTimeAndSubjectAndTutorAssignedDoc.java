/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository.db.model;


import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.util.YearGroupNumberAndNames;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 11:30 AM
 */
@Document(collection = "PeriodAndTimeAndSubjectAndTutorAssignedDoc")
public class PeriodAndTimeAndSubjectAndTutorAssignedDoc {

    @Id
    private String id;

    private List<YearGroup> yearGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

        private YearGroupNumberAndNames yearName;
        private int yearNumber;
        private List<Programme> programme = null;

        public YearGroupNumberAndNames getYearName() {
            return yearName;
        }

        public void setYearName(YearGroupNumberAndNames yearName) {
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
     * programCode,and periodOrLectures List
     */
    public class Programme {

        private String programmeCode;
        private List<PeriodOrLecture> periodOrLectures = null;

        public String getProgrammeCode() {
            return programmeCode;
        }

        public void setProgrammeCode(String programmeCode) {
            this.programmeCode = programmeCode;
        }

        public List<PeriodOrLecture> getPeriodOrLectures() {
            return periodOrLectures;
        }

        public void setPeriodOrLectures(List<PeriodOrLecture> periodOrLectures) {
            this.periodOrLectures = periodOrLectures;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }


}
