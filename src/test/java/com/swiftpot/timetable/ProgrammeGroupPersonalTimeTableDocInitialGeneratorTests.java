package com.swiftpot.timetable;

import com.swiftpot.timetable.base.ProgrammeGroupPersonalTimeTableDocInitialGenerator;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupPersonalTimeTableDoc;
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
 *         04-Mar-17 @ 11:09 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProgrammeGroupPersonalTimeTableDocInitialGeneratorTests {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    ProgrammeGroupPersonalTimeTableDocInitialGenerator programmeGroupPersonalTimeTableDocInitialGenerator;

    List<ProgrammeGroupDoc> programmeGroupDocsList;

    @Before
    public void setupData() {
        programmeGroupDocsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProgrammeGroupDoc programmeGroupDoc = new ProgrammeGroupDoc();
            programmeGroupDoc.setProgrammeCode("ProgrammeCode" + i);
            programmeGroupDocsList.add(programmeGroupDoc);
        }
    }

    @Test(expected = AssertionError.class)
    public void generateAllProgrammeGroupPersonalTimetableDocsForAllProgrammeGroupDocsPassedInExpectAssertionError() throws Exception {
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocsGenerated =
                programmeGroupPersonalTimeTableDocInitialGenerator.
                        generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsPassedIn(programmeGroupDocsList);
        logger.info("\n\n*******programmeGroupPersonalTimeTableDocsGenerated*******\n\n =>>>\t{}",
                PrettyJSON.toListPrettyFormat(programmeGroupPersonalTimeTableDocsGenerated));
        assertThat(programmeGroupDocsList.size(), equalTo(programmeGroupPersonalTimeTableDocsGenerated.size() - 1));
    }

    @Test
    public void generateAllProgrammeGroupPersonalTimetableDocsForAllProgrammeGroupDocsPassedIn() throws Exception {
        List<ProgrammeGroupPersonalTimeTableDoc> programmeGroupPersonalTimeTableDocsGenerated =
                programmeGroupPersonalTimeTableDocInitialGenerator.
                        generateAllProgrammeGroupPersonalTimetableDocForAllProgrammeGroupDocsPassedIn(programmeGroupDocsList);
        logger.info("\n\n*******programmeGroupPersonalTimeTableDocsGenerated*******\n\n =>>>\t{}",
                PrettyJSON.toListPrettyFormat(programmeGroupPersonalTimeTableDocsGenerated));
        assertThat(programmeGroupDocsList.size(), equalTo(programmeGroupPersonalTimeTableDocsGenerated.size()));
    }
}
