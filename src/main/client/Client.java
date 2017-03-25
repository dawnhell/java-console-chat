package main.client;

import main.clientGUI.ClientGUI;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 * Client side console class.
 * @author Vladislav Klochkov
 */

public class Client extends ClientGUI {
    private Socket              socket      = null;
    private BufferedReader      br          = null;
    private BufferedWriter      bw          = null;
    private BufferedReader      userBR      = null;
    private static final String defaultHost = "localhost";
    private static final int    defaultPort = 8181;

    private static boolean isAuthorized = false;

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

            createAndShowAuthorization();
            while(!isAuthorized) {
                String serverSays = null;
                try {
                    serverSays = br.readLine();
                    System.out.println(serverSays);
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }

                if(serverSays.equals("notAuthorized")) {
                    loginJButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                bw.write("login");
                                bw.write("\n");
                                bw.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }

                            if (usernameJTextField.getText().length() == 0) {
                                loginInvalidJLabel = new JLabel("Enter login!");
                                gridBagConstraints = new GridBagConstraints();
                                gridBagConstraints.anchor = GridBagConstraints.CENTER;
                                gridBagConstraints.insets = new Insets(0, 0, 10, 0);
                                gridBagConstraints.gridx = 2;
                                gridBagConstraints.gridy = 2;
                                gridBagConstraints.ipadx = 5;
                                gridBagConstraints.ipady = 5;

                                authJFrame.add(loginInvalidJLabel, gridBagConstraints);
                                authJFrame.revalidate();
                            } else {
                                if (passwordJPasswordField.getPassword().length == 0) {
                                    passwordInvalidJLabel = new JLabel("Enter password!");
                                    gridBagConstraints = new GridBagConstraints();
                                    gridBagConstraints.anchor = GridBagConstraints.CENTER;
                                    gridBagConstraints.insets = new Insets(0, 0, 10, 0);
                                    gridBagConstraints.gridx = 2;
                                    gridBagConstraints.gridy = 4;
                                    gridBagConstraints.ipadx = 5;
                                    gridBagConstraints.ipady = 5;

                                    authJFrame.add(passwordInvalidJLabel, gridBagConstraints);
                                    authJFrame.revalidate();
                                } else {
                                    if (usernameJTextField.getText().length() != 0 &&
                                            passwordJPasswordField.getPassword().length != 0) {
                                        try {
                                            bw.write(usernameJTextField.getText());
                                            bw.write("\n");
                                            bw.write(passwordJPasswordField.getPassword());
                                            bw.write("\n");
                                            bw.flush();
                                        } catch (IOException ioe) {
                                            ioe.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

                if (serverSays.equals("authorized")) {
                    System.out.println("in author");
                    isAuthorized = true;
                    authJFrame.setVisible(false);
                    createAndShowGIU();

                    System.out.println("Creating new Thread.");
                    Thread newThread = new Thread(new ClientHandler()); //Creating
                    newThread.start();                                 //and starting asynchronous Thread connection
                }

                if(serverSays.equals("incorrect")) {
                    System.out.println("not author");
                    createAndShowIncorrectAuthJLabel(authJFrame);
                    authJFrame.revalidate();
                }
//                        if(option.equals("signup")) {
//                            try {
//                                bw.write("signup");
//                                bw.write("\n");
//                                bw.flush();
//                            } catch (IOException ioe) {
//                                ioe.printStackTrace();
//                            }
//                            try {
//                                serverSays = br.readLine();
//                            } catch(IOException ioe) {
//                                ioe.printStackTrace();
//                            }
//                            System.out.println(serverSays);
//                            String username = null;
//                            String password = null;
//                            try {
//                                username = userBR.readLine();
//                                password = userBR.readLine();
//                            } catch (IOException ioe) {
//                                ioe.printStackTrace();
//                            }
//                            try {
//                                bw.write(username);
//                                bw.write("\n");
//                                bw.write(password);
//                                bw.write("\n");
//                                bw.flush();
//                            } catch (IOException ioe) {
//                                ioe.printStackTrace();
//                            }
//                            try {
//                                serverSays = br.readLine();
//                            } catch(IOException ioe) {
//                                ioe.printStackTrace();
//                            }
//                            System.out.println(serverSays);
//                        }
//                    }
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

    public void sendMessageToServer(String message) {
        try {
            bw.write(message);
            bw.write("\n");
            bw.flush();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void runClient() {
        System.out.println("Enter your message(quit for exiting)");
        messagesJTextArea.append("Enter your message('quit' for exiting)");
        sendJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientMessageJTextField.getText();
                if(message.equalsIgnoreCase("QUIT") || socket.isClosed()) {
                    closeSocketConnection();
                    System.out.println("Closed socket connection");
                    clientMessageJTextField.setText("Closed socket connection");
                } else {
                    if(message.length() == 0) {
                        clientMessageJTextField.setText("Enter your message!");
                    } else {
                        sendMessageToServer(message);
                        clientMessageJTextField.setText("");
                        messagesJTextArea.append("\n" + usernameJTextField.getText() + ": " + message);
                    }
                }
            }
        });
    }

    public static void main(String []args) {
        Client client = new Client(defaultHost, defaultPort);
        client.runClient();
    }
}
