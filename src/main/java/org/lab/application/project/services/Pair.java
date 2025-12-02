package org.lab.application.project.services;

import org.lab.domain.emploee.model.Employee;
import org.lab.domain.project.model.Project;

public record Pair(Project project, Employee employee) {}
