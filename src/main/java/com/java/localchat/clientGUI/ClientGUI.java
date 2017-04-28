package com.java.localchat.clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Client side console class.
 * @author Vladislav Klochkov
 */

public class ClientGUI extends JFrame {
    private final  static String       DefaultHost               = "localhost";
    private final  static int          DefaultPort               = 8181;

    public static JFrame               mainJFrame                = null;
    public static JLabel               hostJLabel                = null;
    public static JTextField           hostJTextField            = null;
    public static JLabel               portJLabel                = null;
    public static JTextField           portJTextField            = null;
    public static JButton              disconnectJButton         = null;
    public static JTextArea            messagesJTextArea         = null;
    public static JLabel               clientMessageJLabel       = null;
    public static JTextField           clientMessageJTextField   = null;
    public static JButton              sendJButton               = null;
    public static JList                contactsJList             = null;
    public static JScrollPane          contactsJScrollPane       = null;
    public static DefaultListModel     defaultListModel          = null;
    public static GridBagConstraints gridBagConstraints        = null;
    public static JScrollPane          messagesJScrollPane       = null;
    public static ArrayList<JTextArea> privateClientChatRoomList = new ArrayList<JTextArea>();
    public static int                  currentReceiver           = 0;

    public static JFrame               authJFrame                = null;
    public static JLabel               authJLabel                = null;
    public static JLabel               usernameJLabel            = null;
    public static JLabel               passwordJLabel            = null;
    public static JTextField           usernameJTextField        = null;
    public static JPasswordField       passwordJPasswordField    = null;
    public static JPanel               authJPanel                = null;
    public static JButton              loginJButton              = null;
    public static JButton              signupJButton             = null;
    public static JLabel               incorrectJLabel           = null;
    public static JLabel               loginInvalidJLabel        = null;
    public static JLabel               passwordInvalidJLabel     = null;

    public static String getDefaultHost() {
        return DefaultHost;
    }

    public static int getDefaultPort() {
        return DefaultPort;
    }

    public static String getCurrentHost() {
        return hostJTextField.getText();
    }

    public static int getCurrentPort() {
        return Integer.parseInt(portJTextField.getText());
    }

    public static void addHostJLabel(Container pane) {
        hostJLabel = new JLabel("You are connected to " + getDefaultHost() + ":" + getDefaultPort());

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 1;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(hostJLabel, gridBagConstraints);
    }

    public static void addPortJLabel(Container pane) {
        portJLabel = new JLabel("On port: " + getDefaultPort());

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 2;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(portJLabel, gridBagConstraints);
    }

    public static void addDisconnectionJButtons(Container pane) {
        disconnectJButton = new JButton("Disconnect");
        disconnectJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                connectJButton.setEnabled(true);
//                disconnectJButton.setEnabled(false);
            }
        });

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 4;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(disconnectJButton, gridBagConstraints);
    }

    public static void addMessagesJTextArea(Container pane) {
        messagesJTextArea = new JTextArea();
//        messagesJTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messagesJTextArea.setColumns(30);
        messagesJTextArea.setRows(20);
        messagesJTextArea.setLineWrap(true);
        messagesJTextArea.setWrapStyleWord(true);
        messagesJTextArea.setEditable(false);
        messagesJScrollPane = new JScrollPane(messagesJTextArea);

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 10, 5, 15);
        gridBagConstraints.gridx     = 2;
        gridBagConstraints.gridy     = 1;
        gridBagConstraints.gridwidth = 6;

        pane.add(messagesJScrollPane, gridBagConstraints);
    }

    public static void addClientMessageJLabel(Container pane) {
        clientMessageJLabel = new JLabel("Your message:");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 30, 5, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 2;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;

        pane.add(clientMessageJLabel, gridBagConstraints);
    }

    public static void addClientMessageJTextField(Container pane) {
        clientMessageJTextField = new JTextField();
        clientMessageJTextField.setText("Enter your message...");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(5, 30, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 3;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;

        pane.add(clientMessageJTextField, gridBagConstraints);
    }

    public static void addSendJButton(Container pane) {
        sendJButton = new JButton("Send");

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 10, 5, 10);
        gridBagConstraints.gridx  = 5;
        gridBagConstraints.gridy  = 3;
        gridBagConstraints.ipadx  = 25;
        gridBagConstraints.ipady  = 5;

        pane.add(sendJButton, gridBagConstraints);
    }

    public static void addContactsJList(final Container pane, ArrayList<String> clientUsernameList) {
        System.out.println(clientUsernameList);

        defaultListModel = new DefaultListModel();
        for(String username: clientUsernameList) {
            defaultListModel.addElement(username);
        }

        contactsJList = new JList(defaultListModel);
        contactsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsJList.setVisibleRowCount(-1);
        contactsJList.setFixedCellHeight(30);
        contactsJList.setFixedCellWidth(250);

        contactsJScrollPane = new JScrollPane(contactsJList);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int index = contactsJList.locationToIndex(e.getPoint());
                    currentReceiver = index;

                    if(index == 0) {
                        if(pane.getComponentCount() > 8) {
                            pane.getComponent(pane.getComponentCount() - 1).setVisible(false);
                        }
                        messagesJScrollPane.setVisible(true);
                    } else {
                        if (!contactsJList.getModel().getElementAt(index).equals(usernameJTextField.getText())) {
                            if(pane.getComponentCount() > 8) {
                                pane.getComponent(pane.getComponentCount() - 1).setVisible(false);
                            }
                            messagesJScrollPane.setVisible(false);

                            gridBagConstraints           = new GridBagConstraints();
                            gridBagConstraints.insets    = new Insets(10, 10, 5, 15);
                            gridBagConstraints.gridx     = 2;
                            gridBagConstraints.gridy     = 1;
                            gridBagConstraints.gridwidth = 6;
                            pane.add(new JScrollPane(privateClientChatRoomList.get(index)), gridBagConstraints);
                            pane.revalidate();
                        }
                    }
                }
            }
        };

        contactsJList.addMouseListener(mouseListener);
        contactsJList.setFixedCellWidth(180);

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 30, 5, 30);
        gridBagConstraints.gridx     = 1;
        gridBagConstraints.gridy     = 1;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill      = GridBagConstraints.VERTICAL;

        pane.add(contactsJScrollPane, gridBagConstraints);
    }

    public static void setContactsJList(DefaultListModel defaultListModel) {
        contactsJList.removeAll();
        contactsJList.setModel(defaultListModel);
    }

    public static void addPrivateClientChatRooms(ArrayList<String> clientUsernameList) {
        Container pane = mainJFrame.getContentPane();

        if(privateClientChatRoomList.size() < clientUsernameList.size()) {
            for(int i = privateClientChatRoomList.size(); i < clientUsernameList.size(); ++i) {
                JTextArea privateJTextArea = new JTextArea();
                privateJTextArea.setColumns(30);
                privateJTextArea.setRows(20);
                privateJTextArea.setLineWrap(true);
                privateJTextArea.setWrapStyleWord(true);
                privateJTextArea.setEditable(false);
                privateJTextArea.append("You are writing to " + clientUsernameList.get(i) + "\n");
                privateClientChatRoomList.add(privateJTextArea);
            }
        }
    }

    public static void createAndShowGIU(ArrayList<String> clientUsernameList) {
        currentReceiver = 0;

        mainJFrame = new JFrame("Chat");
        mainJFrame.setDefaultCloseOperation(mainJFrame.EXIT_ON_CLOSE);
        Container pane = mainJFrame.getContentPane();
        pane.setLayout(new GridBagLayout());

        addHostJLabel(pane);
        addPortJLabel(pane);
        addDisconnectionJButtons(pane);
        addMessagesJTextArea(pane);
        addClientMessageJLabel(pane);
        addClientMessageJTextField(pane);
        addSendJButton(pane);
        addContactsJList(pane, clientUsernameList);

        mainJFrame.pack();
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setVisible(true);
        mainJFrame.setResizable(false);
    }

    public static void addAuthJLabel(Container pane) {
        authJLabel = new JLabel("Please, login or signup");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 0;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;
        gridBagConstraints.gridwidth = 2;

        pane.add(authJLabel, gridBagConstraints);
    }

    public static void addAuthJPanel(Container pane) {
        authJPanel             = new JPanel(new GridBagLayout());
        usernameJLabel         = new JLabel("Username:");
        passwordJLabel         = new JLabel("Password:");
        usernameJTextField     = new JTextField(15);
        passwordJPasswordField = new JPasswordField(15);

        gridBagConstraints.anchor    = GridBagConstraints.WEST;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 1;
        gridBagConstraints.insets = new Insets(10, 10, 0, 10);
        pane.add(usernameJLabel, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 2;
        gridBagConstraints.insets = new Insets(0, 10, 10, 10);
        pane.add(usernameJTextField, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.insets = new Insets(10, 10, 0, 10);
        pane.add(passwordJLabel, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 5;
        gridBagConstraints.insets = new Insets(0, 10, 10, 10);
        pane.add(passwordJPasswordField, gridBagConstraints);
    }

    public static void createAndShowIncorrectLoginJLabel(Container pane) {
        if(loginInvalidJLabel == null) {
            loginInvalidJLabel = new JLabel("Enter login!");
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.insets = new Insets(0, 10, 10, 0);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.ipadx = 5;
            gridBagConstraints.ipady = 5;

            authJFrame.add(loginInvalidJLabel, gridBagConstraints);
            authJFrame.revalidate();
        }
    }

    public static void createAndShowIncorrectPasswordJLabel(Container pane) {
        if(passwordInvalidJLabel == null) {
            passwordInvalidJLabel = new JLabel("Enter password!");
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.insets = new Insets(0, 10, 10, 0);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 6;
            gridBagConstraints.ipadx = 5;
            gridBagConstraints.ipady = 5;

            authJFrame.add(passwordInvalidJLabel, gridBagConstraints);
            authJFrame.revalidate();
        }
    }

    public static void createAndShowIncorrectAuthJLabel(Container pane) {
        if(incorrectJLabel == null) {
            incorrectJLabel = new JLabel("Incorrect username or password.");

            gridBagConstraints           = new GridBagConstraints();
            gridBagConstraints.anchor    = GridBagConstraints.CENTER;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
            gridBagConstraints.gridx     = 0;
            gridBagConstraints.gridy     = 7;
            gridBagConstraints.ipadx     = 5;
            gridBagConstraints.ipady     = 5;

            pane.add(incorrectJLabel, gridBagConstraints);
        }
    }

    public static void addAuthJButtons(Container pane) {


        loginJButton = new JButton("Login");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 8;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(loginJButton, gridBagConstraints);

        signupJButton = new JButton("Signup");

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 1;
        gridBagConstraints.gridy  = 8;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(signupJButton, gridBagConstraints);
    }

    public static void createAndShowAuthorization() {
        authJFrame = new JFrame("Authorization");
        authJFrame.setDefaultCloseOperation(authJFrame.EXIT_ON_CLOSE);
        authJFrame.setLayout(new GridBagLayout());

        addAuthJLabel(authJFrame);
        addAuthJPanel(authJFrame);
        addAuthJButtons(authJFrame);

        authJFrame.setResizable(false);
        authJFrame.setMinimumSize(new Dimension(400, 260));
        authJFrame.setLocationRelativeTo(null);
        authJFrame.setVisible(true);
        authJFrame.setResizable(false);
    }

    public static void closeAuthorization() {
        authJFrame.setVisible(false);
    }
}

