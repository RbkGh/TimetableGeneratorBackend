package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.TutorDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Dec-16 @ 7:23 PM
 */
public interface TutorDocRepository extends MongoRepository<TutorDoc, String> {
    List<TutorDoc> findAll();

    TutorDoc findByTutorCode(String tutorCode);

    /**
     * find all tutors belonging to a particular department
     *
     * @param departmentId
     * @return
     */
    List<TutorDoc> findByDepartmentId(String departmentId);

}
