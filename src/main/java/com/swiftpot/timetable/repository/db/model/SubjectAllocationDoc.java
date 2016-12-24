package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         23-Dec-16 @ 2:16 PM
 */
@Document(collection = "SubjectAllocationDoc")
public class SubjectAllocationDoc {

    @Id
    private String id;

    private String subjectCode;

    private int totalSubjectAllocation;

    private int yearGroup;
}
