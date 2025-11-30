package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.create.EmployeeCreateAdapter;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {}).start(7070);
        EmployeeCreateAdapter adapter = new EmployeeCreateAdapter();
        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/employee", adapter::createEmployee);
    }
}
