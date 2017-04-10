package test;

import main.client.Client;
import main.server.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTests {
    private Server server;
    private int    port = 8181;
    private String status = null;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Started Server tests.");
    }

    @Test
    @DisplayName("should initialize the server")
    public void initializeServerTest() {
        this.server = new Server(this.port);
        this.status = this.server.getServerStatus();
        this.server.shutdownServer();

        System.out.println("Server status: " + status);
        assertEquals("initialized on " + this.port, status);
    }

    @Test
    @DisplayName("should start server and connect the client to it")
    @Disabled
    public void startServerAndConnectClientTest() {
        this.server = new Server(this.port);
        this.server.runServer();

        Client client = new Client("localhost", this.port);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finished Server tests.");
    }
}
