/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.invokers;

import com.sun.istack.internal.Nullable;
import com.swiftpot.timetable.command.TimetableGenerationCommand;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 7:56 AM
 */
@Component
public class TimeTableGenerationInvoker {

    TimetableGenerationCommand timetableGenerationCommand;

    public TimeTableGenerationInvoker(TimetableGenerationCommand timetableGenerationCommand) {
        this.timetableGenerationCommand = timetableGenerationCommand;
    }

    public TimeTableSuperDoc executeTimeTableGenerationOperation(@Nullable TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        return this.timetableGenerationCommand.executeTimeTableGenerationOperation(timeTableSuperDoc);
    }
}
