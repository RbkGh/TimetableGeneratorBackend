package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 9:16 PM
 */
public interface DepartmentDocRepository extends MongoRepository<DepartmentDoc,String> {
}
