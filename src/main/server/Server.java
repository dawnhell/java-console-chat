package main.server;

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

    private class SocketHandler implements Runnable {
        private Socket            socket;
        private BufferedReader    br;
        private BufferedWriter    bw;
        private boolean           isAuthorized = false;
        private String            clientName   = null;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.br     = new BufferedReader(new InputStreamReader (socket.getInputStream ()));
            this.bw     = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
                FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/src/main/server/users.json");
                jsonObject = (JSONObject) parser.parse(fileReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JSONArray        arr      = (JSONArray) jsonObject.get("users");
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

                    JSONObject answer = new JSONObject();
                    answer.put("status", "authorized");
                    answer.put("name", clientName);
                    answer.put("message", "Has just connected.");
                    answer.put("users", clientUsernameList);
                    for(SocketHandler socketHandler: socketHandlerQueue) {
                        socketHandler.sendMessage(answer.toString());
                    }
                    sendMessage(answer.toString());
                    return true;
                }
            }
            System.out.println("Incorrect username or password.");
            JSONObject answer = new JSONObject();
            answer.put("status", "incorrect");
            sendMessage(answer.toString());
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

            JSONObject answer = new JSONObject();
            answer.put("status", "authorized");
            answer.put("name", clientName);
            answer.put("message", "Has just signed up!");
            answer.put("users", clientUsernameList);
            for(SocketHandler socketHandler: socketHandlerQueue) {
                socketHandler.sendMessage(answer.toString());
            }
//            sendMessage(answer.toString());

            return true;
        }

        public void run() {
            while(!socket.isClosed()) {
                while(!isAuthorized) {
                    JSONObject answer = new JSONObject();
                    answer.put("status", "notAuthorized");
                    sendMessage(answer.toString());
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
                String clientMessage = null;
                try {
                    clientMessage = br.readLine();
                } catch(IOException ioe) {
                    System.out.println("Can't read client's authorization message.");
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
                        System.out.println("From " + clientName + ": " + clientMessage);
                        JSONObject message = new JSONObject();
                        message.put("name", clientName);
                        message.put("message", clientMessage);
                        message.put("users", clientUsernameList);
                        for(SocketHandler socketHandler: socketHandlerQueue) {
                            System.out.println(message);
                            socketHandler.sendMessage(message.toString());
//                            socketHandler.sendMessage("From " + clientName + ": " + clientMessage);
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
