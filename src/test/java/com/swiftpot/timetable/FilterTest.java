package com.swiftpot.timetable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         26-Jan-17 @ 7:31 PM
 */
public class FilterTest {

    @Test
    public void testStreamsFilter() {
        List<String> tutorIdsInFakeDb = new ArrayList<>(Arrays.asList("i1", "i2", "i3", "i4", "i5", "i6", "i7"));
        List<String> tutorIdsIncoming = new ArrayList<>(Arrays.asList("i10", "i2", "i3", "i4", "i5", "i6", "i90"));

        tutorIdsIncoming.removeIf((tutorIdIncoming) -> !isTutorIdExistent(tutorIdIncoming, tutorIdsInFakeDb));

        System.out.println("Tutors List Incoming final List =" + String.join(",", tutorIdsIncoming));
        assertThat(tutorIdsIncoming.size(), equalTo(5));
    }

    public boolean isTutorIdExistent(String tutorId, List<String> tutorListToCheckAgainst) {
        List<Boolean> tutorIdBooleanList = new ArrayList<>();
        tutorListToCheckAgainst.forEach((currentTutor) -> {
            if (tutorId == currentTutor) {
                tutorIdBooleanList.add(true);
            } else {
                tutorIdBooleanList.add(false);
            }
        });
        System.out.println("IsTutorId existent? =" + tutorIdBooleanList.contains(true));
        if (tutorIdBooleanList.contains(true)) {
            return true; //it was found at least once,hence return true;
        } else {
            return false;//no true found,hence it does not exist in list
        }

    }
}
