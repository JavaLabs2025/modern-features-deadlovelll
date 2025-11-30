import io.javalin.Javalin;

void main() {
    var app = Javalin.create(/*config*/)
            .get("/", ctx -> ctx.result("Hello World"))
            .start(7070);
}