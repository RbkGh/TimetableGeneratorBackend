package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         23-Dec-16 @ 2:19 PM
 */
public interface SubjectAllocationDocRepository extends MongoRepository<SubjectAllocationDoc, String> {
    SubjectAllocationDoc findBySubjectCodeAndYearGroup(String subjectCode,int yearGroup);
    List<SubjectAllocationDoc> findBySubjectCode(String subjectCode);
}
