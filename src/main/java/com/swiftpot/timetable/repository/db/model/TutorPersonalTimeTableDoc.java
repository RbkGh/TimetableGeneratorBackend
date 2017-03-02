package com.swiftpot.timetable.repository.db.model;

import com.swiftpot.timetable.model.ProgrammeDay;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * This entity will be used to keep track of tutor's personal timetable during generation of {@link TimeTableSuperDoc}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Mar-17 @ 7:33 PM
 */
@Document(collection = "TutorPersonalTimeTableDoc")
public class TutorPersonalTimeTableDoc {

    /**
     * Tutor's List of {@link ProgrammeDay} this is equivalent to the <br>
     * same days that are available on the {@link TimeTableSuperDoc}(ie the list of {@link ProgrammeDay} on the <br>
     * {@link com.swiftpot.timetable.model.ProgrammeGroup } object on the timetable object.
     */
    private List<ProgrammeDay> programmeDaysList;

    public List<ProgrammeDay> getProgrammeDaysList() {
        return programmeDaysList;
    }

    public void setProgrammeDaysList(List<ProgrammeDay> programmeDaysList) {
        this.programmeDaysList = programmeDaysList;
    }
}
