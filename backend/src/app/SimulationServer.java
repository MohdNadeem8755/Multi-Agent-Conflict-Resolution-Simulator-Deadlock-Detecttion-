package app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimulationServer {

    private final SimulationState state;

    public SimulationServer(SimulationState state) {
        this.state = state;
    }

    public HttpServer start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/snapshot", new SnapshotHandler());
        server.createContext("/api/start", new ActionHandler("start"));
        server.createContext("/api/conflict", new ActionHandler("conflict"));
        server.createContext("/api/resolve", new ActionHandler("resolve"));
        server.createContext("/api/reset", new ActionHandler("reset"));
        server.createContext("/api/health", new HealthHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        return server;
    }

    private class SnapshotHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            sendJson(exchange, 200, state.snapshotJson());
        }
    }

    private class ActionHandler implements HttpHandler {

        private final String action;

        private ActionHandler(String action) {
            this.action = action;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }

            switch (action) {
                case "start":
                    state.start();
                    break;
                case "conflict":
                    state.generateConflict();
                    break;
                case "resolve":
                    state.resolveDeadlock();
                    break;
                case "reset":
                    state.reset();
                    break;
                default:
                    break;
            }

            sendJson(exchange, 200, state.snapshotJson());
        }
    }

    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            sendCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            sendJson(exchange, 200, "{\"status\":\"ok\"}");
        }
    }

    private void sendCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
    }

    private void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, payload.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(payload);
        }
    }

}