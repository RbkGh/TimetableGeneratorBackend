package com.swiftpot.timetable;

import com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl;
import com.swiftpot.timetable.model.TutorSubjectIdAndProgrammeCodesListObj;
import com.swiftpot.timetable.repository.ProgrammeGroupDocRepository;
import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Tests for {@link com.swiftpot.timetable.base.impl.TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl}
 *
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         28-Feb-17 @ 8:57 AM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TutorSubjectAndProgrammeGroupInitialGeneratorTests {

    private static final Logger logger = LogManager.getLogger();

    @MockBean
    ProgrammeGroupDocRepository programmeGroupDocRepository;
    @MockBean
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @MockBean
    SubjectDocRepository subjectDocRepository;
    @Autowired
    TutorSubjectAndProgrammeGroupInitialGeneratorDefaultImpl tutorSubjectAndProgrammeGroupInitialGeneratorDefault;

    List<TutorDoc> tutorDocs;

    @Before
    public void setupMock() throws Exception {
        MockitoAnnotations.initMocks(this);
        String tutorDocId1 = "12345";
        TutorDoc tutorDoc = new TutorDoc();
        tutorDoc.setId(tutorDocId1);
        TutorSubjectIdAndProgrammeCodesListObj tutorSubjectIdAndProgrammeCodesListObj = new TutorSubjectIdAndProgrammeCodesListObj();
        String tutorSubjectId1 = "234B5";
        tutorSubjectIdAndProgrammeCodesListObj.setTutorSubjectId(tutorSubjectId1);
        String programmeCode1 = "BCT1", programmeCode2 = "BCT2";
        tutorSubjectIdAndProgrammeCodesListObj.setTutorProgrammeCodesList(Arrays.asList(programmeCode1, programmeCode2));
        List<TutorSubjectIdAndProgrammeCodesListObj> tutorSubjectIdAndProgrammeCodesListObjList = new ArrayList<>();
        tutorSubjectIdAndProgrammeCodesListObjList.add(tutorSubjectIdAndProgrammeCodesListObj);


        tutorDoc.setTutorSubjectsAndProgrammeCodesList(tutorSubjectIdAndProgrammeCodesListObjList);

        tutorDocs = new ArrayList<>(Arrays.asList(tutorDoc));

        //number 1 start
        SubjectDoc subjectDoc1 = new SubjectDoc();
        String subjectCode1 = "ENG";
        subjectDoc1.setSubjectCode(subjectCode1);

        ProgrammeGroupDoc programmeGroupDoc1 = new ProgrammeGroupDoc();
        int yearGroup1 = 1;
        programmeGroupDoc1.setYearGroup(yearGroup1);

        SubjectAllocationDoc subjectAllocationDoc1 = new SubjectAllocationDoc(subjectCode1, yearGroup1);
        int totalSubjectAllocation1 = 5;
        subjectAllocationDoc1.setTotalSubjectAllocation(totalSubjectAllocation1);
        //number 1 end


        //number 2 start
        SubjectDoc subjectDoc2 = new SubjectDoc();
        String subjectCode2 = "ENG";
        subjectDoc2.setSubjectCode(subjectCode2);

        ProgrammeGroupDoc programmeGroupDoc2 = new ProgrammeGroupDoc();
        int yearGroup2 = 2;
        programmeGroupDoc2.setYearGroup(yearGroup2);

        SubjectAllocationDoc subjectAllocationDoc2 = new SubjectAllocationDoc(subjectCode2, yearGroup2);
        int totalSubjectAllocation2 = 6;
        subjectAllocationDoc2.setTotalSubjectAllocation(totalSubjectAllocation2);
        //number 2 end


        //first loop start
        Mockito.when(subjectDocRepository.findOne(tutorSubjectId1)).thenReturn(subjectDoc1);
        Mockito.when(programmeGroupDocRepository.findByProgrammeCode(programmeCode1)).thenReturn(programmeGroupDoc1);
        Mockito.when(subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectCode1, yearGroup1)).thenReturn(subjectAllocationDoc1);
        //first loop end


        //second loop start
        Mockito.when(subjectDocRepository.findOne(tutorSubjectId1)).thenReturn(subjectDoc1);
        Mockito.when(programmeGroupDocRepository.findByProgrammeCode(programmeCode2)).thenReturn(programmeGroupDoc2);
        Mockito.when(subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectCode2, yearGroup2)).thenReturn(subjectAllocationDoc2);
        //second loop end
    }

    @Test(expected = AssertionError.class)
    public void testGetAllInitialSubjectAndProgrammeGroupCombinationDocsGeneratedExpectAssertionError() {
        List<TutorSubjectAndProgrammeGroupCombinationDoc> tutorSubjectAndProgrammeGroupCombinationDocs =
                tutorSubjectAndProgrammeGroupInitialGeneratorDefault.getAllInitialSubjectAndProgrammeGroupCombinationDocsGenerated(tutorDocs);


        logger.info("Generated List of {} = {}",
                TutorSubjectAndProgrammeGroupCombinationDoc.class.getCanonicalName(),
                PrettyJSON.toListPrettyFormat(tutorSubjectAndProgrammeGroupCombinationDocs));

        assertThat(3, equalTo(tutorSubjectAndProgrammeGroupCombinationDocs.size()));
    }

    @Test
    public void testGetAllInitialSubjectAndProgrammeGroupCombinationDocsGenerated() {
        List<TutorSubjectAndProgrammeGroupCombinationDoc> tutorSubjectAndProgrammeGroupCombinationDocs =
                tutorSubjectAndProgrammeGroupInitialGeneratorDefault.getAllInitialSubjectAndProgrammeGroupCombinationDocsGenerated(tutorDocs);


        logger.info("Generated List of {} = {}",
                TutorSubjectAndProgrammeGroupCombinationDoc.class.getCanonicalName(),
                PrettyJSON.toListPrettyFormat(tutorSubjectAndProgrammeGroupCombinationDocs));

        assertThat(2, equalTo(tutorSubjectAndProgrammeGroupCombinationDocs.size()));
    }
}
