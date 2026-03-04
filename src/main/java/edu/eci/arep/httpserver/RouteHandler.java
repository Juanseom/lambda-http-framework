package edu.eci.arep.httpserver;

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response res);
}

