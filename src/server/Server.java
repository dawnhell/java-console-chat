package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by vladislav on 1/26/17.
 */

public class Server {
    private ServerSocket serverSocket = null;
    private boolean      serverOn     = true;
    private int          clientNumber = 0;
    private final int    PORT         = 8181;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running on port " + PORT);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        while(serverOn) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted new connection: " + socket.getInetAddress());

                Runnable clientHandler = new ClientHandler(socket, clientNumber++);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    public static void main(String []args) {
        Server server = new Server();
    }
}
