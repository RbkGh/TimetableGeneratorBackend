/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.TimeTableMainDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         16-Mar-17 @ 4:09 PM
 */
public interface TimeTableMainDocRepository extends MongoRepository<TimeTableMainDoc, String> {
}
