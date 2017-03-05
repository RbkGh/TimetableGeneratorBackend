package com.swiftpot.timetable.services;

import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         05-Mar-17 @ 5:39 PM
 */
@Service
public class TimeTableSuperDocServices {

    /**
     * get or retrieve the {@link ProgrammeGroup} object from the passed in {@link TimeTableSuperDoc} object
     *
     * @param timeTableSuperDoc the {@link TimeTableSuperDoc} to be used to search for the {@link ProgrammeGroup} on it.
     * @param programmeCode     the {@link ProgrammeGroup#programmeCode} of the {@link ProgrammeGroup} object to retrieve.
     * @return {@link ProgrammeGroup} object extracted from the {@link com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc} object passed in.
     */
    public ProgrammeGroup getProgrammeGroupObjectFromTimeTableSuperDocObject(TimeTableSuperDoc timeTableSuperDoc,
                                                                             String programmeCode) {
        ProgrammeGroup programmeGroupToReturn = null;
        List<YearGroup> yearGroupList = timeTableSuperDoc.getYearGroupsList();
        for (YearGroup yearGroup : yearGroupList) {
            if (!Objects.isNull(programmeGroupToReturn)) {
                break;
            } else {
                List<ProgrammeGroup> programmeGroupList = yearGroup.getProgrammeGroupList();
                for (ProgrammeGroup programmeGroup : programmeGroupList) {
                    if (Objects.equals(programmeCode, programmeGroup.getProgrammeCode())) {
                        programmeGroupToReturn = programmeGroup;
                        break;
                    }
                }
            }
        }
        return programmeGroupToReturn;
    }
}
