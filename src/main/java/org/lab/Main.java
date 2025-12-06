package org.lab;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import io.javalin.Javalin;

import org.lab.api.adapters.employee.EmployeeCreateAdapter;
import org.lab.api.adapters.employee.EmployeeDeleteAdapter;
import org.lab.api.adapters.employee.EmployeeGetAdapter;
import org.lab.api.adapters.error_message.ErrorMessageCloseAdapter;
import org.lab.api.adapters.error_message.ErrorMessageCreateAdapter;
import org.lab.api.adapters.project.ProjectCreateAdapter;
import org.lab.api.adapters.project.ProjectDeleteAdapter;
import org.lab.api.adapters.project.ProjectGetAdapter;
import org.lab.api.adapters.project.ProjectListAdapter;
import org.lab.api.adapters.ticket.TicketCloseAdapter;
import org.lab.api.adapters.ticket.TicketCreateAdapter;
import org.lab.infra.di.AppModule;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new AppModule());
        Javalin app = Javalin.create(config -> {}).start(7070);

        EmployeeCreateAdapter createEmployeeAdapter = injector.getInstance(EmployeeCreateAdapter.class);
        EmployeeDeleteAdapter deleteEmployeeAdapter = injector.getInstance(EmployeeDeleteAdapter.class);
        EmployeeGetAdapter getEmployeeAdapter = injector.getInstance(EmployeeGetAdapter.class);

        ProjectCreateAdapter createProjectAdapter = injector.getInstance(ProjectCreateAdapter.class);
        ProjectGetAdapter projectGetAdapter = injector.getInstance(ProjectGetAdapter.class);
        ProjectDeleteAdapter projectDeleteAdapter = injector.getInstance(ProjectDeleteAdapter.class);
        ProjectListAdapter projectListAdapter = injector.getInstance(ProjectListAdapter.class);

        TicketCreateAdapter ticketCreateAdapter = injector.getInstance(TicketCreateAdapter.class);
        TicketCloseAdapter ticketCloseAdapter = injector.getInstance(TicketCloseAdapter.class);

        ErrorMessageCreateAdapter errorMessageCreateAdapter = injector.getInstance(
                ErrorMessageCreateAdapter.class
        );
        ErrorMessageCloseAdapter errorMessageCloseAdapter = injector.getInstance(
                ErrorMessageCloseAdapter.class
        );

        app.get("/", ctx -> ctx.result("Hello World"));

        app.post("/employee/{actorId}", createEmployeeAdapter::createEmployee);
        app.delete("/employee/{employeeId}/{actorId}", deleteEmployeeAdapter::deleteEmployee);
        app.get("/employee/{employeeId}/{actorId}", getEmployeeAdapter::getEmployee);

        app.get("/project/list/{employeeId}", projectListAdapter::listProjects);
        app.post("/project/{employeeId}", createProjectAdapter::createProject);
        app.get("/project/{projectId}/{employeeId}", projectGetAdapter::getProject);
        app.delete("/project/{projectId}/{employeeId}", projectDeleteAdapter::deleteProject);

        app.post("/ticket/{employeeId}/{projectId}",  ticketCreateAdapter::createTicket);
        app.patch("/ticket/{employeeId}/{ticketId}",  ticketCloseAdapter::closeTicket);

        app.post("/error_message/{employeeId}", errorMessageCreateAdapter::createErrorMessage);
        app.patch("/error_message/{messageId}", errorMessageCloseAdapter::closeMessage);
    }
}
