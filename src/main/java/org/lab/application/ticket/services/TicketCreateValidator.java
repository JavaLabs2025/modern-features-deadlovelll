package org.lab.application.ticket.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

import com.google.inject.Inject;

import org.lab.application.shared.services.ProjectProvider;

public class TicketCreateValidator {

    private final TicketPermissionValidator ticketPermissionValidator;
    private final ProjectProvider projectProvider;

    @Inject
    public TicketCreateValidator(
            TicketPermissionValidator ticketPermissionValidator,
            ProjectProvider projectProvider
    ) {
        this.ticketPermissionValidator = ticketPermissionValidator;
        this.projectProvider = projectProvider;
    }

    public void validate(
            int employeeId,
            int projectId
    ) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()){
            scope.fork(() -> {
                this.ticketPermissionValidator.validate(employeeId);
                return null;
            });
            scope.fork(() -> {
                this.projectProvider.get(projectId);
                return null;
            });
            scope.join();
            scope.throwIfFailed();

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
