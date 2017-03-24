/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.services.ProgrammeGroupDocServices;
import com.swiftpot.timetable.util.PrettyJSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         24-Mar-17 @ 10:21 PM
 */
public class ProgrammeGroupDocServiceTests {

    @Test
    public void getYearGroupNumbersAndProgrammeGroupsThatExistInEachYearGroupNumberTest() {
        List<Integer> yearGroupNumbersForPositions = new ArrayList<>(Arrays.asList(1, 3, 2, 3, 3, 2));
        List<ProgrammeGroupDoc> programmeGroupDocs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ProgrammeGroupDoc programmeGroupDoc = new ProgrammeGroupDoc();
            programmeGroupDoc.setYearGroup(yearGroupNumbersForPositions.get(i));
            programmeGroupDoc.setProgrammeFullName("Dummy");

            programmeGroupDocs.add(programmeGroupDoc);
        }

        ProgrammeGroupDocServices programmeGroupDocServices = new ProgrammeGroupDocServices();
        Map<Integer, List<ProgrammeGroupDoc>> mapOfYearGroupAndProgrammeGroupDocs =
                programmeGroupDocServices.
                        getYearGroupNumbersAndProgrammeGroupsThatExistInEachYearGroupNumber(programmeGroupDocs);

        System.out.println("Final Results ==> \n\t" + PrettyJSON.toPrettyFormat(new Gson().toJson(mapOfYearGroupAndProgrammeGroupDocs)));
        mapOfYearGroupAndProgrammeGroupDocs.forEach((yearNumber, mapOfYearGroupAndProgrammeGroupDoc) -> {
            System.out.println("mapOfYearGroup = >" + PrettyJSON.toListPrettyFormat(mapOfYearGroupAndProgrammeGroupDoc) + "\n");
            assertTrue(!mapOfYearGroupAndProgrammeGroupDoc.isEmpty());
        });

        assertThat(mapOfYearGroupAndProgrammeGroupDocs.containsKey(1), equalTo(true));
        assertThat(mapOfYearGroupAndProgrammeGroupDocs.containsKey(2), equalTo(true));
        assertThat(mapOfYearGroupAndProgrammeGroupDocs.containsKey(3), equalTo(true));
        assertThat(mapOfYearGroupAndProgrammeGroupDocs.get(1).size(), equalTo(1));
        assertThat(mapOfYearGroupAndProgrammeGroupDocs.get(2).size(), equalTo(2));
        assertThat(mapOfYearGroupAndProgrammeGroupDocs.get(3).size(), equalTo(3));

    }
}
