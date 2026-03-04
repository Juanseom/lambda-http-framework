package edu.eci.arep.httpserver;

import static edu.eci.arep.httpserver.LambdaFramework.get;
import static edu.eci.arep.httpserver.LambdaFramework.staticfiles;

public class App {

    public static void main(String[] args) throws Exception {
        staticfiles("/webroot");
        get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        get("/pi", (req, res) -> {
            return String.valueOf(Math.PI);
        });

        HttpServer.main(args);
    }
}

