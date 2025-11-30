import io.javalin.Javalin;
import org.lab.api.adapters.employee.create.EmployeeCreateAdapter;

void main() {
    var app = Javalin.create(/*config*/)
            .get("/", ctx -> ctx.result("Hello World"))
            .start(7070);

    EmployeeCreateAdapter adapter = new EmployeeCreateAdapter();
    app.post("/employee", adapter::createEmployee);
}