package org.lab.api.adapters.employee.create;

import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.application.employee.dto.get.GetEmployeeDTO;
import org.lab.application.employee.dto.create.CreateEmployeeDTO;
import org.lab.domain.emploee.model.Employee;
import org.lab.application.employee.use_cases.create.CreateEmployeeUseCase;
import org.lab.domain.shared.exceptions.NotPermittedException;

public class EmployeeCreateAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CreateEmployeeUseCase useCase = new CreateEmployeeUseCase();

    public GetEmployeeDTO getEmployee(
            CreateEmployeeDTO createEmployeeDTO
    ) {
        try {
            Employee employee = mapper.mapToDomain(createEmployeeDTO, Employee.class);
            Employee createdEmployee = useCase.execute(employee);
            GetEmployeeDTO presentationEmployee = mapper.mapToPresentation(
                    createdEmployee,
                    GetEmployeeDTO.class
            );
            return presentationEmployee;
        } catch (NotPermittedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
