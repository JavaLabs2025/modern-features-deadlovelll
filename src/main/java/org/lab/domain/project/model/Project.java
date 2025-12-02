package org.lab.domain.project.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

import org.lab.domain.interfaces.DomainObject;
import org.lab.core.constants.project.ProjectStatus;

@Data
public class Project implements DomainObject {
    private int id;
    private String name;
    private String description;

    private int managerId;
    private Integer teamLeadId;

    private List<Integer> developerIds;
    private List<Integer> testerIds;

    private ProjectStatus status;

    private Integer currentMilestoneId;
    private List<Integer> milestoneIds;

    private List<Integer> bugReportIds;

    private Date createdDate;
    private int createdBy;
    private Date updatedDate;
    private Integer updatedBy;
}