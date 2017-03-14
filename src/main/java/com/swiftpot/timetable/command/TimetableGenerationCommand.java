/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command;

import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         14-Mar-17 @ 5:38 PM
 */
@Component
public interface TimetableGenerationCommand {
    /**
     * generate {@link TimeTableSuperDoc} object from database.
     *
     * @param timeTableSuperDoc The {@link TimeTableSuperDoc} object to operate on.
     * @return {@link TimeTableSuperDoc}
     */
    TimeTableSuperDoc executeTimeTableGenerationOperation(TimeTableSuperDoc timeTableSuperDoc) throws Exception;
}
