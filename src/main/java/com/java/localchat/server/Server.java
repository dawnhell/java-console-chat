package com.java.localchat.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Console server class.
 * @author Vladislav Klochkov
 */

public class Server {
    private ServerSocket                 serverSocket       = null;
    private int                          port               = 8181;
    private Thread                       serverThread       = null;
    private BlockingQueue<SocketHandler> socketHandlerQueue = new LinkedBlockingQueue<SocketHandler>();
    private ArrayList<String>            clientUsernameList = new ArrayList<String>();
    private String                       serverStatus       = "not initialized";

    public String getServerStatus() {
        return this.serverStatus;
    }

    private class SocketHandler implements Runnable {
        private Socket         socket;
        private BufferedReader br;
        private BufferedWriter bw;
        private boolean        isAuthorized = false;
        private String         clientName   = null;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.br     = new BufferedReader(new InputStreamReader(socket.getInputStream ()));
            this.bw     = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        public String generateServerAnswer(String status, String clientName, String receiver, String message) {
            JSONObject answer = new JSONObject();
            answer.put("status", status);
            answer.put("name", clientName);
            answer.put("receiver", receiver);
            answer.put("message", message);
            answer.put("users", clientUsernameList);

            return answer.toString();
        }

        public boolean authorize() {
            System.out.println("Waiting for client's logging in.");

            String username = null;
            String password = null;
            try {
                username = br.readLine();
                password = br.readLine();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

            JSONObject jsonObject = null;
            JSONParser parser     = new JSONParser();
            try {
                FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/src/main/java/com/java/localchat/server/users.json");
                jsonObject = (JSONObject) parser.parse(fileReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JSONArray arr      = (JSONArray) jsonObject.get("users");
            Iterator<Object> iterator = arr.iterator();
            while (iterator.hasNext()) {
                JSONObject temp         = (JSONObject) iterator.next();
                String     tempUsername = (String) temp.get("username");
                String     tempPassword = (String) temp.get("password");
                if(username.equals(tempUsername) && password.equals(tempPassword)) {
                    System.out.println("Client " + username + " authorized.");
                    clientName = username;
                    if(!clientUsernameList.contains(clientName)) {
                        clientUsernameList.add(clientName);
                    }

                    for(SocketHandler socketHandler: socketHandlerQueue) {
                        socketHandler.sendMessage(generateServerAnswer("authorized", clientName, "everyone", "Has just connected."));
                    }
                    return true;
                }
            }
            System.out.println("Incorrect username or password.");
            sendMessage(generateServerAnswer("incorrect", "", clientName, ""));
            return false;
        }

        public boolean signup() {
            System.out.println("Waiting for client's singning up.");

            String username = null;
            String password = null;
            try {
                username = br.readLine();
                password = br.readLine();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

            JSONObject jsonObject = null;
            JSONParser parser     = new JSONParser();
            try {
                FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/src/main/server/users.json");
                jsonObject = (JSONObject) parser.parse(fileReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JSONArray  userArr = (JSONArray) jsonObject.get("users");
            JSONObject newUser = new JSONObject();

            newUser.put("username", username);
            newUser.put("password", password);
            userArr.add(newUser);
            JSONObject usersObject = new JSONObject();
            usersObject.put("users", userArr);

            try {
                FileWriter jsonFileWriter = new FileWriter(System.getProperty("user.dir") + "/src/main/server/users.json");
                jsonFileWriter.write(usersObject.toJSONString());
                jsonFileWriter.flush();
                jsonFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("User has been added.");

            clientName = username;
            if(!clientUsernameList.contains(clientName)) {
                clientUsernameList.add(clientName);
            }

            for(SocketHandler socketHandler: socketHandlerQueue) {
                socketHandler.sendMessage(generateServerAnswer("authorized", clientName, clientName, "Has just signed up!"));
            }

            return true;
        }

        public void run() {
            while(!socket.isClosed()) {
                while(!isAuthorized) {
                    sendMessage(generateServerAnswer("notAuthorized", "", clientName, ""));
                    String option = null;
                    try {
                        option = br.readLine();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    if(option.equals("login")){
                        isAuthorized = authorize();
                    }
                    if(option.equals("signup")){
                        isAuthorized = signup();
                    }
                }

                String receiver      = null;
                String clientMessage = null;
                try {
                    String response = br.readLine();
                    receiver = ((JSONObject) new JSONParser().parse(response)).get("receiver").toString();
                    clientMessage = ((JSONObject) new JSONParser().parse(response)).get("message").toString();
                } catch (Exception ex) {
                    System.out.println("Can't read client's authorization message.");
                    ex.printStackTrace();
                }

                if(clientMessage.equalsIgnoreCase("SHUTDOWN")) {
                    serverThread.interrupt();
                    try {
                        new Socket("localhost", port);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } finally {
                        shutdownServer();
                    }
                } else {
                    System.out.println("From " + clientName + ": '" + clientMessage + "' to " + receiver);

                    if(receiver.equals("everyone")) {
                        for(SocketHandler socketHandler: socketHandlerQueue) {
                            socketHandler.sendMessage(generateServerAnswer("authorized", clientName, "everyone", clientMessage));
                        }
                    } else {
                        System.out.println(receiver+ "SEnding to him");
                        for(SocketHandler socketHandler: socketHandlerQueue) {
                            if(socketHandler.clientName.equals(receiver)) {
                                socketHandler.sendMessage(generateServerAnswer("authorized", clientName, receiver, clientMessage));
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
            this.serverStatus = "initialized on " + this.port;
        } catch(IOException ioe) {
            System.out.println("Can't create server on port " + port);
            this.serverStatus = "can't initialize on " + port;
            ioe.printStackTrace();
        }
    }

    public synchronized void shutdownServer() {
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
                this.serverStatus = "accepted new client";
            } catch(IOException ioe) {
                ioe.printStackTrace();
                shutdownServer();
            }
            if(serverThread.isInterrupted()) {
                break;
            } else {
                if(socket != null) {
                    try {
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

