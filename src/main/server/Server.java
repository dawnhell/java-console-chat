package main.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by vladislav on 1/26/17.
 */

public class Server {
    private ServerSocket serverSocket               = null;
    private int    port                             = 8181;//By default
    private Thread serverThread                     = null;
    BlockingQueue<SocketHandler> socketHandlerQueue = new LinkedBlockingQueue<SocketHandler>();

    private class SocketHandler implements Runnable {
        private Socket socket;
        private BufferedReader br;
        private BufferedWriter bw;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            br          = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw          = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        public void run() {
            while(!socket.isClosed()) {
                String clientMessage = null;

                try {
                    clientMessage = br.readLine();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }

                if(clientMessage.equalsIgnoreCase("QUIT")) {
                    closeSocketConnection();
                } else {
                    if(clientMessage.equalsIgnoreCase("SHUTDOWN")) {
                        serverThread.interrupt();
                        try {
                            new Socket("localhost", port); //Creating faked socket connection, to be able to interrupt
                        } catch(IOException ioe) {
                            ioe.printStackTrace();
                        } finally {
                            shutdownServer();
                        }
                    } else {
                        System.out.println("From client: " + clientMessage);
                        for(SocketHandler socketHandler: socketHandlerQueue) {
                            socketHandler.sendMessage(clientMessage);
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
                ioe.printStackTrace();
            }
        }

        public synchronized void closeSocketConnection() {
            socketHandlerQueue.remove(this);
            if(!socket.isClosed()) {
                try {
                    socket.close();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        //Just in case. Not neccessary
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
                        final SocketHandler socketHandler = new SocketHandler(socket);
                        final Thread thread               = new Thread(socketHandler);//Separate asynchronouse Thread for reading from socket

                        thread.setDaemon(true);//Setting it to daemon
                        thread.start();//Starting thread
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
