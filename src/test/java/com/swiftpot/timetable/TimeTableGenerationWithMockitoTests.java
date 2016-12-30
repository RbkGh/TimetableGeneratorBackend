package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.swiftpot.timetable.factory.TimeTableDefaultPeriodsAllocatorFactory;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.db.model.ProgrammeGroupDoc;
import com.swiftpot.timetable.repository.db.model.TimeTableSuperDoc;
import com.swiftpot.timetable.services.ProgrammeDaysGenerator;
import com.swiftpot.timetable.services.TimeTableGeneratorService;
import com.swiftpot.timetable.services.TimeTablePopulatorService;
import com.swiftpot.timetable.util.PrettyJSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         29-Dec-16 @ 7:05 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TimeTableGenerationWithMockitoTests {

    @Autowired
    TimeTablePopulatorService timeTablePopulatorService;

    @MockBean
    ProgrammeGroupDocRepository programmeGroupDocRepository;


    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        //timeTablePopulatorService = Mockito.mock(TimeTablePopulatorService.class);


    }

    @Test
    public void testMockCreation(){
        assertNotNull(programmeGroupDocRepository);
    }
    @Test
    public void partOneSetYearGroups() throws Exception {

        ProgrammeGroupDoc programmeGroupDoc1 = new ProgrammeGroupDoc();
        programmeGroupDoc1.setProgrammeFullName("Building Construction Technology");
        programmeGroupDoc1.setProgrammeInitials("BCT");
        programmeGroupDoc1.setYearGroup(1);
        programmeGroupDoc1.setProgrammeCode("BCT-1A");
        programmeGroupDoc1.setDefaultClassRoomId("class1kj");
        programmeGroupDoc1.setYearGroupList(new ArrayList<>(Arrays.asList("1", "2", "3")));
        programmeGroupDoc1.setProgrammeSubjectsCodeList(new ArrayList<>(Arrays.asList("ENG", "MATH", "INTSCIENCE", "SOCSTUDIES", "FDT", "RET", "RRT", "ECTR", "ICT", "PE")));
        programmeGroupDoc1.setTechnicalWorkshopOrLabRequired(true);

        ProgrammeGroupDoc programmeGroupDoc2 = new ProgrammeGroupDoc();
        programmeGroupDoc2.setProgrammeFullName("Business Studies in Accounting");
        programmeGroupDoc2.setProgrammeInitials("BUS-ACC");
        programmeGroupDoc2.setYearGroup(2);
        programmeGroupDoc2.setProgrammeCode("BUS-ACC2A");
        programmeGroupDoc2.setDefaultClassRoomId("class1kj");
        programmeGroupDoc2.setYearGroupList(new ArrayList<>(Arrays.asList("1", "2", "3")));
        programmeGroupDoc2.setProgrammeSubjectsCodeList(new ArrayList<>(Arrays.asList("ENG", "MATH", "INTSCIENCE", "SOCSTUDIES", "ECONS", "CETTR", "RRYT", "ECPOTR", "ICT", "PE")));
        programmeGroupDoc2.setTechnicalWorkshopOrLabRequired(false);

        ProgrammeGroupDoc programmeGroupDoc3 = new ProgrammeGroupDoc();
        programmeGroupDoc3.setProgrammeFullName("Computer Technology - Hardware");
        programmeGroupDoc3.setProgrammeInitials("CTECH-IT-HARDWARE");
        programmeGroupDoc3.setYearGroup(3);
        programmeGroupDoc3.setProgrammeCode("CTECH-IT-HARDWARE3A");
        programmeGroupDoc3.setDefaultClassRoomId("class1kj");
        programmeGroupDoc3.setYearGroupList(new ArrayList<>(Arrays.asList("1", "2", "3")));
        programmeGroupDoc3.setProgrammeSubjectsCodeList(new ArrayList<>(Arrays.asList("ENG", "MATH", "INTSCIENCE", "SOCSTUDIES", "PHYSICS", "HARDWARE", "PLMP", "ECPOTR", "ICT", "PE")));
        programmeGroupDoc3.setTechnicalWorkshopOrLabRequired(false);

        List<ProgrammeGroupDoc> programmeGroupDocList = new ArrayList<>();
        programmeGroupDocList.add(programmeGroupDoc1);
        programmeGroupDocList.add(programmeGroupDoc2);
        programmeGroupDocList.add(programmeGroupDoc3);



        Mockito.when(programmeGroupDocRepository.findAll()).thenReturn(programmeGroupDocList);
        Mockito.when(programmeGroupDocRepository.findByYearGroup(1)).thenReturn(Arrays.asList(programmeGroupDoc1));
        Mockito.when(programmeGroupDocRepository.findByYearGroup(2)).thenReturn(Arrays.asList(programmeGroupDoc2));
        Mockito.when(programmeGroupDocRepository.findByYearGroup(3)).thenReturn(Arrays.asList(programmeGroupDoc3));


        TimeTableSuperDoc timeTableSuperDoc;
        timeTableSuperDoc = timeTablePopulatorService.partOneSetYearGroups();
        String timetableSuperDocString = new Gson().toJson(timeTableSuperDoc);
        System.out.println("***********TimeTable with year set = "+timetableSuperDocString);
        System.out.println("\n \n ********************Timetable pretty print json = "+ PrettyJSON.toPrettyFormat(timetableSuperDocString));
        assertThat(5,equalTo(timeTableSuperDoc.getYearGroupsList().get(0).getProgrammeGroupList().get(0).getProgrammeDaysList().size()));
    }
}
