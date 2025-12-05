package org.lab.application.project.dto;

import org.lab.domain.interfaces.PresentationObject;

import java.util.List;

public record CreateProjectDTO(
        String name,
        String description,
        int managerId,
        Integer teamLeadId,
        List<Integer>developerIds,
        List<Integer> testerIds
) implements PresentationObject {
}
