package main.clientGUI;

import main.client.Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Client side console class.
 * @author Vladislav Klochkov
 */

public class ClientGUI extends JFrame {
    private final  static String     DefaultHost        = "localhost";
    private final  static int        DefaultPort        = 8181;

    public static JFrame             mainJFrame              = null;
    public static JLabel             hostJLabel              = null;
    public static JTextField         hostJTextField          = null;
    public static JLabel             portJLabel              = null;
    public static JTextField         portJTextField          = null;
    public static JButton            disconnectJButton       = null;
    public static JTextArea          messagesJTextArea       = null;
    public static JLabel             clientMessageJLabel     = null;
    public static JTextField         clientMessageJTextField = null;
    public static JButton            sendJButton             = null;
    public static JList              contactsJList           = null;
    public static DefaultListModel   defaultListModel        = null;
    public static GridBagConstraints gridBagConstraints      = null;
    public static JScrollPane        jScrollPane             = null;
    public static Border             border                  = null;

    public static JFrame             authJFrame              = null;
    public static JLabel             authJLabel              = null;
    public static JLabel             usernameJLabel          = null;
    public static JLabel             passwordJLabel          = null;
    public static JTextField         usernameJTextField      = null;
    public static JPasswordField     passwordJPasswordField  = null;
    public static JPanel             authJPanel              = null;
    public static JButton            loginJButton            = null;
    public static JButton            signupJButton           = null;
    public static JLabel             incorrectJLabel         = null;
    public static JLabel             loginInvalidJLabel      = null;
    public static JLabel             passwordInvalidJLabel   = null;

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
        hostJLabel = new JLabel("You are connected to : " + getDefaultHost());

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
        gridBagConstraints.ipady   = 5;

        pane.add(disconnectJButton, gridBagConstraints);
    }

    public static void addMessagesJTextArea(Container pane) {
        messagesJTextArea = new JTextArea(20, 0);
        jScrollPane = new JScrollPane(messagesJTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagesJTextArea.setEditable(false);

        border = BorderFactory.createLineBorder(Color.GRAY);
        messagesJTextArea.setBorder(border);

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 10, 5, 15);
        gridBagConstraints.gridx     = 2;
        gridBagConstraints.gridy     = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;

        pane.add(messagesJTextArea, gridBagConstraints);
        pane.add(jScrollPane, gridBagConstraints);
    }

    public static void addClientMessageJLabel(Container pane) {
        clientMessageJLabel = new JLabel("Your message:");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 10, 5, 10);
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

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(5, 10, 10, 10);
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

    public static void addContactsJList(Container pane, ArrayList<String> clientUsernameList) {
        defaultListModel = new DefaultListModel();
        for(String username: clientUsernameList) {
            defaultListModel.addElement(username);
        }

        contactsJList = new JList(defaultListModel);
        contactsJList.setFixedCellHeight(30);
        contactsJList.setFixedCellWidth(250);

        jScrollPane = new JScrollPane(contactsJList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        border = BorderFactory.createLineBorder(Color.GRAY);
        contactsJList.setBorder(border);

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.insets    = new Insets(10, 30, 5, 30);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 1;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill      = GridBagConstraints.VERTICAL;

        pane.add(contactsJList, gridBagConstraints);
        pane.add(jScrollPane, gridBagConstraints);
    }

    public static void setContactsJList(DefaultListModel defaultListModel) {
        contactsJList.removeAll();
        contactsJList.setModel(defaultListModel);
    }

    public static void createAndShowGIU(ArrayList<String> clientUsernameList) {
        mainJFrame = new JFrame("ClientGUI");
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
    }

    public static void addAuthJLabel(Container pane) {
        authJLabel = new JLabel("Please, login or signup");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
//        gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
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
        gridBagConstraints.gridy  = 3;
        gridBagConstraints.insets = new Insets(10, 10, 0, 10);
        pane.add(passwordJLabel, gridBagConstraints);

        gridBagConstraints.gridx  = 0;
        gridBagConstraints.gridy  = 4;
        gridBagConstraints.insets = new Insets(0, 10, 10, 10);
        pane.add(passwordJPasswordField, gridBagConstraints);
    }

    public static void createAndShowIncorrectAuthJLabel(Container pane) {
        incorrectJLabel = new JLabel("Incorrect username or password.");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 6;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(incorrectJLabel, gridBagConstraints);
    }

    public static void addAuthJButtons(Container pane) {
        loginJButton = new JButton("Login");

        gridBagConstraints           = new GridBagConstraints();
        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 7;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(loginJButton, gridBagConstraints);

        signupJButton = new JButton("Signup");

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 1;
        gridBagConstraints.gridy  = 7;
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

//        authJFrame.pack();
//        authJFrame.setSize(400, 250);
        authJFrame.setResizable(false);
        authJFrame.setMinimumSize(new Dimension(400, 260));

//        Dimension dimension = new Dimension(, authJFrame.getHeight());

//        Toolkit toolkit =

        authJFrame.setLocationRelativeTo(null);
        authJFrame.setVisible(true);
    }

    public static void closeAuthorization() {
        authJFrame.setVisible(false);
    }
}
