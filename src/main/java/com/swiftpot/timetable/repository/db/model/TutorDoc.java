package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.base.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 2:11 PM
 */
@Document
public class TutorDoc extends Person {

    @Id
    private String id;

    private String tutorCode;

    private int currentPeriodLoad;

    private int minPeriodLoad;

    private int maxPeriodLoad;

    private String assignedYearGroup;

    private TutorSubjectSpeciality tutorSubjectSpeciality;

    public enum TutorSubjectSpeciality {
        CORE_TUTOR("Core Tutor"), ELECTIVE_TUTOR("Elective Tutor");
        String tutorSubjectSpecialityFullName;

        private TutorSubjectSpeciality(String tutorSubjectSpecialityFullName) {
            this.tutorSubjectSpecialityFullName = tutorSubjectSpecialityFullName;
        }
    }

    public TutorDoc() {
        super();
    }


    public String getTutorCode() {
        return tutorCode;
    }

    public TutorDoc setTutorCode(String tutorCode) {
        this.tutorCode = tutorCode;
        return this;
    }

    public int getCurrentPeriodLoad() {
        return currentPeriodLoad;
    }

    public TutorDoc setCurrentPeriodLoad(int currentPeriodLoad) {
        this.currentPeriodLoad = currentPeriodLoad;
        return this;
    }

    public int getMinPeriodLoad() {
        return minPeriodLoad;
    }

    public TutorDoc setMinPeriodLoad(int minPeriodLoad) {
        this.minPeriodLoad = minPeriodLoad;
        return this;
    }

    public int getMaxPeriodLoad() {
        return maxPeriodLoad;
    }

    public TutorDoc setMaxPeriodLoad(int maxPeriodLoad) {
        this.maxPeriodLoad = maxPeriodLoad;
        return this;
    }

    public String getAssignedYearGroup() {
        return assignedYearGroup;
    }

    public TutorDoc setAssignedYearGroup(String assignedYearGroup) {
        this.assignedYearGroup = assignedYearGroup;
        return this;
    }

    public TutorSubjectSpeciality getTutorSubjectSpeciality() {
        return tutorSubjectSpeciality;
    }

    public TutorDoc setTutorSubjectSpeciality(TutorSubjectSpeciality tutorSubjectSpeciality) {
        this.tutorSubjectSpeciality = tutorSubjectSpeciality;
        return this;
    }
}
