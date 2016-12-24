package com.swiftpot.timetable;

import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 8:04 PM
 */

public class TestObjectChangesInList {

    @Test
    public void testObjectChanges() throws Exception {
        List<ProgrammeGroupDoc> programmeGroupDocList = new ArrayList<>();
        ProgrammeGroupDoc programmeGroupDoc = new ProgrammeGroupDoc();
        programmeGroupDoc.setProgrammeInitials("Bus-IT");
        programmeGroupDoc.setYearGroup(3);
        programmeGroupDocList.add(programmeGroupDoc);

        List<ProgrammeGroupDoc> finalProgrammeGroupDocs = new ArrayList<>(0);
        String programmeInitials = programmeGroupDoc.getProgrammeInitials();
        int yearGroup =  programmeGroupDoc.getYearGroup();



        //add entities first without setting programcode
        for (int i = 0; i < programmeGroupDocList.size(); i++) {
            finalProgrammeGroupDocs.add(programmeGroupDoc);
        }

        for (ProgrammeGroupDoc x : finalProgrammeGroupDocs){
            System.out.println("before setting new programCode="+x.toString());
        }


        for (ProgrammeGroupDoc x : finalProgrammeGroupDocs){

            int i = finalProgrammeGroupDocs.indexOf(x);
            switch (i) {
                case 0:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "A"));

                    System.out.println(programmeGroupDoc.getProgrammeCode());
                    break;
                case 1:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "B"));

                    System.out.println(programmeGroupDoc.getProgrammeCode());
                    break;
                case 2:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "C"));

                    System.out.println(programmeGroupDoc.getProgrammeCode());
                    break;
                case 3:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "D"));
                    break;
                case 4:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "E"));
                    break;
                case 5:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "F"));
                    break;
                case 6:
                    x.setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "G"));
                default:
                    throw new Exception("maximum of 7classes are allowed");
            }
        }

        for (ProgrammeGroupDoc x : finalProgrammeGroupDocs){
            System.out.println("after setting new programCode="+x.toString());
        }


    }




    private String createProgrammeCode(String programmeInitials, int yearGroup, String alphabetToAppend) {
        return programmeInitials + yearGroup + alphabetToAppend;
    }


}
