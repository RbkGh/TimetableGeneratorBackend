/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         23-Dec-16 @ 9:36 PM
 */
@Document(collection = "PracticalsClassroomDoc")
public class PracticalsClassroomDoc {

    @Id
    private String id;

    private String practicalsClassRoomCode ;

    /**
     * this is the maxCapacity of days the lab can be used,typically 5 days ,since the whole day is used or
     * allocated for practicals
     */
    private int maxDaysCapacityForWeek;

    /**
     * always decrease by 1 till it reaches 0,initially set it as equal to maxCapacityForWeek
     */
    private int remainingDaysToBeAllocated;

    /**
     * Set this to true only when the Practicals Classroom is fully allocated for all the ProgrammeDays list
     */
    private boolean isPracticalsClassroomFullyAllocated;

    /**
     * we shall use only the isAllocated field in {@link ProgrammeDay}
     */
    private List<ProgrammeDay> programmeDaysList;

    public PracticalsClassroomDoc() {
    }
    /**
     * Initially during invocation,set programmeDayListmaxCapacityForWeek and remainingDaysToBeAllocated to be same
     * ...only during first instantiation
     * @param programmeDaysList
     * @param maxDaysCapacityForWeek
     * @param remainingDaysToBeAllocated
     */
    public PracticalsClassroomDoc(List<ProgrammeDay> programmeDaysList,int maxDaysCapacityForWeek,int remainingDaysToBeAllocated) {
        this.programmeDaysList = programmeDaysList;
        this.maxDaysCapacityForWeek = maxDaysCapacityForWeek;
        this.remainingDaysToBeAllocated = remainingDaysToBeAllocated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPracticalsClassRoomCode() {
        return practicalsClassRoomCode;
    }

    public void setPracticalsClassRoomCode(String practicalsClassRoomCode) {
        this.practicalsClassRoomCode = practicalsClassRoomCode;
    }

    public int getMaxDaysCapacityForWeek() {
        return maxDaysCapacityForWeek;
    }

    public void setMaxDaysCapacityForWeek(int maxDaysCapacityForWeek) {
        this.maxDaysCapacityForWeek = maxDaysCapacityForWeek;
    }

    public int getRemainingDaysToBeAllocated() {
        return remainingDaysToBeAllocated;
    }

    public void setRemainingDaysToBeAllocated(int remainingDaysToBeAllocated) {
        this.remainingDaysToBeAllocated = remainingDaysToBeAllocated;
    }

    public boolean isPracticalsClassroomFullyAllocated() {
        return isPracticalsClassroomFullyAllocated;
    }

    public void setIsPracticalsClassroomFullyAllocated(boolean isPracticalsClassroomFullyAllocated) {
        this.isPracticalsClassroomFullyAllocated = isPracticalsClassroomFullyAllocated;
    }

    public List<ProgrammeDay> getProgrammeDaysList() {
        return programmeDaysList;
    }

    public void setProgrammeDaysList(List<ProgrammeDay> programmeDayList) {
        this.programmeDaysList = programmeDayList;
    }
}
