package org.lab.application.project.dto;

import org.lab.core.constants.project.ProjectStatus;
import org.lab.domain.interfaces.PresentationObject;

import java.util.Date;
import java.util.List;

public record GetProjectDTO(
        int id,
        String name,
        String description,

        int managerId,
        Integer teamLeadId,

        List<Integer>developerIds,
        List<Integer> testerIds,

        ProjectStatus status,

        Integer currentMilestoneId,
        List<Integer> milestoneIds,

        List<Integer> bugReportIds,

        Date createdDate,
        int createdBy,
        Date updatedDate,
        Integer updatedBy
) implements PresentationObject {
}
