package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.PeriodOrLecture;
import com.swiftpot.timetable.model.YearGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         20-Dec-16 @ 7:41 PM
 */
@Document(collection = "TimeTableSuperDoc")
public class TimeTableSuperDoc {

    @Id
    private String id;

    private List<YearGroup> yearGroupsList;

    public TimeTableSuperDoc() {

    }

    public List<YearGroup> getYearGroupsList() {
        return yearGroupsList;
    }

    public void setYearGroupsList(List<YearGroup> yearGroupsList) {
        this.yearGroupsList = yearGroupsList;
    }

}
