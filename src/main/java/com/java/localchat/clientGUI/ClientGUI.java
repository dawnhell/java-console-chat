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
    public static JTextArea            messagesJTextArea         = null;
    public static JTextField           clientMessageJTextField   = null;
    public static JButton              sendJButton               = null;
    public static JList                contactsJList             = null;
    public static JScrollPane          contactsJScrollPane       = null;
    public static DefaultListModel     defaultListModel          = null;
    public static GridBagConstraints   gridBagConstraints        = null;
    public static JScrollPane          messagesJScrollPane       = null;
    public static ArrayList<JTextArea> privateClientChatRoomList = new ArrayList<JTextArea>();
    public static int                  currentReceiver           = 0;

    public static JFrame               authJFrame                = null;
    public static JTextField           usernameJTextField        = null;
    public static JPasswordField       passwordJPasswordField    = null;
    public static JButton              loginJButton              = null;
    public static JButton              signupJButton             = null;
    public static JLabel               incorrectJLabel           = null;
    public static JLabel               existsJLabel              = null;
    public static JLabel               loginInvalidJLabel        = null;
    public static JLabel               passwordInvalidJLabel     = null;

    public static JFrame               serverDisabledJFrame      = null;

    public static String getDefaultHost() {
        return DefaultHost;
    }

    public static int getDefaultPort() {
        return DefaultPort;
    }

    public static void addWelcomeJLabel(Container pane, String clientUsername) {
        JPanel jPanel = new JPanel(new GridBagLayout());

        JLabel hostJLabel = new JLabel("Welcome, " + clientUsername + "! You are connected to " + getDefaultHost() + ":" + getDefaultPort());
        hostJLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        jPanel.add(hostJLabel, gridBagConstraints);

        pane.add(jPanel, BorderLayout.PAGE_START);
    }

    public static void addMessagesJTextArea(Container pane) {
        messagesJTextArea = new JTextArea();
        messagesJTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messagesJTextArea.setColumns(30);
        messagesJTextArea.setRows(20);
        messagesJTextArea.setLineWrap(true);
        messagesJTextArea.setWrapStyleWord(true);
        messagesJTextArea.setEditable(false);
        messagesJScrollPane = new JScrollPane(messagesJTextArea);

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 10, 5, 15);
        gridBagConstraints.gridx     = 1;
        gridBagConstraints.gridy     = 0;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(messagesJScrollPane, gridBagConstraints);
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
                        if(pane.getComponentCount() == 3) {
                            pane.remove(2);
                        }
                        messagesJScrollPane.setVisible(true);
                    } else {
                        if (!contactsJList.getModel().getElementAt(index).equals(usernameJTextField.getText())) {
                            if(pane.getComponentCount() == 3) {
                                pane.remove(2);
                            }
                            messagesJScrollPane.setVisible(false);

                            gridBagConstraints           = new GridBagConstraints();
                            gridBagConstraints.insets    = new Insets(10, 10, 5, 15);
                            gridBagConstraints.gridx     = 1;
                            gridBagConstraints.gridy     = 0;
                            gridBagConstraints.ipadx     = 5;
                            gridBagConstraints.ipady     = 5;
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
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 0;
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
        if(privateClientChatRoomList.size() < clientUsernameList.size()) {
            for(int i = privateClientChatRoomList.size(); i < clientUsernameList.size(); ++i) {
                JTextArea privateJTextArea = new JTextArea();
                privateJTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
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

    public static void addMessagesJTextAreaAndContactsJList(Container pane, ArrayList<String> clientUsernameList) {
        JPanel jPanel = new JPanel(new GridBagLayout());

        addMessagesJTextArea(jPanel);
        addContactsJList(jPanel, clientUsernameList);

        pane.add(jPanel, BorderLayout.CENTER);
    }

    public static void addClientMessageJTextField(Container pane) {
        clientMessageJTextField = new JTextField();
        clientMessageJTextField.setText("Enter your message...");
        clientMessageJTextField.setSize(1, 20);
        clientMessageJTextField.setColumns(25);
        clientMessageJTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clientMessageJTextField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(5, 250, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 0;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 10;

        pane.add(clientMessageJTextField, gridBagConstraints);
    }

    public static void addSendJButton(Container pane) {
        sendJButton = new JButton("Send");

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 10, 5, 10);
        gridBagConstraints.gridx  = 1;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.ipadx  = 25;
        gridBagConstraints.ipady  = 5;

        pane.add(sendJButton, gridBagConstraints);
    }

    public static void addClientMessageJPanel(Container pane) {
        JPanel jPanel = new JPanel(new GridBagLayout());

        addClientMessageJTextField(jPanel);
        addSendJButton(jPanel);

        pane.add(jPanel, BorderLayout.PAGE_END);
    }

    public static void createAndShowGIU(ArrayList<String> clientUsernameList, String clientUsername) {
        currentReceiver = 0;

        mainJFrame = new JFrame("Chat");
        mainJFrame.setDefaultCloseOperation(mainJFrame.EXIT_ON_CLOSE);

        addWelcomeJLabel(mainJFrame.getContentPane(), clientUsername);
        addMessagesJTextAreaAndContactsJList(mainJFrame.getContentPane(), clientUsernameList);
        addClientMessageJPanel(mainJFrame.getContentPane());

        mainJFrame.pack();
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setResizable(false);
        mainJFrame.setVisible(true);
    }

    public static void addAuthJLabel(Container pane) {
        JPanel jPanel      = new JPanel(new GridBagLayout());
        jPanel.setBackground(new Color(0xFAFAFA));

        JLabel authJLabel  = new JLabel("Please, login or signup");
        authJLabel.setFont  (new Font("Helvetica", Font.BOLD, 14));

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 0;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;
        gridBagConstraints.insets    = new Insets(10, 10, 0, 10);
        gridBagConstraints.gridwidth = 2;

        jPanel.add(authJLabel, gridBagConstraints);
        pane.add(jPanel, BorderLayout.PAGE_START);
    }

    public static void addLoginInvalidJLabel(Container pane) {
        loginInvalidJLabel        = new JLabel("Enter login!");
        loginInvalidJLabel.setVisible(false);
        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(0, 10, 10, 0);
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(loginInvalidJLabel, gridBagConstraints);
    }

    public static void addPasswordInvalidJLabel(Container pane) {
        passwordInvalidJLabel     = new JLabel("Enter password!");
        passwordInvalidJLabel.setVisible(false);
        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(0, 10, 10, 0);
        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(passwordInvalidJLabel, gridBagConstraints);
    }

    public static void addIncorrectJLabel(Container pane) {
        incorrectJLabel              = new JLabel("Incorrect username or password.");
        incorrectJLabel.setVisible(false);
        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets    = new Insets(10, 0, 10, 0);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 4;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(incorrectJLabel, gridBagConstraints);
    }

    public static void addExistsJLabel(Container pane) {
        existsJLabel                 = new JLabel("User with such name already exists.");
        existsJLabel.setVisible(false);
        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets    = new Insets(10, 0, 10, 0);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 4;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(existsJLabel, gridBagConstraints);
    }

    public static void addAuthInputFields(Container pane) {
        JPanel jPanel      = new JPanel(new GridBagLayout());
        jPanel.setBackground(new Color(0xFAFAFA));

        JLabel usernameJLabel     = new JLabel("Username:");
        JLabel passwordJLabel     = new JLabel("Password:");
        usernameJTextField        = new JTextField(15);
        passwordJPasswordField    = new JPasswordField(15);

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 0;
        gridBagConstraints.insets = new Insets(10, 30, 0, 30);
        jPanel.add(usernameJLabel, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 1;
        gridBagConstraints.insets = new Insets(0, 30, 5, 30);
        jPanel.add(usernameJTextField, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 2;
        gridBagConstraints.insets = new Insets(5, 30, 0, 30);
        jPanel.add(passwordJLabel, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 3;
        gridBagConstraints.insets = new Insets(0, 30, 10, 30);
        jPanel.add(passwordJPasswordField, gridBagConstraints);

        addLoginInvalidJLabel   (jPanel);
        addPasswordInvalidJLabel(jPanel);
        addIncorrectJLabel      (jPanel);
        addExistsJLabel         (jPanel);

        pane.add(jPanel, BorderLayout.CENTER);
    }

    public static void showLoginInvalidJLabel(Container pane) {
        passwordInvalidJLabel.setVisible(false);
        incorrectJLabel      .setVisible(false);
        existsJLabel         .setVisible(false);
        loginInvalidJLabel   .setVisible(true);
        pane.revalidate();
    }

    public static void showPasswordInvalidJLabel(Container pane) {
        loginInvalidJLabel   .setVisible(false);
        incorrectJLabel      .setVisible(false);
        existsJLabel         .setVisible(false);
        passwordInvalidJLabel.setVisible(true);
        pane.revalidate();
    }

    public static void showIncorrectJLabel(Container pane) {
        loginInvalidJLabel   .setVisible(false);
        passwordInvalidJLabel.setVisible(false);
        existsJLabel         .setVisible(false);
        incorrectJLabel      .setVisible(true);
        pane.revalidate();
    }

    public static void showExistsJLabel(Container pane) {
        loginInvalidJLabel   .setVisible(false);
        passwordInvalidJLabel.setVisible(false);
        incorrectJLabel      .setVisible(false);
        existsJLabel         .setVisible(true);
        pane.revalidate();
    }

    public static void hideLoginInvalidJLabel(Container pane) {
        loginInvalidJLabel.setVisible(false);
        pane.revalidate();
    }

    public static void hidePasswordInvalidJLabel(Container pane) {
        passwordInvalidJLabel.setVisible(false);
        pane.revalidate();
    }

    public static void addAuthJButtons(Container pane) {
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBackground(new Color(0xFAFAFA));

        loginJButton  = new JButton("Login");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new Insets(20, 20, 20, 10);

        jPanel.add(loginJButton, gridBagConstraints);

        signupJButton = new JButton("Signup");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new Insets(20, 10, 20, 20);

        jPanel.add(signupJButton, gridBagConstraints);

        pane.add(jPanel, BorderLayout.PAGE_END);
    }

    public static void createAndShowAuthorization() {
        authJFrame = new JFrame("Authorization");
        authJFrame.getContentPane().setBackground(new Color(0xFAFAFA));
        authJFrame.setDefaultCloseOperation(authJFrame.EXIT_ON_CLOSE);

        addAuthJLabel     (authJFrame.getContentPane());
        addAuthInputFields(authJFrame.getContentPane());
        addAuthJButtons   (authJFrame.getContentPane());

        authJFrame.setResizable(false);
        authJFrame.setMinimumSize(new Dimension(360, 260));
        authJFrame.setLocationRelativeTo(null);
        authJFrame.setVisible(true);
    }

    public static void closeAuthorization() {
        authJFrame.setVisible(false);
    }

    public static void createAndShowServerDisabledJFrame() {
        serverDisabledJFrame = new JFrame("Server disabled");
        serverDisabledJFrame.getContentPane().setBackground(new Color(0xFAFAFA));
        serverDisabledJFrame.setDefaultCloseOperation(serverDisabledJFrame.EXIT_ON_CLOSE);
        serverDisabledJFrame.setLayout(new GridBagLayout());

        JLabel jLabel = new JLabel("Server is not available now.");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        serverDisabledJFrame.getContentPane().add(jLabel, gridBagConstraints);

        JButton jButton = new JButton("Ok");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverDisabledJFrame.dispose();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        serverDisabledJFrame.getContentPane().add(jButton, gridBagConstraints);


        serverDisabledJFrame.setResizable(false);
        serverDisabledJFrame.setMinimumSize(new Dimension(250, 150));
        serverDisabledJFrame.setLocationRelativeTo(null);
        serverDisabledJFrame.setVisible(true);
    }
}
