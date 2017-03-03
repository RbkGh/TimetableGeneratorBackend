package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swiftpot.timetable.model.ProgrammeDay;
import com.swiftpot.timetable.repository.TutorPersonalTimeTableDocRepository;
import com.swiftpot.timetable.repository.db.model.TutorPersonalTimeTableDoc;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import com.swiftpot.timetable.services.TutorPersonalTimeTableDocServices;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import com.swiftpot.timetable.util.PrettyJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Type;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         03-Mar-17 @ 8:45 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TutorPersonalTimeTableDocServicesTest {

    @Autowired
    private TutorPersonalTimeTableDocServices tutorPersonalTimeTableDocServices;
    @MockBean
    private TutorPersonalTimeTableDocRepository tutorPersonalTimeTableDocRepository;
    @Autowired
    private TimeTableGeneratorService timeTableGeneratorService;
    @Autowired
    private BusinessLogicConfigurationProperties businessLogicConfigurationProperties;
    String tutorUniqueIdInDb;
    String subjectUniqueId;
    private int periodNumberToStartSettingSubject;
    private int periodNumberToStopSettingSubject;
    private static final Logger logger = LogManager.getLogger();

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<ProgrammeDay> programmeDaysListDefault = timeTableGeneratorService.generateAllProgrammeDaysFirstTime();
        tutorUniqueIdInDb = "GHDF";
        subjectUniqueId = "SUBJECT";
        periodNumberToStartSettingSubject = 3;
        periodNumberToStopSettingSubject = 5;
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = new TutorPersonalTimeTableDoc();
        tutorPersonalTimeTableDoc.setTutorUniqueIdInDb(tutorUniqueIdInDb);
        tutorPersonalTimeTableDoc.setProgrammeDaysList(programmeDaysListDefault);

        Mockito.when(tutorPersonalTimeTableDocRepository.findByTutorUniqueIdInDb(tutorUniqueIdInDb)).thenReturn(tutorPersonalTimeTableDoc);

    }

    @Test
    public void getTutorPersonalTimeTableWithIncomingPeriodsSet() {
        String programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_3;

        Type typeOfSrc = new TypeToken<TutorPersonalTimeTableDoc>() {
        }.getType();
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = tutorPersonalTimeTableDocServices.
                getTutorPersonalTimeTableWithIncomingPeriodsSet(tutorUniqueIdInDb, subjectUniqueId, programmeDayName, periodNumberToStartSettingSubject, periodNumberToStopSettingSubject);
        logger.info("TutorPersonalTimeTableDoc generated with set periods =\n{}",
                PrettyJSON.toPrettyFormat(new Gson().toJson(tutorPersonalTimeTableDoc, typeOfSrc)));
        assertThat(tutorPersonalTimeTableDoc.getProgrammeDaysList().get(2).getPeriodList().get(2).getIsAllocated(), equalTo(true));
    }

    @Test(expected = NullPointerException.class)
    public void getTutorPersonalTimeTableWithIncomingPeriodsSetExpectError() {
        String programmeDayName = businessLogicConfigurationProperties.TIMETABLE_DAY_6;

        Type typeOfSrc = new TypeToken<TutorPersonalTimeTableDoc>() {
        }.getType();
        TutorPersonalTimeTableDoc tutorPersonalTimeTableDoc = tutorPersonalTimeTableDocServices.
                getTutorPersonalTimeTableWithIncomingPeriodsSet(tutorUniqueIdInDb, subjectUniqueId, programmeDayName, periodNumberToStartSettingSubject, periodNumberToStopSettingSubject);
        logger.info("TutorPersonalTimeTableDoc generated with set periods =\n{}",
                PrettyJSON.toPrettyFormat(new Gson().toJson(tutorPersonalTimeTableDoc, typeOfSrc)));
        assertThat(tutorPersonalTimeTableDoc.getProgrammeDaysList().get(2).getPeriodList().get(2).getIsAllocated(), equalTo(true));
    }
}
