package edu.eci.arep.httpserver;

public class Request {

    private String method;
    private String path;
    private String queryString;

    public Request(String method, String path, String queryString) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}

