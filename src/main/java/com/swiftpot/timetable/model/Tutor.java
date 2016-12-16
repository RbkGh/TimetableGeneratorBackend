package com.swiftpot.timetable.model;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 2:11 PM
 */
public class Tutor {

    private String tutorCode;

    private int currentPeriodLoad;

    private int minPeriodLoad;

    private int maxPeriodLoad;

    private TutorSubjectSpeciality tutorSubjectSpeciality;

    public enum TutorSubjectSpeciality {
        CORE_TUTOR("Core Tutor"), ELECTIVE_TUTOR("Elective Tutor");
        String tutorSubjectSpecialityFullName;

        private TutorSubjectSpeciality(String tutorSubjectSpecialityFullName) {
            this.tutorSubjectSpecialityFullName = tutorSubjectSpecialityFullName;
        }
    }

    public Tutor() {
    }

    public String getTutorCode() {
        return tutorCode;
    }

    public Tutor setTutorCode(String tutorCode) {
        this.tutorCode = tutorCode;
        return this;
    }

    public int getCurrentPeriodLoad() {
        return currentPeriodLoad;
    }

    public Tutor setCurrentPeriodLoad(int currentPeriodLoad) {
        this.currentPeriodLoad = currentPeriodLoad;
        return this;
    }

    public int getMinPeriodLoad() {
        return minPeriodLoad;
    }

    public Tutor setMinPeriodLoad(int minPeriodLoad) {
        this.minPeriodLoad = minPeriodLoad;
        return this;
    }

    public int getMaxPeriodLoad() {
        return maxPeriodLoad;
    }

    public Tutor setMaxPeriodLoad(int maxPeriodLoad) {
        this.maxPeriodLoad = maxPeriodLoad;
        return this;
    }

    public TutorSubjectSpeciality getTutorSubjectSpeciality() {
        return tutorSubjectSpeciality;
    }

    public Tutor setTutorSubjectSpeciality(TutorSubjectSpeciality tutorSubjectSpeciality) {
        this.tutorSubjectSpeciality = tutorSubjectSpeciality;
        return this;
    }
}
