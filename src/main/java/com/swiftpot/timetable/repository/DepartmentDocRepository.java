/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 9:16 PM
 */
public interface DepartmentDocRepository extends MongoRepository<DepartmentDoc,String> {
    DepartmentDoc findByDeptProgrammeInitials(String deptProgrammeInitials);

    /**
     * find by {@link DepartmentDoc#deptType}
     * qualifying candidates are {@link DepartmentDoc#DEPARTMENT_TYPE_CORE} or {@link DepartmentDoc#DEPARTMENT_TYPE_ELECTIVE}
     *
     * @param deptType QUALIFYING CANDIDATES ARE {@link DepartmentDoc#DEPARTMENT_TYPE_CORE} or {@link DepartmentDoc#DEPARTMENT_TYPE_ELECTIVE} ONLY
     * @return {@link List} of {@link DepartmentDoc}
     */
    List<DepartmentDoc> findByDeptType(String deptType);
}
