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
}
