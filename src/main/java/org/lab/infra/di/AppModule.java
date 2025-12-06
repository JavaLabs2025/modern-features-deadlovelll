package org.lab.infra.di;

import com.google.inject.AbstractModule;
import org.lab.api.adapters.employee.*;
import org.lab.api.adapters.error_message.ErrorMessageCloseAdapter;
import org.lab.api.adapters.error_message.ErrorMessageCreateAdapter;
import org.lab.api.adapters.project.*;
import org.lab.api.adapters.ticket.*;
import org.lab.application.employee.use_cases.*;
import org.lab.application.error_message.services.CreateErrorMessageValidator;
import org.lab.application.error_message.services.ErrorMessageValidator;
import org.lab.application.error_message.use_cases.CloseErrorMessageUseCase;
import org.lab.application.error_message.use_cases.CreateErrorMessageUseCase;
import org.lab.application.project.use_cases.*;
import org.lab.application.ticket.use_cases.*;
import org.lab.application.shared.services.*;
import org.lab.application.ticket.services.*;
import org.lab.application.project.services.*;
import org.lab.core.utils.mapper.ObjectMapper;
import org.lab.domain.project.services.ProjectMembershipValidator;
import org.lab.infra.db.repository.employee.EmployeeRepository;
import org.lab.infra.db.repository.project.ProjectRepository;
import org.lab.infra.db.repository.ticket.TicketRepository;


public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EmployeeRepository.class).asEagerSingleton();
        bind(ProjectRepository.class).asEagerSingleton();
        bind(TicketRepository.class).asEagerSingleton();
        bind(ObjectMapper.class).asEagerSingleton();
        bind(EmployeePermissionValidator.class).asEagerSingleton();
        bind(ProjectProvider.class).asEagerSingleton();
        bind(EmployeeProvider.class).asEagerSingleton();
        bind(ProjectMembershipValidator.class).asEagerSingleton();
        bind(GetValidator.class).asEagerSingleton();
        bind(UserSpecFactory.class).asEagerSingleton();
        bind(TicketPermissionValidator.class).asEagerSingleton();
        bind(TicketCreateValidator.class).asEagerSingleton();
        bind(TicketCloseValidator.class).asEagerSingleton();
        bind(CreateErrorMessageValidator.class).asEagerSingleton();
        bind(ProjectSpecProvider.class).asEagerSingleton();
        bind(ErrorMessageValidator.class).asEagerSingleton();

        bind(CreateEmployeeUseCase.class).asEagerSingleton();
        bind(DeleteEmployeeUseCase.class).asEagerSingleton();
        bind(GetEmployeeUseCase.class).asEagerSingleton();
        bind(CreateProjectUseCase.class).asEagerSingleton();
        bind(GetProjectUseCase.class).asEagerSingleton();
        bind(DeleteProjectUseCase.class).asEagerSingleton();
        bind(ListProjectUseCase.class).asEagerSingleton();
        bind(CreateTicketUseCase.class).asEagerSingleton();
        bind(CloseTicketUseCase.class).asEagerSingleton();
        bind(CreateErrorMessageUseCase.class).asEagerSingleton();
        bind(CloseErrorMessageUseCase.class).asEagerSingleton();

        bind(EmployeeCreateAdapter.class).asEagerSingleton();
        bind(EmployeeDeleteAdapter.class).asEagerSingleton();
        bind(EmployeeGetAdapter.class).asEagerSingleton();
        bind(ProjectCreateAdapter.class).asEagerSingleton();
        bind(ProjectGetAdapter.class).asEagerSingleton();
        bind(ProjectDeleteAdapter.class).asEagerSingleton();
        bind(ProjectListAdapter.class).asEagerSingleton();
        bind(TicketCreateAdapter.class).asEagerSingleton();
        bind(TicketCloseAdapter.class).asEagerSingleton();
        bind(ErrorMessageCreateAdapter.class).asEagerSingleton();
        bind(ErrorMessageCloseAdapter.class).asEagerSingleton();
    }
}
