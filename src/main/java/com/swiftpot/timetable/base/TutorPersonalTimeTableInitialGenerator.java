package com.swiftpot.timetable.base;

import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This interface will be used to generate the initial personal timetable for <br>
 * a {@link com.swiftpot.timetable.repository.db.model.TutorDoc} ,hence there will be days <br>
 * from eg. Monday to Friday that will have the periods for each day and the periods that the <br>
 * tutor has been assigned.This will keep track of the periods that have been assigned to tutor <br>
 * for a particular day,whether the tutor has exhausted the times he can teach in a day or not and so forth
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Mar-17 @ 8:01 PM
 */
@Component
public interface TutorPersonalTimeTableInitialGenerator {

    /**
     * this will retrieve all {@link com.swiftpot.timetable.repository.db.model.TutorDoc}s in <br>
     * database and generate {@link com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc}
     * for each. and save it into database<br>
     * <b>THIS SHOULD BE GENERATED AT THE BEGINNING OF GENERATION OF A TIMETABLE</b>
     */
    void generatePersonalTimeTableForAllTutorsInDbAndSaveIntoDb() throws Exception;

    /**
     * this will retrieve all {@link com.swiftpot.timetable.repository.db.model.TutorDoc}s in <br>
     * database and generate {@link com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc}
     * for each. without saving into database<br>
     */
    List<TutorPersonalTimeTableDoc> generatePersonalTimeTableForAllTutorsInDb(List<TutorDoc> tutorDocs) throws Exception;
}
