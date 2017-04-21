/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.repository;

import com.swiftpot.timetable.repository.db.model.UserAccountDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Apr-17 @ 3:53 PM
 */
public interface UserAccountDocRepository extends MongoRepository<UserAccountDoc, String> {
    UserAccountDoc findByUserName(String userName);

    /**
     * find {@link UserAccountDoc#userName} & {@link UserAccountDoc#password}
     *
     * @param userName
     * @param password
     * @return
     */
    UserAccountDoc findByUserNameAndPassword(String userName, String password);
}
