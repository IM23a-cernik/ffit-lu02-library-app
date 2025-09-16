package ch.bzz;

import ch.bzz.db.BookPersistor;
import io.javalin.Javalin;

public class JavalinMain {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
            .get("/books", ctx -> ctx.json(BookPersistor.getAll(Integer.parseInt(ctx.queryParam("limit")))))
            .start(7070);
    }
}
