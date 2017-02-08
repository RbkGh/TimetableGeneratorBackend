package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.DepartmentDocRepository;
import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.DepartmentDoc;
import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import com.swiftpot.timetable.services.GeneralAbstractServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 1:48 PM
 *         TODO DONE!! ADD Initialization and crud of SubjectAllocationDoc upon every CRUD of SubjectDoc
 */
@RestController
@RequestMapping(path = "/subject")
public class SubjectsController {

    @Autowired
    SubjectDocRepository subjectDocRepository;
    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    DepartmentDocRepository departmentDocRepository;
    @Autowired
    GeneralAbstractServices generalAbstractServices;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload createSubjectDoc(@RequestBody SubjectDoc subjectDoc) {

        //check if subject code already exists first
        String subjectCode = subjectDoc.getSubjectCode();
        if (Objects.nonNull(subjectDocRepository.findBySubjectCodeAllIgnoreCase(subjectCode))) {
            return new ErrorOutgoingPayload("Subject Code Exists Already For Subject By Name:\n " + subjectDocRepository.findBySubjectCodeAllIgnoreCase(subjectCode).getSubjectFullName());
        } else if (subjectDoc.getSubjectYearGroupList().isEmpty() || Objects.isNull(subjectDoc.getSubjectYearGroupList())) {
            return new ErrorOutgoingPayload("Subject YearGroups cannot be empty.Please Choose at least one year group");
        } else {
            //create subjectAllocationDoc asynchronously for Subject when it's successfully saved without setting totalPeriodsForYearGroup,
            //as that will be updated later
            SubjectDoc subjectDocSaved = subjectDocRepository.save(subjectDoc);
            deleteAndCreateSubjectAllocationDocs(subjectDocSaved);

            return new SuccessfulOutgoingPayload(subjectDocSaved);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload getSubjectDoc(@PathVariable String id) {
        SubjectDoc subjectDoc = subjectDocRepository.findOne(id);
        if (Objects.isNull(subjectDoc)) {
            return new ErrorOutgoingPayload();
        } else {
            return new SuccessfulOutgoingPayload(subjectDoc);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload getAllSubjectDocs() {
        List<SubjectDoc> subjectDocs = subjectDocRepository.findAll();
        if (Objects.nonNull(subjectDocs)) {
            return new SuccessfulOutgoingPayload(subjectDocs);
        } else {
            return new ErrorOutgoingPayload();
        }
    }

    /*
    Don't allow user to update the subjectCode as this can cause trouble retrieving the subjectAllocationDoc
    *
    * */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload updateSubjectDoc(@PathVariable String id,
                                            @RequestBody SubjectDoc subjectDoc) {
        if (!subjectDocRepository.exists(id)) {
            return new ErrorOutgoingPayload("Id does not exist");
        } else if (subjectDoc.getSubjectYearGroupList().isEmpty() || Objects.isNull(subjectDoc.getSubjectYearGroupList())) {
            return new ErrorOutgoingPayload("Subject YearGroups cannot be empty.Please Choose at least one year group");
        } else {
            subjectDoc.setId(id);
            List<SubjectAllocationDoc> subjectAllocationDocsToDelete = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
            subjectAllocationDocRepository.delete(subjectAllocationDocsToDelete);
            SubjectDoc subjectDocSaved = subjectDocRepository.save(subjectDoc);
            deleteAndCreateSubjectAllocationDocs(subjectDoc);
            generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
            return new SuccessfulOutgoingPayload(subjectDocSaved);
        }

    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public OutgoingPayload deleteSubjectDoc(@PathVariable String id) {
        if (subjectDocRepository.exists(id)) {
            String subjectCode = subjectDocRepository.findOne(id).getSubjectCode();
            List<DepartmentDoc> allDepartmentDocs = departmentDocRepository.findAll();

            if (!allDepartmentDocs.isEmpty()) {
                List<DepartmentDoc> departmentDocsWithProgrammeSubjectDocId = new ArrayList<>();
                allDepartmentDocs.forEach(departmentDoc -> {
                    departmentDoc.getProgrammeSubjectsDocIdList().forEach(programmeSubjectDocId -> {
                        if (id.equalsIgnoreCase(programmeSubjectDocId)) {
                            departmentDocsWithProgrammeSubjectDocId.add(departmentDoc);
                        }
                    });
                });
                if (departmentDocsWithProgrammeSubjectDocId.isEmpty()) {
                    deleteSubjectAndAccompanyingSubjectAllocation(subjectCode, id);
                    generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
                    return new SuccessfulOutgoingPayload("Deleted Successfully");
                } else {
                    return new ErrorOutgoingPayload("Can not be deleted because a department holds a reference to this subject.");
                }
            } else { //there are no departments hence,continue to delete the subject
                deleteSubjectAndAccompanyingSubjectAllocation(subjectCode, id);
                generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
                return new SuccessfulOutgoingPayload("Deleted Successfully");
            }
        } else {
            return new ErrorOutgoingPayload("Id does not exist");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    private OutgoingPayload deleteAllSubjects() {
        if (subjectDocRepository.count() > 1) {
            subjectDocRepository.deleteAll();
            subjectAllocationDocRepository.deleteAll();
            generalAbstractServices.resetAllTutorsSubjectIdAndProgrammeCodeListProperty();//reset all tutor references.
            return new SuccessfulOutgoingPayload("Deleted Successfully");
        } else {
            return new ErrorOutgoingPayload("No Subjects To Delete Currently");
        }
    }

    private void deleteSubjectAndAccompanyingSubjectAllocation(String subjectCode, String subjectID) {
        List<SubjectAllocationDoc> subjectAllocationDocsToDelete = subjectAllocationDocRepository.findBySubjectCode(subjectCode);
        subjectAllocationDocRepository.delete(subjectAllocationDocsToDelete);
        subjectDocRepository.delete(subjectID);
    }

    /**
     * delete any existing SubjectAllocationDoc for subjectCode and recreate a new one whenever a new
     * subject is created or updated
     *
     * @param subjectDoc
     */
    private void deleteAndCreateSubjectAllocationDocs(SubjectDoc subjectDoc) {
        List<SubjectAllocationDoc> allSubjectDocsWithSubjectCodeIfPresent = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
        if (Objects.nonNull(allSubjectDocsWithSubjectCodeIfPresent)) {
            subjectAllocationDocRepository.delete(allSubjectDocsWithSubjectCodeIfPresent);
        }
        new Thread(() -> {
            List<SubjectAllocationDoc> subjectAllocationDocsToSaveInDb =
                    subjectDoc.getSubjectYearGroupList().stream().map(
                            yearGroupNo -> new SubjectAllocationDoc(subjectDoc.getSubjectCode(),
                                    yearGroupNo)).collect(Collectors.toList());
            subjectAllocationDocRepository.save(subjectAllocationDocsToSaveInDb);
        }).start();
    }


}
