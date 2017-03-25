/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         20-Dec-16 @ 8:45 PM
 */
public class PeriodOrLecture {
    private String periodName;
    private int periodNumber;
    private String periodStartandEndTime;
    private boolean isAllocated;

    /**
     * the unique {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#id id} of {@link com.swiftpot.timetable.repository.db.model.SubjectDoc subjectDoc } object.
     */
    private String subjectUniqueIdInDb;

    /**
     * the unique {@link com.swiftpot.timetable.repository.db.model.TutorDoc#id id} of {@link com.swiftpot.timetable.repository.db.model.TutorDoc tutorDoc } object.
     */
    private String tutorUniqueId;

    /**
     * the full {@link com.swiftpot.timetable.repository.db.model.TutorDoc#firstName} + {@link com.swiftpot.timetable.repository.db.model.TutorDoc#surName}
     * this is set only at the final generation stage. when returning to the user.although setting it at anytime with the correct value is also good ,doesn't spoil anything.
     */
    private String tutorFullName;

    /**
     * the {@link com.swiftpot.timetable.repository.db.model.SubjectDoc#subjectFullName}
     * this is set only at the final generation stage. when returning to the user.although setting it at anytime with the correct value is also good ,doesn't spoil anything.
     */
    private String subjectFullName;

    /**
     * this will only be used to set the {@link com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc}'s periods,to show the specific class that the tutor is teaching at a particular moment.
     */
    private String programmeCodeThatTutorIsTeaching;

    public PeriodOrLecture() {
    }

    /**
     * This will be used in firstTime initialization when we are generating the initial data only
     *
     * @param periodStartandEndTime
     * @param periodNumber
     * @param periodName
     */
    public PeriodOrLecture(String periodStartandEndTime, int periodNumber, String periodName) {
        this.periodStartandEndTime = periodStartandEndTime;
        this.periodNumber = periodNumber;
        this.periodName = periodName;
    }

    public PeriodOrLecture(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

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

    public String getSubjectUniqueIdInDb() {
        return subjectUniqueIdInDb;
    }

    public void setSubjectUniqueIdInDb(String subjectUniqueIdInDb) {
        this.subjectUniqueIdInDb = subjectUniqueIdInDb;
    }

    public String getTutorUniqueId() {
        return tutorUniqueId;
    }

    public void setTutorUniqueId(String tutorUniqueId) {
        this.tutorUniqueId = tutorUniqueId;
    }

    public String getTutorFullName() {
        return tutorFullName;
    }

    public PeriodOrLecture setTutorFullName(String tutorFullName) {
        this.tutorFullName = tutorFullName;
        return this;
    }

    public String getSubjectFullName() {
        return subjectFullName;
    }

    public PeriodOrLecture setSubjectFullName(String subjectFullName) {
        this.subjectFullName = subjectFullName;
        return this;
    }

    /**
     * @return {@link PeriodOrLecture#programmeCodeThatTutorIsTeaching}
     * @see PeriodOrLecture#programmeCodeThatTutorIsTeaching
     */
    public String getProgrammeCodeThatTutorIsTeaching() {
        return programmeCodeThatTutorIsTeaching;
    }

    /**
     * @see PeriodOrLecture#programmeCodeThatTutorIsTeaching
     */
    public void setProgrammeCodeThatTutorIsTeaching(String programmeCodeThatTutorIsTeaching) {
        this.programmeCodeThatTutorIsTeaching = programmeCodeThatTutorIsTeaching;
    }
}
