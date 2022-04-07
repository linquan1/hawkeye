package com.quan.hawkeye.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class HttpRequestHandler implements HttpHandler {


    private static final int BUFFER_SIZE = 1024;

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        System.out.println("get request: " + requestMethod);
        if (requestMethod.equalsIgnoreCase("POST")) {
            String requestBodyString = readFromStream(exchange.getRequestBody());
            System.out.println(requestBodyString);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 2);
            OutputStream os = exchange.getResponseBody();
            os.write("ab".getBytes());
            os.flush();
            os.close();
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD,0);
        }
        exchange.close();
    }

    private static String readFromStream(InputStream is) {
        try {
            byte[] ba = new byte [BUFFER_SIZE];
            int off = 0, c;
            while ((c = is.read(ba, off, ba.length)) != -1) {
                off += c;
            }
            return new String(ba, 0, off, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
