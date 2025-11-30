package org.lab;

import io.javalin.Javalin;
import org.lab.api.adapters.employee.create.EmployeeCreateAdapter;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
        }).start(7070);

        // Создаём адаптер
        EmployeeCreateAdapter adapter = new EmployeeCreateAdapter();

        // Регистрируем POST-эндпоинт
        app.post("/employee", ctx -> {
            // Передаём ctx в метод адаптера
            adapter.createEmployee(ctx);
        });

        // Пример GET для проверки
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
