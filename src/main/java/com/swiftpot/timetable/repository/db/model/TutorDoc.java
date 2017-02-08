package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.base.Person;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

    /**
     * the departmentId is the the dept unique Id
     * that the tutor belongs to,eg AB43R could be the
     * id of Mechanical Engineering Department
     */
    private String departmentId;

    /**
     * 2.Each Entity has the subjectId and a corresponding list of programmeCodes.
     */
    private List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectsAndProgrammeCodesList;

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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<TutorSubjectIdAndProgrammeCodesListObj> getTutorSubjectsAndProgrammeCodesList() {
        return tutorSubjectsAndProgrammeCodesList;
    }

    public void setTutorSubjectsAndProgrammeCodesList(List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectsAndProgrammeCodesList) {
        this.tutorSubjectsAndProgrammeCodesList = tutorSubjectsAndProgrammeCodesList;
    }
}
