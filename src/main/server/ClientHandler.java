package main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vladislav on 1/26/17.
 */

public class ClientHandler implements Runnable {
    private Socket         clientSocket;
    private BufferedReader bf;
    private PrintStream    ps;
    private ArrayList<PrintStream> clientOutputList;
    private int            clientNumber;

    public ClientHandler(Socket clientSocket, ArrayList<PrintStream> clientOutputList) {
        try {
            this.clientOutputList = clientOutputList;
            this.clientNumber = clientOutputList.size();
            this.clientSocket = clientSocket;
            this.bf = new BufferedReader(
                            new InputStreamReader(
                                this.clientSocket.getInputStream()));
            this.ps = clientOutputList.get(clientOutputList.size() - 1);
            //testing print stream correctness
            ps.println("this is test message");
            //end
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendToEveryone(String message) {
        Iterator iterator = clientOutputList.iterator();
        while(iterator.hasNext()) {
            PrintStream ps = (PrintStream) iterator.next();
            ps.println(message);
            ps.flush();
        }
    }

    public void run() {
        try {
            String clientMessage;
            while((clientMessage = bf.readLine()) != null) {
                System.out.println("#" + clientNumber + " client says:\n" + clientMessage);
                sendToEveryone("#" + clientNumber + " says: " + clientMessage);

                if(clientMessage.equalsIgnoreCase("QUIT")) {
                    System.out.println("Client #" + clientNumber + " is quitning.");
                    sendToEveryone("Client #" + clientNumber + " is quitning.");
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
