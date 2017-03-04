package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 8:39 PM
 */
@Document(collection = "ProgrammeGroupPersonalTimeTableDoc")
public class ProgrammeGroupPersonalTimeTableDoc {

    /**
     * this is the {@link com.swiftpot.timetable.model.ProgrammeGroup#programmeCode} existent on
     * the {@link com.swiftpot.timetable.model.ProgrammeGroup} on {@link TimeTableSuperDoc} object.
     */
    private String programmeCode;

    /**
     * ProgrammeGroup's List of {@link ProgrammeDay} ,this is equivalent to the <br>
     * same days that are available on the {@link TimeTableSuperDoc}(ie the list of {@link ProgrammeDay} on the <br>
     * {@link com.swiftpot.timetable.model.ProgrammeGroup } object on the timetable object.
     */
    private List<ProgrammeDay> programmeDaysList;

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public List<ProgrammeDay> getProgrammeDaysList() {
        return programmeDaysList;
    }

    public void setProgrammeDaysList(List<ProgrammeDay> programmeDaysList) {
        this.programmeDaysList = programmeDaysList;
    }
}
