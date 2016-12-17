package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.PeriodAndTimeAndSubjectAndTutorAssignedDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         17-Dec-16 @ 11:27 AM
 */
@Repository
public interface PeriodAndTimeAndSubjectAndTutorAssignedDocRepository extends MongoRepository<PeriodAndTimeAndSubjectAndTutorAssignedDoc,String> {

}
