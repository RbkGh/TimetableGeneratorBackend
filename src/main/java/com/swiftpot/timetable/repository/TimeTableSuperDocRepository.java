package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         20-Dec-16 @ 7:59 PM
 */
public interface TimeTableSuperDocRepository extends MongoRepository<TimeTableSuperDoc,String> {
}
