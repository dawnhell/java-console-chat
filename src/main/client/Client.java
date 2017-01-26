package main.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by vladislav on 1/26/17.
 */

public class Client {
    private  Socket         socket = null;
    private  BufferedReader br     = null;
    private  BufferedWriter bw     = null;
    private  BufferedReader userBR = null;

    private class ClientHandler implements Runnable {
        public void run() {
            while(!socket.isClosed()) {
                String message = null;
                try {
                    message = br.readLine();
                } catch(IOException ioe) {
                    if("Socket closed".equals(ioe.getMessage())) {
                        System.out.println("Server is stopped");
                        break;
                    }
                    System.out.println("Connection lost");
                    closeSocketConnection();
                }

                if(message == null) {
                    System.out.println("Server closed this connection");
                    closeSocketConnection();
                } else {
                    System.out.println("Server: " + message);
                }
            }
        }
    }

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            br     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw     = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            userBR = new BufferedReader(new InputStreamReader(System.in));

            Thread newThread = new Thread(new ClientHandler()); //Creating
            newThread.start();                             //and starting asynchronous Thread connection
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public synchronized void closeSocketConnection() {
        if(!socket.isClosed()) {
            try {
                socket.close();
                System.exit(0);
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void runClient() {
        System.out.println("Enter your message(quit for exiting)");
        while(true) {
            String message = null;
            try {
                message = userBR.readLine();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

            if((message == null) || (message.equalsIgnoreCase("QUIT")) || (socket.isClosed())) {
                closeSocketConnection();
                break;
            } else {
                try {
                    bw.write(message);
                    bw.write("\n");
                    bw.flush();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                    closeSocketConnection();
                }
            }
        }
    }

    public static void main(String []args) throws IOException {
        final int       PORT   = 8181;
        final String    HOST   = "localhost";

        Client client;
        client = new Client(HOST, PORT);
        client.runClient();
    }
}
