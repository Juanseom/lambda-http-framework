package edu.eci.arep.httpserver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    @Test
    public void testGetValuesWithOneParam() {
        Request req = new Request("GET", "/hello", "name=Pedro");
        assertEquals("Pedro", req.getValues("name"));
    }

    @Test
    public void testGetValuesWithMultipleParams() {
        Request req = new Request("GET", "/hello", "name=Pedro&age=20");
        assertEquals("Pedro", req.getValues("name"));
        assertEquals("20", req.getValues("age"));
    }

    @Test
    public void testGetValuesWithMissingParam() {
        Request req = new Request("GET", "/hello", "name=Pedro");
        assertEquals("", req.getValues("city"));
    }

    @Test
    public void testGetValuesWithEmptyQueryString() {
        Request req = new Request("GET", "/hello", "");
        assertEquals("", req.getValues("name"));
    }

    @Test
    public void testGetValuesWithNullQueryString() {
        Request req = new Request("GET", "/hello", null);
        assertEquals("", req.getValues("name"));
    }

    @Test
    public void testGetMethod() {
        Request req = new Request("GET", "/hello", "");
        assertEquals("GET", req.getMethod());
    }

    @Test
    public void testGetPath() {
        Request req = new Request("GET", "/hello", "");
        assertEquals("/hello", req.getPath());
    }

    @Test
    public void testGetQueryString() {
        Request req = new Request("GET", "/hello", "name=Pedro");
        assertEquals("name=Pedro", req.getQueryString());
    }
}

