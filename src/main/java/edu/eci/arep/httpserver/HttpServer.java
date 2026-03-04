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
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        OutputStream outStream = clientSocket.getOutputStream();

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

        if (!requestLine.isEmpty()) {
            String[] parts = requestLine.split(" ");

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
                    handleRestRequest(method, path, queryString, outStream);
                } else {
                    handleStaticFile(path, outStream);
                }
            }
        }

        outStream.close();
        in.close();
        clientSocket.close();
    }

    private static void handleRestRequest(String method, String path, String queryString, OutputStream outStream) throws IOException {
        String servicePath = path.substring(APP_PREFIX.length());
        RouteHandler handler = LambdaFramework.getHandler(servicePath);

        String response;
        if (handler != null) {
            Request req = new Request(method, servicePath, queryString);
            Response res = new Response();
            String body = handler.handle(req, res);

            response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + body;
        } else {
            response = "HTTP/1.1 404 Not Found\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + "404 Not Found";
        }
        outStream.write(response.getBytes());
    }

    private static void handleStaticFile(String path, OutputStream outStream) throws IOException {
        String staticFolder = LambdaFramework.getStaticFilesPath();
        String filePath = staticFolder + path;

        InputStream fileStream = HttpServer.class.getResourceAsStream(filePath);

        if (fileStream == null) {
            String response = "HTTP/1.1 404 Not Found\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + "404 Not Found";
            outStream.write(response.getBytes());
            return;
        }

        String contentType = getContentType(path);
        byte[] fileBytes = fileStream.readAllBytes();
        fileStream.close();

        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + fileBytes.length + "\r\n"
                + "\r\n";
        outStream.write(header.getBytes());
        outStream.write(fileBytes);
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }
}




