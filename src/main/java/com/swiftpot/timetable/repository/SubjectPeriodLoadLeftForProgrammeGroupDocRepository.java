package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.SubjectPeriodLoadLeftForProgrammeGroupDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         27-Dec-16 @ 11:01 PM
 */
public interface SubjectPeriodLoadLeftForProgrammeGroupDocRepository extends MongoRepository<SubjectPeriodLoadLeftForProgrammeGroupDoc,String> {
    SubjectPeriodLoadLeftForProgrammeGroupDoc findByProgrammeCodeAndSubjectCode(String programmeCode,String subjectCode);
}
