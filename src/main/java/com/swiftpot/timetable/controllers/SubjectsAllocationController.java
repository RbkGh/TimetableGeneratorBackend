package com.swiftpot.timetable.controllers;

import com.swiftpot.timetable.model.ErrorOutgoingPayload;
import com.swiftpot.timetable.model.OutgoingPayload;
import com.swiftpot.timetable.model.SuccessfulOutgoingPayload;
import com.swiftpot.timetable.repository.SubjectAllocationDocRepository;
import com.swiftpot.timetable.repository.SubjectDocRepository;
import com.swiftpot.timetable.repository.db.model.SubjectAllocationDoc;
import com.swiftpot.timetable.repository.db.model.SubjectDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 6:55 PM
 */
@RestController
@RequestMapping(path = "/subject/allocation")
public class SubjectsAllocationController {

    @Autowired
    SubjectAllocationDocRepository subjectAllocationDocRepository;
    @Autowired
    SubjectDocRepository subjectDocRepository;

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OutgoingPayload updateSubjectAllocation(@RequestBody SubjectAllocationDoc subjectAllocationDoc) {
        System.out.println("request recieved");

        if (subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectAllocationDoc.getSubjectCode(), subjectAllocationDoc.getYearGroup()) != null) {
            SubjectAllocationDoc existingSubDoc = subjectAllocationDocRepository.findBySubjectCodeAndYearGroup(subjectAllocationDoc.getSubjectCode(), subjectAllocationDoc.getYearGroup());
            subjectAllocationDoc.setId(existingSubDoc.getId());
            SubjectAllocationDoc subjectAllocationDocSaved = subjectAllocationDocRepository.save(subjectAllocationDoc);
            return new SuccessfulOutgoingPayload(subjectAllocationDocSaved);
        } else {
            return new ErrorOutgoingPayload("Subject YearGroup and subject code does not exist");
        }
    }

    /**
     * if one subjectAllocationDoc is not set,then set {SubjectDoc's isAllSubjectYearGroupsAllocated property} to false
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/state")
    public OutgoingPayload getAllSubjectsAllocationState() {
        //get all subjects
        List<SubjectDoc> subjectDocs = subjectDocRepository.findAll();
        if (!(subjectDocs.size() > 0)) {
            return new SuccessfulOutgoingPayload(subjectDocs);
        } else {
            subjectDocs.forEach((subjectDoc -> {
                //find all subjectAllocationDocs by subjectCode
                List<SubjectAllocationDoc> subjectAllocationDocs = subjectAllocationDocRepository.findBySubjectCode(subjectDoc.getSubjectCode());
                ArrayList<Boolean> booleanArrayList = new ArrayList<>();
                subjectAllocationDocs.forEach((subjectAllocationDoc) -> {
                    int totalSubjectAllocation = subjectAllocationDoc.getTotalSubjectAllocation();
                    System.out.println("Total Subject Allocation = " + totalSubjectAllocation);
                    //uninitialized java int have a default of zero,zero means uninitialized
                    if (Objects.equals(subjectAllocationDoc.getTotalSubjectAllocation(), 0)) {
                        //totalSubjectAllocation is null so add false
                        booleanArrayList.add(false);
                    } else {
                        //totalSubjectAllocation is not 0 so add true since it's allocated
                        booleanArrayList.add(true);
                    }
                    //now check if false exists in the list,then render subjectDoc's isAllSubjectYearGroupsAllocated as false
                    if (booleanArrayList.contains(false)) {
                        subjectDoc.setIsAllSubjectYearGroupsAllocated(false);
                    } else {
                        subjectDoc.setIsAllSubjectYearGroupsAllocated(true);
                    }
                });
            }));
        }

        return new SuccessfulOutgoingPayload(subjectDocs);
    }
}
