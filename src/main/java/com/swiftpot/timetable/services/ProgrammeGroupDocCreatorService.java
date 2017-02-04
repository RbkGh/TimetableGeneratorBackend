package com.swiftpot.timetable.services;

import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.TimeTableSuperDocRepository;
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
public class ProgrammeGroupDocCreatorService {

    @Autowired
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @Autowired
    TimeTableSuperDocRepository timeTableSuperDocRepository;

    /**
     * TODO URGENT!!!!sort incoming list into various yeargroups list before generating the created programmeCode for each yearGroup
     *
     * @param programmeGroupDocs
     * @return
     * @throws Exception
     */
    public List<ProgrammeGroupDoc> createProgramGroupDocWithConstraintsCateredForAndSave(List<ProgrammeGroupDoc> programmeGroupDocs) throws Exception {

        List<ProgrammeGroupDoc> finalProgrammeGroupDocs = new ArrayList<>(0);
        int numberOfProgrammeGroupDocs = programmeGroupDocs.size();

        for (int i = 0; i < numberOfProgrammeGroupDocs; i++) {

            String programmeInitials = programmeGroupDocs.get(i).getProgrammeInitials();
            int yearGroup = programmeGroupDocs.get(i).getYearGroup();
            System.out.println("Current number=" + i);
            switch (i) {
                case 0:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "A"));
                    break;
                case 1:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "B"));
                    break;
                case 2:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "C"));
                    break;
                case 3:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "D"));
                    break;
                case 4:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "E"));
                    break;
                case 5:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "F"));
                    break;
                case 6:
                    programmeGroupDocs.get(i).setProgrammeCode(createProgrammeCode(programmeInitials, yearGroup, "G"));
                    break;
                default:
                    throw new Exception("maximum of 7classes are allowed");
            }
        }

        List<ProgrammeGroupDoc> r3 = programmeGroupDocRepository.save(programmeGroupDocs);
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
