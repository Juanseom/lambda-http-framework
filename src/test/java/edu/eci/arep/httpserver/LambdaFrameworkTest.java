package edu.eci.arep.httpserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LambdaFrameworkTest {
    @BeforeEach
    public void setUp() {
        LambdaFramework.get("/test", null);
    }
    @Test
    public void testRegisterAndGetRoute() {
        LambdaFramework.get("/hello", (req, res) -> "Hello World!");
        RouteHandler handler = LambdaFramework.getHandler("/hello");
        assertNotNull(handler);
    }
    @Test
    public void testRouteReturnsCorrectValue() {
        LambdaFramework.get("/hello", (req, res) -> "Hello World!");
        RouteHandler handler = LambdaFramework.getHandler("/hello");
        Request req = new Request("GET", "/hello", "");
        Response res = new Response();
        assertEquals("Hello World!", handler.handle(req, res));
    }
    @Test
    public void testRouteWithQueryParams() {
        LambdaFramework.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        RouteHandler handler = LambdaFramework.getHandler("/hello");
        Request req = new Request("GET", "/hello", "name=Pedro");
        Response res = new Response();
        assertEquals("Hello Pedro", handler.handle(req, res));
    }
    @Test
    public void testNonExistentRoute() {
        RouteHandler handler = LambdaFramework.getHandler("/nonexistent");
        assertNull(handler);
    }
    @Test
    public void testStaticFilesPath() {
        LambdaFramework.staticfiles("/webroot");
        assertEquals("/webroot", LambdaFramework.getStaticFilesPath());
    }
    @Test
    public void testPiRoute() {
        LambdaFramework.get("/pi", (req, res) -> String.valueOf(Math.PI));
        RouteHandler handler = LambdaFramework.getHandler("/pi");
        Request req = new Request("GET", "/pi", "");
        Response res = new Response();
        assertEquals(String.valueOf(Math.PI), handler.handle(req, res));
    }
}