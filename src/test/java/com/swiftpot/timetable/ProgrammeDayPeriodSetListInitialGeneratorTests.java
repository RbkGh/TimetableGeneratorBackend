/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.swiftpot.timetable.base.ProgrammeDayPeriodSetListInitialGenerator;
import com.swiftpot.timetable.base.impl.ProgrammeDayPeriodSetListInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDayPeriodSetsDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import com.swiftpot.timetable.util.PrettyJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         10-Mar-17 @ 5:15 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgrammeDayPeriodSetListInitialGeneratorTests {

    private static final Logger logger = LogManager.getLogger();
    @Autowired
    ProgrammeDayPeriodSetListInitialGenerator programmeDayPeriodSetListInitialGenerator;
    @Autowired
    ProgrammeDayPeriodSetListInitialGeneratorDefaultImpl programmeDayPeriodSetListInitialGeneratorDefaultImpl;

    private List<ProgrammeGroupDoc> programmeGroupDocList;

    @Autowired
    private BusinessLogicConfigurationProperties businessLogicConfigurationProperties;

    @Before
    public void setUpStuff() {
        programmeGroupDocList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ProgrammeGroupDoc programmeGroupDoc = new ProgrammeGroupDoc();
            programmeGroupDoc.setProgrammeCode("PROGRAMMECODE" + i);

            programmeGroupDocList.add(programmeGroupDoc);
        }
    }

    @Test
    public void generateProgrammeDayPeriodSetForEachDayInProgrammeGroupList() throws Exception {
        programmeDayPeriodSetListInitialGenerator = programmeDayPeriodSetListInitialGeneratorDefaultImpl;
        List<ProgrammeGroupDayPeriodSetsDoc> programmeGroupDayPeriodSetsDocs =
                programmeDayPeriodSetListInitialGenerator.generateProgrammeGroupDayPeriodSetsDocForEachProgrammeGroupDoc(programmeGroupDocList);

        String dayNameToFind = businessLogicConfigurationProperties.TIMETABLE_DAY_1;

        logger.info("ProgrammeGroupDayPeriodSetsDocs List generated ===>>\n\n\t\t,{}", PrettyJSON.toListPrettyFormat(programmeGroupDayPeriodSetsDocs));
        programmeGroupDayPeriodSetsDocs.forEach(programmeGroupDayPeriodSetsDoc -> assertThat(programmeGroupDayPeriodSetsDoc.
                        getMapOfProgDayNameAndTheListOfPeriodSets().containsKey(dayNameToFind),
                equalTo(true)));
    }
}
