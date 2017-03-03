package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Mar-17 @ 8:44 PM
 */
public interface TutorPersonalTimeTableDocRepository extends MongoRepository<TutorPersonalTimeTableDoc, String> {
    TutorPersonalTimeTableDoc findByTutorUniqueIdInDb(String tutorUniqueIdInDb);
}
