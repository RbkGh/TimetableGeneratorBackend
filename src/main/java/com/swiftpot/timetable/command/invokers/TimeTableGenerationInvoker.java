/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.command.invokers;

import com.sun.istack.Nullable;
import com.swiftpot.timetable.command.commands.commandfactory.TimeTableGenerationCommandFactory;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         15-Mar-17 @ 7:56 AM
 */
@Component
public class TimeTableGenerationInvoker {

    @Autowired
    private TimeTableGenerationCommandFactory timeTableGenerationCommandFactory;

    private String timeTableGenerationImplType;


    public TimeTableGenerationInvoker() {

    }

    public TimeTableSuperDoc executeTimeTableGenerationOperation(@Nullable TimeTableSuperDoc timeTableSuperDoc) throws Exception {
        return this.
                timeTableGenerationCommandFactory.
                getTimeTableGenerationCommandImpl(this.timeTableGenerationImplType).
                executeTimeTableGenerationOperation(timeTableSuperDoc);
    }

    /**
     * @param timeTableGenerationImplType type must be one of the static final Strings in {@link TimeTableGenerationCommandFactory} <br>
     *                                    eg. {@link TimeTableGenerationCommandFactory#RESET_TEMPORARY_DATABASE_ENTITIES}
     */
    public void setTimeTableGenerationImplType(String timeTableGenerationImplType) {
        this.timeTableGenerationImplType = timeTableGenerationImplType;
    }
}
