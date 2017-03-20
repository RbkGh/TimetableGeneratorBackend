/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * The final timetable object to be returned to client.
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Mar-17 @ 2:37 PM
 */
@Document(collection = "TimeTableMainDoc")
public class TimeTableMainDoc {

    @Id
    private String id;

    int year;

    String timeTableName;

    /**
     * list of {@link TutorPersonalTimeTableDoc}
     */
    List<TutorPersonalTimeTableDoc> tutorPersonalTimeTableDocs;

    /**
     * List of {@link ProgrammeGroupPersonalTimeTableDoc}
     */
    List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public TimeTableMainDoc setYear(int year) {
        this.year = year;
        return this;
    }

    public String getTimeTableName() {
        return timeTableName;
    }

    public TimeTableMainDoc setTimeTableName(String timeTableName) {
        this.timeTableName = timeTableName;
        return this;
    }

    public List<TutorPersonalTimeTableDoc> getTutorPersonalTimeTableDocs() {
        return tutorPersonalTimeTableDocs;
    }

    public TimeTableMainDoc setTutorPersonalTimeTableDocs(List<TutorPersonalTimeTableDoc> tutorPersonalTimeTableDocs) {
        this.tutorPersonalTimeTableDocs = tutorPersonalTimeTableDocs;
        return this;
    }

    public List<ProgrammeGroupPersonalTimeTableDoc> getProgrammeGroupPersonalTimeTableDocs() {
        return programmeGroupPersonalTimeTableDocs;
    }

    public TimeTableMainDoc setProgrammeGroupPersonalTimeTableDocs(List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocs) {
        this.programmeGroupPersonalTimeTableDocs = programmeGroupPersonalTimeTableDocs;
        return this;
    }
}
