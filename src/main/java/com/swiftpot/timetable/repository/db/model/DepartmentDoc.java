package com.swiftpot.timetable.repository.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 6:39 PM
 */
@Document
public class DepartmentDoc {

    @Id
    private String id;

    private String deptName;

    private List<String> tutorCodeList;

    /**
     * HOD tutorCode
     */
    private String deptHODtutorCode;

    /**
     * assistant HOD tutorCode
     */
    private String deptHODdeputyTutorCode;
}
