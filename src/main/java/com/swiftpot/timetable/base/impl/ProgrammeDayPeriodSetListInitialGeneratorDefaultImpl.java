/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable.base.impl;

import com.swiftpot.timetable.base.ProgrammeDayPeriodSetListInitialGenerator;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.ProgrammeGroupDayPeriodSetsDocRepository;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDayPeriodSetsDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.services.ProgrammeDayPeriodSetService;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import com.swiftpot.timetable.services.servicemodels.PeriodSetForProgrammeDay;
import com.swiftpot.timetable.util.RandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link ProgrammeDayPeriodSetListInitialGenerator}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 1:40 PM
 * @see ProgrammeDayPeriodSetListInitialGenerator
 */
@Component
public class ProgrammeDayPeriodSetListInitialGeneratorDefaultImpl implements ProgrammeDayPeriodSetListInitialGenerator {

    @Autowired
    private ProgrammeGroupDayPeriodSetsDocRepository programmeGroupDayPeriodSetsDocRepository;
    @Autowired
    private ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    ProgrammeDayPeriodSetService programmeDayPeriodSetService;
    @Autowired
    private TimeTableGeneratorService timeTableGeneratorService;

    @Override
    public void generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDocAndSaveInDb() throws Exception {
        List<ProgrammeGroupDoc> listOAllProgrammeGroupDocsInDb = programmeGroupDocRepository.findAll();
        List<ProgrammeGroupDayPeriodSetsDoc> finalProgrammeGroupDayPeriodSetsDocs =
                this.generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDoc(listOAllProgrammeGroupDocsInDb);

        programmeGroupDayPeriodSetsDocRepository.save(finalProgrammeGroupDayPeriodSetsDocs);
    }

    @Override
    public List<ProgrammeGroupDayPeriodSetsDoc> generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDoc(List<ProgrammeGroupDoc> programmeGroupDocsList) throws Exception {
        List<ProgrammeGroupDayPeriodSetsDoc> finalProgrammeGroupDayPeriodSetsDocs = new ArrayList<>();
        for (ProgrammeGroupDoc programmeGroupDoc : programmeGroupDocsList) {
            ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDoc =
                    this.generateProgrammeGroupDayPeriodSetsDocFromProgrammeGroupDoc(programmeGroupDoc);
            finalProgrammeGroupDayPeriodSetsDocs.add(programmeGroupDayPeriodSetsDoc);
        }
        return finalProgrammeGroupDayPeriodSetsDocs;
    }

    private ProgrammeGroupDayPeriodSetsDoc generateProgrammeGroupDayPeriodSetsDocFromProgrammeGroupDoc(ProgrammeGroupDoc programmeGroupDoc) throws Exception {

        List<ProgrammeDay> programmeDaysListForProgrammeCode = this.generateProgrammeDaysForProgrammeGroup();

        Map<String, List<PeriodSetForProgrammeDay>> programmeGroupDayPeriodSetsDocMap = new HashMap<>();

        for (ProgrammeDay programmeDay : programmeDaysListForProgrammeCode) {
            String programmeDayName = programmeDay.getDayName();

            List<PeriodSetForProgrammeDay> periodSetForProgrammeDayList =
                    this.generatePeriodSetForProgrammeDayListForProgrammeDay();

            programmeGroupDayPeriodSetsDocMap.put(programmeDayName, periodSetForProgrammeDayList);
        }

        ProgrammeGroupDayPeriodSetsDoc programmeGroupDayPeriodSetsDoc = new ProgrammeGroupDayPeriodSetsDoc();
        programmeGroupDayPeriodSetsDoc.setProgrammeCode(programmeGroupDoc.getProgrammeCode()); //set programmeCode
        programmeGroupDayPeriodSetsDoc.setMapOfProgDayNameAndTheListOfPeriodSets(programmeGroupDayPeriodSetsDocMap);

        return programmeGroupDayPeriodSetsDoc;
    }

    private List<PeriodSetForProgrammeDay> generatePeriodSetForProgrammeDayListForProgrammeDay() {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator() {

        };
        //generate random number to fetch a random number combination of programmeDay Period Allocation.
        List<PeriodSetForProgrammeDay> finalPeriodSetForProgrammeDays =
                programmeDayPeriodSetService.getPeriodAllocationForDayAsProgDayPeriodSetList(randomNumberGenerator.generateRandomNumber(1, 2));
        return finalPeriodSetForProgrammeDays;
    }

    protected List<ProgrammeDay> generateProgrammeDaysForProgrammeGroup() throws Exception {
        return timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
    }
}
