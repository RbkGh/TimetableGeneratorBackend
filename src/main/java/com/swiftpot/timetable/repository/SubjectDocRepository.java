package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 11:11 AM
 */
public interface SubjectDocRepository extends MongoRepository<SubjectDoc,String> {
    SubjectDoc findBySubjectCodeAllIgnoreCase(String subjectCode);
}
