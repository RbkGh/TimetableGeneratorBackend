package com.swiftpot.timetable;

import com.swiftpot.timetable.base.TutorPersonalTimeTableInitialGenerator;
import com.swiftpot.timetable.base.impl.TutorPersonalTimeTableInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.repository.db.model.TutorDoc;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import com.swiftpot.timetable.util.PrettyJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 *         02-Mar-17 @ 8:24 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TutorPersonalTimeTableInitialGeneratorTests {

    private static final Logger logger = LogManager.getLogger();
    @Autowired
    TutorPersonalTimeTableInitialGeneratorDefaultImpl tutorPersonalTimeTableInitialGeneratorDefault;
    @Autowired
    TutorPersonalTimeTableInitialGenerator tutorPersonalTimeTableInitialGenerator;

    @Test
    public void testGeneratePersonalTimeTableForAllTutorsSupplied() throws Exception {
        List<TutorDoc> tutorDocs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TutorDoc tutorDoc = new TutorDoc();
            tutorDoc.setId("ID" + i);
            tutorDocs.add(tutorDoc);
        }
        tutorPersonalTimeTableInitialGenerator = tutorPersonalTimeTableInitialGeneratorDefault;
        List<TutorPersonalTimeTableDoc> generatedTutorPersonalTimeTableDocs = tutorPersonalTimeTableInitialGenerator.generatePersonalTimeTableForAllTutorsInDb(tutorDocs);
        logger.info("Generated TutorPersonalTimeTableDocs = {}", PrettyJSON.toListPrettyFormat(generatedTutorPersonalTimeTableDocs));
        assertThat(generatedTutorPersonalTimeTableDocs.size(), equalTo(3));
    }

    @Test
    public void testGeneratePersonalTimeTableForAllTutorsSuppliedEnsureProgrammeDaysEqualsFive() throws Exception {
        List<TutorDoc> tutorDocs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TutorDoc tutorDoc = new TutorDoc();
            tutorDoc.setId("ID" + i);
            tutorDocs.add(tutorDoc);
        }
        tutorPersonalTimeTableInitialGenerator = tutorPersonalTimeTableInitialGeneratorDefault;
        List<TutorPersonalTimeTableDoc> generatedTutorPersonalTimeTableDocs = tutorPersonalTimeTableInitialGenerator.generatePersonalTimeTableForAllTutorsInDb(tutorDocs);
        logger.info("Generated TutorPersonalTimeTableDocs = {}", PrettyJSON.toListPrettyFormat(generatedTutorPersonalTimeTableDocs));
        assertThat(generatedTutorPersonalTimeTableDocs.get(1).getProgrammeDaysList().size(), equalTo(5));
    }

}
