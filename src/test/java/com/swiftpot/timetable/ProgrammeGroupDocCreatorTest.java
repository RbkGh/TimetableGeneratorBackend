package com.swiftpot.timetable;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.services.ProgrammeGroupDocCreatorService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         04-Feb-17 @ 8:04 PM
 */
public class ProgrammeGroupDocCreatorTest {

    @Test(expected = AssertionError.class)
    public void testProgrammeGroupDocCreationExpectAssertionError() throws Exception {

        List<ProgrammeGroupDoc> programmeGroupDocsFinal = generateProgrammeGroupDocs(3);
        ProgrammeGroupDocCreatorService programmeGroupDocCreatorService = new ProgrammeGroupDocCreatorService();//don't wanna use autowiring
        //now pass the 6 generated programmeGroupDocs to the creator service
        List<ProgrammeGroupDoc> programmeGroupDocsWithgeneratedProgrammeCodes = programmeGroupDocCreatorService.createAllProgrammeCodesByFilteringYearGroupsAndProcessing(programmeGroupDocsFinal);

        for (ProgrammeGroupDoc x2 : programmeGroupDocsWithgeneratedProgrammeCodes) {
            System.out.println("\n\nEntities after setting programmeCode =" + x2.toString());
        }
        assertThat(programmeGroupDocsWithgeneratedProgrammeCodes.get(2).getProgrammeCode(), equalTo("ECE2A"));
    }

    @Test(expected = Exception.class)
    public void testProgrammeGroupDocCreationWhenClassesIsAboveMaximumExpected() throws Exception {

        List<ProgrammeGroupDoc> programmeGroupDocsFinal = generateProgrammeGroupDocs(8);//max is 7 so error should throw
        ProgrammeGroupDocCreatorService programmeGroupDocCreatorService = new ProgrammeGroupDocCreatorService();//don't wanna use autowiring
        //now pass the 6 generated programmeGroupDocs to the creator service
        List<ProgrammeGroupDoc> programmeGroupDocsWithgeneratedProgrammeCodes = programmeGroupDocCreatorService.createAllProgrammeCodesByFilteringYearGroupsAndProcessing(programmeGroupDocsFinal);

        for (ProgrammeGroupDoc x2 : programmeGroupDocsWithgeneratedProgrammeCodes) {
            System.out.println("\n\nEntities after setting programmeCode =" + x2.toString());
        }
        assertThat(programmeGroupDocsWithgeneratedProgrammeCodes.get(2).getProgrammeCode(), equalTo("ECE2A"));
    }

    @Test()
    public void testProgrammeGroupDocCreationWithTwoClassesEach() throws Exception {

        List<ProgrammeGroupDoc> programmeGroupDocsFinal = generateProgrammeGroupDocs(2);//max is 7 so error should throw
        ProgrammeGroupDocCreatorService programmeGroupDocCreatorService = new ProgrammeGroupDocCreatorService();//don't wanna use autowiring
        //now pass the 6 generated programmeGroupDocs to the creator service
        List<ProgrammeGroupDoc> programmeGroupDocsWithgeneratedProgrammeCodes = programmeGroupDocCreatorService.createAllProgrammeCodesByFilteringYearGroupsAndProcessing(programmeGroupDocsFinal);

        for (ProgrammeGroupDoc x2 : programmeGroupDocsWithgeneratedProgrammeCodes) {
            System.out.println("\n\nEntities after setting programmeCode =" + x2.toString());
        }
        assertThat(programmeGroupDocsWithgeneratedProgrammeCodes.get(2).getProgrammeCode(), equalTo("ECE2A"));
    }


    List<ProgrammeGroupDoc> generateProgrammeGroupDocs(int numberOfClassesForEachYearGroup) {
        List<ProgrammeGroupDoc> programmeGroupDocsFinal = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            List<ProgrammeGroupDoc> programmeGroupDocsForCurrentYearGroup = new ArrayList<>();
            for (int currentYearGroup = 1; currentYearGroup <= numberOfClassesForEachYearGroup; currentYearGroup++) {
                ProgrammeGroupDoc programmeGroupDoc = new ProgrammeGroupDoc();
                programmeGroupDoc.setYearGroupList(Arrays.asList(1, 2, 3));
                programmeGroupDoc.setProgrammeInitials("ECE");
                programmeGroupDoc.setYearGroup(i);
                programmeGroupDoc.setProgrammeFullName("Electronics Communication Engineering");
                programmeGroupDocsForCurrentYearGroup.add(programmeGroupDoc);
            }
            programmeGroupDocsFinal.addAll(programmeGroupDocsForCurrentYearGroup);
        }
        return programmeGroupDocsFinal;
    }
}
