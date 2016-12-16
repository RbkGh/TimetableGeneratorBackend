package com.swiftpot.timetable;

import com.swiftpot.timetable.util.BusinessLogicConfigValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimetableTechnicalApplicationTests {

    @Autowired
    BusinessLogicConfigValues businessLogicConfigValues;

    @Test
    public void contextLoads() {

    }


    /**
     * Test to ensure that property for period duration is set and only numbers
     */
    @Test
    public void testReadingFromCustomFile() {
        String charSequence = "[0-9]+";
        String periodDuration = businessLogicConfigValues.PERIOD_DURATION_IN_SECONDS;
        System.out.println("periodDuration = " + periodDuration);
        assertThat((periodDuration.matches(charSequence)) && (!periodDuration.isEmpty()),
                equalTo(true));

    }


}
