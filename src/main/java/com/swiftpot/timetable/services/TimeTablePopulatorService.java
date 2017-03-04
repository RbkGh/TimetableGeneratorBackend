package com.swiftpot.timetable.services;

import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.model.ProgrammeGroup;
import com.swiftpot.timetable.model.YearGroup;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.util.YearGroupNumberAndNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 10:49 AM
 */
@Service
public class TimeTablePopulatorService {

    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    ProgrammeDaysGenerator programmeDaysGenerator;
    @Autowired
    TimeTableDefaultPeriodsAllocatorFactory timeTableDefaultPeriodsAllocatorFactory;

    public TimeTableSuperDoc partOneSetYearGroups() throws Exception {
        List<ProgrammeGroupDoc> allProgrammeGroupDocsListInDb = getAllProgrammeGroupDocsListInDb();

        TimeTableSuperDoc timeTableSuperDoc = new TimeTableSuperDoc();
        //set yearGroupList
        List<YearGroup> yearGroupsList = new ArrayList<>();

        int numberOfProgrammeGroupDocs = allProgrammeGroupDocsListInDb.size();
        //go through ProgrammeGroupDoc list and set new YearGroup for each instance and add to yearGroupsList
        for (int currentNo = 0; currentNo < numberOfProgrammeGroupDocs; currentNo++) {
            int currentYearGroup = allProgrammeGroupDocsListInDb.get(currentNo).getYearGroup();
            String currentYearName = YearGroupNumberAndNames.getYearGroupName(currentYearGroup);

            YearGroup yearGroup = new YearGroup();
            yearGroup.setYearName(currentYearName);
            yearGroup.setYearNumber(currentYearGroup);

            //now to set programmeGroupList for YearGroup,fetch all ProgrammeGroupDoc from db with current yeargroup Number
            List<ProgrammeGroup> programmeGroupList = new ArrayList<>();
            ProgrammeGroup programmeGroup = new ProgrammeGroup();
            List<ProgrammeGroupDoc> programmeGroupDocForCurrentYearGroupList = programmeGroupDocRepository.findByYearGroup(currentYearGroup);
            //set each ProgrammeGroup's parameters from ProgramGroupDoc and add to ProgrammeGroup list
            for (ProgrammeGroupDoc x : programmeGroupDocForCurrentYearGroupList) {
                programmeGroup.setProgrammeCode(x.getProgrammeCode());
                //set wether the programmeCode Requires a technical Workshop or not
                programmeGroup.setIsProgrammeRequiringPracticalsClassroom(x.getIsTechnicalWorkshopOrLabRequired());
                //Now we need to set programmeDaysList ,Initialize it typically from Monday To Friday with the days and the periodsList
                programmeGroup.setProgrammeDaysList(programmeDaysGenerator.generateAllProgrammeDays(x.getProgrammeCode()));
                //finally in this loop,finish off by adding the programmeGroup to the programmeGroupList
                programmeGroupList.add(programmeGroup);
            }
            //now we can set programmeGroupList for YearGroup,as we have finished iterating through
            yearGroup.setProgrammeGroupList(programmeGroupList);
            //now we can add current YearGroup Object to the yearGroupList
            yearGroupsList.add(yearGroup);
        }
        //now we can set yearGroupList to the timeTableSuperDoc and return it finally
        timeTableSuperDoc.setYearGroupsList(yearGroupsList);

        return timeTableSuperDoc;
    }

    public TimeTableSuperDoc partTwoAllocateDefaultPeriods(String timeTableDefaultPeriodsAllocatorType, TimeTableSuperDoc timeTableSuperDocWithInitialDefaultDataSet) throws Exception {
        return timeTableDefaultPeriodsAllocatorFactory.getTimeTableDefaultPeriodsAllocator(timeTableDefaultPeriodsAllocatorType).
                allocateDefaultPeriodsOnTimeTable(timeTableSuperDocWithInitialDefaultDataSet);
    }

    private List<ProgrammeGroupDoc> getAllProgrammeGroupDocsListInDb() {
        return programmeGroupDocRepository.findAll();
    }

}
