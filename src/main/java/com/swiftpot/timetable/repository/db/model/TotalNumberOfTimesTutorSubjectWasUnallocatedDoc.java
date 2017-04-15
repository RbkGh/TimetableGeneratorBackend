/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Apr-17 @ 9:39 PM
 */
@Document(collection = "TotalNumberOfTimesTutorSubjectWasUnallocatedDoc")
public class TotalNumberOfTimesTutorSubjectWasUnallocatedDoc {
    @Id
    private String id;
    /**
     * total number of unallocated subjects for all tutors doc,increment by one anytime a
     * match is not found for a tutor's subject to be taught in a class
     */
    private int totalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue;
    /**
     * total number of unallocated subjects for all tutors doc,the best value so far
     */
    private int totalNumberOfTimesTutorSubjectWasUnallocatedBestValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue() {
        return totalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue;
    }

    public void setTotalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue(int totalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue) {
        this.totalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue = totalNumberOfTimesTutorSubjectWasUnallocatedCurrentValue;
    }

    public int getTotalNumberOfTimesTutorSubjectWasUnallocatedBestValue() {
        return totalNumberOfTimesTutorSubjectWasUnallocatedBestValue;
    }

    public void setTotalNumberOfTimesTutorSubjectWasUnallocatedBestValue(int totalNumberOfTimesTutorSubjectWasUnallocatedBestValue) {
        this.totalNumberOfTimesTutorSubjectWasUnallocatedBestValue = totalNumberOfTimesTutorSubjectWasUnallocatedBestValue;
    }
}
