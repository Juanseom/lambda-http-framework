package edu.eci.arep.httpserver;

import java.util.HashMap;
import java.util.Map;

public class LambdaFramework {

    private static Map<String, RouteHandler> routes = new HashMap<>();
    private static String staticFilesPath = "";

    public static void get(String path, RouteHandler handler) {
        routes.put(path, handler);
    }

    public static void staticfiles(String path) {
        staticFilesPath = path;
    }

    public static RouteHandler getHandler(String path) {
        return routes.get(path);
    }


    public static String getStaticFilesPath() {
        return staticFilesPath;
    }
}

