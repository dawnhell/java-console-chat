package main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by vladislav on 1/26/17.
 */

public class ClientHandler implements Runnable {
    private Socket         clientSocket;
    private BufferedReader bf;
    private PrintStream    ps;
    private int            clientNumber;

    public ClientHandler(Socket clientSocket, int clientNumber) {
        try {
            this.clientNumber = clientNumber;
            this.clientSocket = clientSocket;
            this.bf = new BufferedReader(
                            new InputStreamReader(
                                this.clientSocket.getInputStream()));
            this.ps = new PrintStream(
                            this.clientSocket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        try {
            String clientMessage;
            while((clientMessage = bf.readLine()) != null) {
                System.out.println("# " + clientNumber + " client says:\n" + clientMessage);
                ps.println("# " + clientNumber + " says: " + clientMessage);
                if(clientMessage.equalsIgnoreCase("QUIT")) {
                    System.out.println("Client # " + clientNumber + "is quitning.");
                    ps.println("Client # " + clientNumber + "is quitning.");//Must be told to EVERYONE
                    try {
                        bf.close();
                        ps.close();
                        clientSocket.close();
                        System.out.println("Client disconnected.");
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
