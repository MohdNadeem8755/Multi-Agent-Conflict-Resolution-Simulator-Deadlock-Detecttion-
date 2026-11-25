package app;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("==================================================");
        System.out.println("   MULTI-AGENT CONFLICT RESOLUTION ENGINE API");
        System.out.println("==================================================");

        SimulationState state = new SimulationState();
        SimulationServer server = new SimulationServer(state);
        HttpServer httpServer = server.start(8080);

        System.out.println("API running at http://localhost:8080/api");
        System.out.println("Available endpoints: /snapshot, /start, /conflict, /resolve, /reset");
        System.out.println("Press Ctrl+C to stop the server.");

    }
}