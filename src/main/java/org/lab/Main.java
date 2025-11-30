package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.create.EmployeeCreateAdapter;
import org.lab.api.adapters.employee.delete.EmployeeDeleteAdapter;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {}).start(7070);
        EmployeeCreateAdapter createEmployeeAdapter = new EmployeeCreateAdapter();
        EmployeeDeleteAdapter deleteEmployeeAdapter = new EmployeeDeleteAdapter();

        app.get("/", ctx -> ctx.result("Hello World"));

        app.post("/employee", createEmployeeAdapter::createEmployee);
        app.delete("/employee", deleteEmployeeAdapter::deleteEmployee);
    }
}
