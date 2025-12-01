package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.EmployeeCreateAdapter;
import org.lab.api.adapters.employee.EmployeeDeleteAdapter;
import org.lab.api.adapters.employee.EmployeeGetAdapter;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {}).start(7070);
        EmployeeCreateAdapter createEmployeeAdapter = new EmployeeCreateAdapter();
        EmployeeDeleteAdapter deleteEmployeeAdapter = new EmployeeDeleteAdapter();
        EmployeeGetAdapter getEmployeeAdapter = new EmployeeGetAdapter();

        app.get("/", ctx -> ctx.result("Hello World"));

        app.post("/employee", createEmployeeAdapter::createEmployee);
        app.delete("/employee", deleteEmployeeAdapter::deleteEmployee);
        app.get("/employee", getEmployeeAdapter::getEmployee);
    }
}
