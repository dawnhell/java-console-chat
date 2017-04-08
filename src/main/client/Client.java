package main.client;

import main.clientGUI.ClientGUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.*;

/**
 * Client side console class.
 * @author Vladislav Klochkov
 */

public class Client extends ClientGUI {
    private Socket                   socket             = null;
    private BufferedReader           br                 = null;
    private BufferedWriter           bw                 = null;
    private static final String      defaultHost        = "localhost";
    private static final int         defaultPort        = 8181;
    private static ArrayList<String> clientUsernameList = new ArrayList<String>();
    private static JSONParser        jsonParser         = new JSONParser();

    private static boolean isAuthorized = false;
    private static String  currentClientUsername = null;

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
                    String clientUsername = null;
                    String clientMessage  = null;
                    String receiver       = null;
                    try {
                        clientUsername  = (String)    ((JSONObject) jsonParser.parse(message)).get("name");
                        clientMessage   = (String)    ((JSONObject) jsonParser.parse(message)).get("message");
                        receiver        = (String)    ((JSONObject) jsonParser.parse(message)).get("receiver");
                        JSONArray users = (JSONArray) ((JSONObject) jsonParser.parse(message)).get("users");
                        clientUsernameList = new ArrayList<String>();
                        clientUsernameList.add("Common chat");
                        for(int i = 0; i < users.size(); ++i) {
                            clientUsernameList.add((String) users.get(i));
                        }
                    } catch(Exception e) {
                        System.out.println(e);
                    }

                    if(receiver.equals("everyone")) {
                        messagesJTextArea.append("\n" + clientUsername + ": " + clientMessage);
                    } else {
                        if (receiver.equals(currentClientUsername)) {
                            for(int index = 0; index < clientUsernameList.size(); ++index) {
                                if(clientUsernameList.get(index).equals(clientUsername)) {
                                    privateClientChatRoomList.get(index).append("From " + clientUsername + ": " + clientMessage + "\n");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void checkAndSendAuthorizationFields() {
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
                    currentClientUsername = usernameJTextField.getText();
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

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            br     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw     = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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

                if(((JSONObject) jsonParser.parse(serverSays)).get("status").equals("notAuthorized")) {
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
                            checkAndSendAuthorizationFields();
                        }
                    });

                    signupJButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                bw.write("signup");
                                bw.write("\n");
                                bw.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            checkAndSendAuthorizationFields();
                        }
                    });
                }

                if (((JSONObject) jsonParser.parse(serverSays)).get("status").equals("authorized")) {
                    System.out.println("authorized");
                    isAuthorized = true;

                    JSONArray users = (JSONArray) ((JSONObject) jsonParser.parse(serverSays)).get("users");
                    clientUsernameList = new ArrayList<String>();
                    clientUsernameList.add("Common chat");
                    for(int i = 0; i < users.size(); ++i) {
                        clientUsernameList.add((String) users.get(i));
                    }

                    closeAuthorization();
                    createAndShowGIU(clientUsernameList);

                    Thread newThread = new Thread(new ClientHandler());
                    newThread.start();
                }

                if(((JSONObject) jsonParser.parse(serverSays)).get("status").equals("incorrect")) {
                    System.out.println("not authorized");
                    isAuthorized = false;
                    createAndShowIncorrectAuthJLabel(authJFrame);
                    authJFrame.revalidate();
                }
            }
        } catch(IOException ioe) {
            System.out.println("Can't connect to the server " + host + ":" + port);
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
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

    public String generateClientAnswer(String receiver, String message) {
        JSONObject answer = new JSONObject();
        answer.put("receiver", receiver);
        answer.put("message", message);

        return answer.toString();
    }

    public void runClient() {
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
                        clientMessageJTextField.setText("Your need to enter the message!");
                    } else {
                        if(currentReceiver == 0) {
                            sendMessageToServer(generateClientAnswer("everyone", message));
                            clientMessageJTextField.setText("Enter your message...");
                        } else {
                            privateClientChatRoomList.get(currentReceiver).append("From " + currentClientUsername + ": " + message + "\n");
                            sendMessageToServer(generateClientAnswer(clientUsernameList.get(currentReceiver), message));
                            clientMessageJTextField.setText("Enter your message...");
                        }
                    }
                }
            }
        });
    }

    public static void updateMainJFrame() {
        final DefaultListModel defaultListModel = new DefaultListModel();
        for(int i = 0; i < clientUsernameList.size(); ++i) {
            defaultListModel.addElement(clientUsernameList.get(i));
        }
        setContactsJList(defaultListModel);
        addPrivateClientChatRooms(clientUsernameList);
    }

    public static void setTimerOnUpdateMainJFrame() {
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainJFrame();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public static void main(String []args) {
        Client client = new Client(defaultHost, defaultPort);
        client.runClient();

        updateMainJFrame();
        setTimerOnUpdateMainJFrame();
    }
}
