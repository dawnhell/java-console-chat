package main.client;

import java.io.*;
import java.net.Socket;

/**
 * Client side console class.
 * @author Vladislav Klochkov
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
                        System.out.println("Server has been stopped.");
                        break;
                    }
                    System.out.println("Connection lost.");
                    closeSocketConnection();
                }

                if(message == null) {
                    System.out.println("Server closed this connection.");
                    closeSocketConnection();
                } else {
                    System.out.println(message);
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

            System.out.println("Connected to the server " + host + " on port " + port);

            boolean isAuthorized = false;
            String  serverSays   = null;

            while(!isAuthorized) {
                try {
                    serverSays = br.readLine();
                    System.out.println(serverSays);
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                if(serverSays.equals("Client is not authorized.")) {
                    System.out.println("You are not authorized. You can login or signup. Type your option:");
                    String option = null;
                    try {
                        option = userBR.readLine();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    while(!option.equals("login") && !option.equals("signup")) {
                        System.out.println("Error. Try again.");
                        try {
                            option = userBR.readLine();
                        } catch(IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                    if(option.equals("login")) {
                        try {
                            bw.write("login");
                            bw.write("\n");
                            bw.flush();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        while(!isAuthorized) {
                            System.out.println("Please, enter username and password:");

                            String username = null;
                            String password = null;
                            try {
                                username = userBR.readLine();
                                password = userBR.readLine();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            try {
                                bw.write(username);
                                bw.write("\n");
                                bw.write(password);
                                bw.write("\n");
                                bw.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            try {
                                serverSays = br.readLine();
                                System.out.println(serverSays);
                            } catch(IOException ioe) {
                                ioe.printStackTrace();
                            }
                            if (serverSays.equals("Client authorized.")) {
                                isAuthorized = true;
                                System.out.println("Client authorized.");
                            }
                        }
                    } else {
                        if(option.equals("signup")) {
                            try {
                                bw.write("signup");
                                bw.write("\n");
                                bw.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            try {
                                serverSays = br.readLine();
                            } catch(IOException ioe) {
                                ioe.printStackTrace();
                            }
                            System.out.println(serverSays);
                            String username = null;
                            String password = null;
                            try {
                                username = userBR.readLine();
                                password = userBR.readLine();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            try {
                                bw.write(username);
                                bw.write("\n");
                                bw.write(password);
                                bw.write("\n");
                                bw.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            try {
                                serverSays = br.readLine();
                            } catch(IOException ioe) {
                                ioe.printStackTrace();
                            }
                            System.out.println(serverSays);
                        }
                    }
                }
            }

            if(isAuthorized){
                System.out.println("Creating new Thread.");
                Thread newThread = new Thread(new ClientHandler()); //Creating
                newThread.start();                                 //and starting asynchronous Thread connection
            }
        } catch(IOException ioe) {
            System.out.println("Can't connect to the server " + host + ":" + port);
            ioe.printStackTrace();
        }
    }

    public synchronized void closeSocketConnection() {
        if(!socket.isClosed()) {
            try {
                socket.close();
                System.exit(0);
            } catch(IOException ioe) {
                System.out.println("Error found while closing socket connection.");
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
            if((message == null) ||
                    (message.equalsIgnoreCase("QUIT")) ||
                    (socket.isClosed())) {
                closeSocketConnection();
                break;
            } else {
                try {
                    bw.write(message);
                    bw.write("\n");
                    bw.flush();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
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
