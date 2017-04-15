/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.UnallocatedTutorsDetailsDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Apr-17 @ 9:27 PM
 */
public interface UnallocatedTutorsDetailsDocRepository extends MongoRepository<UnallocatedTutorsDetailsDoc, String> {
}
