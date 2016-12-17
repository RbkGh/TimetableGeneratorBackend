package com.swiftpot.timetable;

import com.swiftpot.timetable.services.SubjectsAssigner;
import com.swiftpot.timetable.util.BusinessLogicConfigurationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimetableTechnicalApplicationTests {

    @Autowired
    BusinessLogicConfigurationProperties businessLogicConfigurationProperties;
    @Autowired
    SubjectsAssigner subjectsAssigner;

    @Test
    public void contextLoads() {

    }


    /**
     * Test to ensure that property for period duration is set in properties file and only numbers allowed
     */
    @Test
    public void testReadingFromCustomFile() {
        String charSequence = "[0-9]+";
        String periodDuration = businessLogicConfigurationProperties.PERIOD_DURATION_IN_SECONDS;
        System.out.println("periodDuration = " + periodDuration);
        assertThat((periodDuration.matches(charSequence)) && (!periodDuration.isEmpty()),
                equalTo(true));

    }

    @Test
    public void testTotalSubjectAllocationGenerationVariables() {
        List<Integer> testSampleSpace = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        //start from i=1 as periods allocation does not have 1 involved
        for (int i = 1; i <= testSampleSpace.size(); i++) {
            List<Integer> totalSubjectAllocationList = subjectsAssigner.getTotalSubjectPeriodAllocationAsList(i);
            System.out.println("Total Subject Allocation for " + i + " periods = \n");
            for (Integer x : totalSubjectAllocationList) {
                System.out.println(x + "\n");
            }

            switch (i){
                case 1 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{1},totalSubjectAllocationList);
                    break;
                case 2 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{2},totalSubjectAllocationList);
                    break;
                case 3 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3},totalSubjectAllocationList);
                    break;
                case 4 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{2,2},totalSubjectAllocationList);
                    break;
                case 5 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,2},totalSubjectAllocationList);
                    break;
                case 6 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,3},totalSubjectAllocationList);
                    break;
                case 7 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,2,2},totalSubjectAllocationList);
                    break;
                case 8 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,3,2},totalSubjectAllocationList);
                    break;
                case 9 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,3,3},totalSubjectAllocationList);
                    break;
                case 10 :
                    assertTotalSubjectAllocationGenerationVariables(new Integer[]{3,3,2,2},totalSubjectAllocationList);
                    break;

                default:
                    System.out.println("Only 1-10 accepted");

            }

        }

    }

    private void assertTotalSubjectAllocationGenerationVariables(Integer[] val,List<Integer> totalSubjectAllocationList){
        assertThat(new ArrayList<>(Arrays.asList(val)), equalTo(totalSubjectAllocationList));
    }

}
