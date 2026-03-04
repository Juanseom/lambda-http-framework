package edu.eci.arep.httpserver;

import java.io.*;
import java.net.*;

public class HttpServer {

    private static final int PORT = 8080;
    private static final String APP_PREFIX = "/App";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT + ".");
            e.printStackTrace();
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Ready to receive on port " + PORT + " ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            handleRequest(clientSocket);
        }

        serverSocket.close();
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        String requestLine = "";


        while ((inputLine = in.readLine()) != null) {
            if (requestLine.isEmpty()) {
                requestLine = inputLine;
            }
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }

        String outputLine = "";

        if (!requestLine.isEmpty()) {
            String[] parts = requestLine.split(" "); // Parse the request line: GET /App/hello?name=Pedro HTTP/1.1

            if (parts.length >= 2) {
                String method = parts[0];
                String fullPath = parts[1];

                String path = fullPath;
                String queryString = "";
                if (fullPath.contains("?")) {
                    path = fullPath.substring(0, fullPath.indexOf("?"));
                    queryString = fullPath.substring(fullPath.indexOf("?") + 1);
                }

                if (path.startsWith(APP_PREFIX)) {
                    String servicePath = path.substring(APP_PREFIX.length());
                    RouteHandler handler = LambdaFramework.getHandler(servicePath);

                    if (handler != null) {
                        Request req = new Request(method, servicePath, queryString);
                        Response res = new Response();
                        String body = handler.handle(req, res);

                        outputLine = "HTTP/1.1 200 OK\r\n"
                                + "Content-Type: text/plain\r\n"
                                + "\r\n"
                                + body;
                    } else {
                        outputLine = "HTTP/1.1 404 Not Found\r\n"
                                + "Content-Type: text/plain\r\n"
                                + "\r\n"
                                + "404 Not Found";
                    }
                } else {
                    outputLine = "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "\r\n"
                            + "404 Not Found";
                }
            }
        }

        out.println(outputLine);

        out.close();
        in.close();
        clientSocket.close();
    }
}




