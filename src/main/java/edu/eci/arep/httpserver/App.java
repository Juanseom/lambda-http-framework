package edu.eci.arep.httpserver;

import static edu.eci.arep.httpserver.LambdaFramework.get;

public class App {

    public static void main(String[] args) throws Exception {
        get("/hello", (req, res) -> "Hello World!");

        HttpServer.main(args);
    }
}

