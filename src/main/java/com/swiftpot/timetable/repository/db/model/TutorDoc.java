package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.base.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Dec-16 @ 2:11 PM
 */
@Document(collection = "TutorDoc")
public class TutorDoc extends Person {

    @Id
    private String id;

    private String tutorCode;

    private int currentPeriodLoadLeft;

    private int minPeriodLoad;

    private int maxPeriodLoad;

    private String assignedYearGroup;

    /**
     * either "CORE" or "ELECTIVE"
     */
    private String tutorSubjectSpeciality;



    public TutorDoc() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTutorCode() {
        return tutorCode;
    }

    public TutorDoc setTutorCode(String tutorCode) {
        this.tutorCode = tutorCode;
        return this;
    }



    public int getMinPeriodLoad() {
        return minPeriodLoad;
    }

    public int getCurrentPeriodLoadLeft() {
        return currentPeriodLoadLeft;
    }

    public void setCurrentPeriodLoadLeft(int currentPeriodLoadLeft) {
        this.currentPeriodLoadLeft = currentPeriodLoadLeft;
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

    public String getTutorSubjectSpeciality() {
        return tutorSubjectSpeciality;
    }

    public TutorDoc setTutorSubjectSpeciality(String tutorSubjectSpeciality) {
        this.tutorSubjectSpeciality = tutorSubjectSpeciality;
        return this;
    }
}
