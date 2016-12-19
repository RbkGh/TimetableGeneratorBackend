package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         18-Dec-16 @ 1:30 PM
 */
@Service
public class ProgrammeGroupDocCreator {

    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;

    public List<ProgrammeGroupDoc> createProgramGroupDocWithConstraintsCateredForAndSave(ProgrammeGroupDoc programmeGroupDoc) throws Exception {

        List<ProgrammeGroupDoc> finalProgrammeGroupDocs = new ArrayList<>(0);

        int numberOfProgrammeGroupsToCreate = programmeGroupDoc.getNumberOfClasses();
        System.out.println("number of classes =" + numberOfProgrammeGroupsToCreate);
        String programmeInitials = programmeGroupDoc.getProgrammeInitials();
        int yearGroup = programmeGroupDoc.getYearGroup();

        //add entities first without setting programcode
        for (int i = 0; i < numberOfProgrammeGroupsToCreate; i++) {
            finalProgrammeGroupDocs.add(programmeGroupDoc);
        }


        List<String> generatedProgramCodes = new ArrayList<>();
        for (int i = 0; i < finalProgrammeGroupDocs.size(); i++) {

            System.out.println("Current number=" + i);
            switch (i) {
                case 0:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "A"));
                    System.out.println("generatedProgramCode for position"+0+" ="+generatedProgramCodes.get(0));
                    break;
                case 1:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "B"));
                    System.out.println("generatedProgramCode for position" +1+" ="+generatedProgramCodes.get(1));
                    break;
                case 2:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "C"));
                    System.out.println("generatedProgramCode for position" +2+" ="+generatedProgramCodes.get(2));
                    break;
                case 3:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "D"));
                    System.out.println("generatedProgramCode for position" +3+" ="+generatedProgramCodes.get(3));
                    break;
                case 4:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "E"));
                    System.out.println("generatedProgramCode for position" +4+" ="+generatedProgramCodes.get(4));
                    break;
                case 5:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "F"));
                    System.out.println("generatedProgramCode for position" +5+" ="+generatedProgramCodes.get(5));
                    break;
                case 6:
                    generatedProgramCodes.add(createProgrammeCode(programmeInitials, yearGroup, "G"));
                    System.out.println("generatedProgramCode for position" +6+" ="+generatedProgramCodes.get(6));
                    break;
                default:
                    throw new Exception("maximum of 7classes are allowed");
            }
        }

        for (int i=0; i< generatedProgramCodes.size();i++) {
            finalProgrammeGroupDocs.get(i).setProgrammeCode(generatedProgramCodes.get(i));
            System.out.println("\nEntities before saved in db =" + finalProgrammeGroupDocs.get(i).toString());
        }


        List<ProgrammeGroupDoc> r3 = programmeGroupDocRepository.save(finalProgrammeGroupDocs);
        for (ProgrammeGroupDoc x2 : r3) {
            System.out.println("\n\nEntities after saved in db =" + x2.toString());
        }

        return finalProgrammeGroupDocs;
    }


    private String createProgrammeCode(String programmeInitials, int yearGroup, String alphabetToAppend) {
        String generatedProgrammeCode = programmeInitials + String.valueOf(yearGroup) + alphabetToAppend;
        return generatedProgrammeCode;
    }


}
