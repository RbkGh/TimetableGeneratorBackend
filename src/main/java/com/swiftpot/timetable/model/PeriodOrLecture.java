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
    private String subjectCode;
    private String tutorCode;

    public PeriodOrLecture() {
    }

    public PeriodOrLecture(String periodStartandEndTime,int periodNumber,String periodName) {
        this.periodStartandEndTime = periodStartandEndTime;
        this.periodNumber = periodNumber;
        this.periodName = periodName;
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

    public boolean isAllocated() {
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
}
