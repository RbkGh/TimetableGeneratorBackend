package com.swiftpot.timetable.model;


import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * @author Ace Programmer Rbk
 * <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 * 17-Dec-16 @ 11:13 AM
 */

/**
 * PeriodOrLecture Object
 * with periodName,periodNumber,
 * periodStartAndEndTime->eg 13:00-13:40GMT format,isAllocated type of boolean
 * subjectCode->SubjectInitials,
 * tutorCode->tutorCode eg 23,to retrieve Tutor info,like Name and details
 */
public class PeriodOrLecture {

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

    public PeriodOrLecture() {
    }

    public PeriodOrLecture(String periodStartandEndTime, int periodNumber, String periodName) {
        this.periodStartandEndTime = periodStartandEndTime;
        this.periodNumber = periodNumber;
        this.periodName = periodName;
    }

    public PeriodOrLecture(boolean isAllocated, String subjectCode, String tutorCode) {
        this.isAllocated = isAllocated;
        this.subjectCode = subjectCode;
        this.tutorCode = tutorCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}