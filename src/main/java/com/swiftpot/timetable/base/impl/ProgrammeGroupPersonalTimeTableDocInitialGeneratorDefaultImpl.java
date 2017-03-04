package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.ProgrammeGroupPersonalTimeTableDocInitialGenerator;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.ProgrammeGroupPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Mar-17 @ 10:40 PM
 */
@Component
public class ProgrammeGroupPersonalTimeTableDocInitialGeneratorDefaultImpl implements ProgrammeGroupPersonalTimeTableDocInitialGenerator {

    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    ProgrammeGroupPersonalTimeTableDocRepository programmeGroupPersonalTimeTableDocRepository;
    @Autowired
    TimeTableGeneratorService timeTableGeneratorService;

    @Override
    public synchronized void generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsInDbAndSaveInDb() throws Exception {
        List<ProgrammeGroupDoc> allProgrammeGroupDocsInDb = programmeGroupDocRepository.findAll();
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocs =
                this.generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsPassedIn(allProgrammeGroupDocsInDb);

        programmeGroupPersonalTimeTableDocRepository.save(programmeGroupPersonalTimeTableDocs);
    }

    @Override
    public List<ProgrammeGroupPersonalTimeTableDoc> generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsPassedIn(List<ProgrammeGroupDoc> programmeGroupDocs) throws Exception {
        List<ProgrammeDay> programmeDaysList = timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocsList = new ArrayList<>();
        programmeGroupDocs.forEach((ProgrammeGroupDoc programmeGroupDoc) ->
                programmeGroupPersonalTimeTableDocsList.add
                        (this.createProgrammeGroupPersonalTimeTableDoc(programmeGroupDoc, programmeDaysList)));
        return programmeGroupPersonalTimeTableDocsList;
    }

    protected ProgrammeGroupPersonalTimeTableDoc createProgrammeGroupPersonalTimeTableDoc(ProgrammeGroupDoc programmeGroupDoc, List<ProgrammeDay> programmeDaysToSet) {
        String programmeCode = programmeGroupDoc.getProgrammeCode();
        ProgrammeGroupPersonalTimeTableDoc programmeGroupPersonalTimeTableDoc = new ProgrammeGroupPersonalTimeTableDoc();
        programmeGroupPersonalTimeTableDoc.setProgrammeCode(programmeCode);
        programmeGroupPersonalTimeTableDoc.setProgrammeDaysList(programmeDaysToSet);
        return programmeGroupPersonalTimeTableDoc;
    }
}
