package main.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.*;

/**
 * Console server class.
 * @author Vladislav Klochkov
 */

public class Server {
    private ServerSocket                 serverSocket       = null;
    private int                          port               = 8181;
    private Thread                       serverThread       = null;
    private BlockingQueue<SocketHandler> socketHandlerQueue = new LinkedBlockingQueue<SocketHandler>();

    private class SocketHandler implements Runnable {
        private Socket         socket;
        private BufferedReader br;
        private BufferedWriter bw;
        private boolean        isAuthorized = false;
        private String         clientName   = null;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.br     = new BufferedReader(new InputStreamReader (socket.getInputStream ()));
            this.bw     = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        public boolean authorize() {
            System.out.println("Waiting for client's authorization.");

            String username = null;
            String password = null;
            try {
                username = br.readLine();
                password = br.readLine();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/out/production/CourseWork/main/server/users.json"))));
            } catch(FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            JSONArray arr = (JSONArray) jsonObject.get("users");
            Iterator<Object> iterator = arr.iterator();
            while (iterator.hasNext()) {
                JSONObject temp = (JSONObject) iterator.next();
                String tempUsername = (String) temp.get("username");
                String tempPassword = (String) temp.get("password");
                if(username.equals(tempUsername) && password.equals(tempPassword)) {
                    System.out.println("Client " + username + " authorized.");
                    clientName = username;
                    sendMessage("Client authorized.");
                    return true;
                }
            }
            System.out.println("Incorrect username or password.");
            sendMessage("Incorrect username or password. Please, try again.");
            return false;
        }

        public void signUp() {
            sendMessage("Enter username and password.");

            String username = null;
            String password = null;
            try {
                username = br.readLine();
                password = br.readLine();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/main/server/users.json"))));
            } catch(FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            JSONArray userAarr = (JSONArray) jsonObject.get("users");
            JSONObject newUser = new JSONObject();
            newUser.put("username", username);
            newUser.put("password", password);
            userAarr.put(newUser);
            JSONObject usersObject = new JSONObject();
            usersObject.put("users", userAarr);
            try(FileWriter file = new FileWriter(System.getProperty("user.dir") + "/src/main/server/users.json")) {
                    file.write(usersObject.toString());
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            sendMessage("User has been added.");
        }

        public void run() {
            if(!isAuthorized){
                sendMessage("Client is not authorized.");
            }
            while(!socket.isClosed()) {
                if(!isAuthorized) {
                    String option = null;
                    try {
                        option = br.readLine();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    if(option.equals("login")){
                        isAuthorized = authorize();
                    } else {
                        if(option.equals("signup")){
                            signUp();
                            isAuthorized = authorize();
                        }
                    }
                } else {
                    String clientMessage = null;
                    try {
                        clientMessage = br.readLine();
                    } catch(IOException ioe) {
                        System.out.println("Can't read client's message.");
                        ioe.printStackTrace();
                    }

                    if(clientMessage.equalsIgnoreCase("QUIT")) {
                        closeSocketConnection();
                    } else {
                        if(clientMessage.equalsIgnoreCase("SHUTDOWN")) {
                            serverThread.interrupt();
                            try {
                                /** Creating faked socket connection, to be able to interrupt */
                                new Socket("localhost", port);
                            } catch(IOException ioe) {
                                ioe.printStackTrace();
                            } finally {
                                shutdownServer();
                            }
                        } else {
                            System.out.println("From client " + clientName + " : " + clientMessage);
                            for(SocketHandler socketHandler: socketHandlerQueue) {
                                socketHandler.sendMessage("From client " + clientName + " : " + clientMessage);
                            }
                        }
                    }
                }
            }
        }

        public synchronized void sendMessage(String message) {
            try {
                bw.write(message);
                bw.write("\n");
                bw.flush();
            } catch(IOException ioe) {
                System.out.println("Can't send message to client.");
                ioe.printStackTrace();
            }
        }

        public synchronized void closeSocketConnection() {
            socketHandlerQueue.remove(this);
            if(!socket.isClosed()) {
                try {
                    socket.close();
                } catch(IOException ioe) {
                    System.out.println("Can't close socket connection.");
                    ioe.printStackTrace();
                }
            }
        }

        //Clear memory in the end. Not neccessary.
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            closeSocketConnection();
        }
    }

    public Server(int port) {
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);
        } catch(IOException ioe) {
            System.out.println("Can't create server on port " + port);
            ioe.printStackTrace();
        }
    }

    private synchronized void shutdownServer() {
        for(SocketHandler socketHandler: socketHandlerQueue) {
            socketHandler.closeSocketConnection();
        }
        if(!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void runServer() {
        serverThread  = Thread.currentThread();
        Socket socket = null;

        while(true) {
            try {
                socket = serverSocket.accept();
                System.out.println("Accepted from " + socket.getInetAddress());
            } catch(IOException ioe) {
                ioe.printStackTrace();
                shutdownServer();
            }
            if(serverThread.isInterrupted()) {
                break;
            } else {
                if(socket != null) {
                    try {
                        //Separate asynchronous Thread for reading from socket
                        final SocketHandler socketHandler = new SocketHandler(socket);
                        final Thread thread               = new Thread(socketHandler);

                        thread.setDaemon(true);                 //Setting it to daemon
                        thread.start();                         //Starting thread
                        socketHandlerQueue.offer(socketHandler);//Adding in the list of active socket-processors
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String []args) throws IOException {
        Server server = new Server(8181);
        server.runServer();
    }
}
