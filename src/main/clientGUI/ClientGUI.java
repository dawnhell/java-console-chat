package main.clientGUI;

import main.client.Client;
import org.json.simple.parser.ContentHandler;
import sun.java2d.xr.GrowableIntArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Client side console class.
 * @author Vladislav Klochkov
 */

public class ClientGUI extends JFrame {
    private final static String DefaultHost = "localhost";
    private final static int    DefaultPort = 8181;

    public static JLabel             hostJLabel              = null;
    public static JTextField         hostJTextField          = null;
    public static JLabel             portJLabel              = null;
    public static JTextField         portJTextField          = null;
    public static JButton            connectJButton          = null;
    public static JButton            disconnectJButton       = null;
    public static JTextArea          messagesJTextArea       = null;
    public static JLabel             clientMessageJLabel     = null;
    public static JTextField         clientMessageJTextField = null;
    public static JButton            sendJButton             = null;
    public static GridBagConstraints gridBagConstraints      = null;

    public static JLabel             authJLabel              = null;
    public static JLabel             usernameJLabel          = null;
    public static JLabel             passwordJLabel          = null;
    public static JTextField         usernameJTextField      = null;
    public static JPasswordField     passwordJPasswordField  = null;
    public static JPanel             authJPanel              = null;
    public static JButton            loginJButton            = null;
    public static JButton            signupJButton           = null;

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
        hostJLabel = new JLabel("Host: ");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;

        pane.add(hostJLabel, gridBagConstraints);
    }

    public static void addHostJTextField(Container pane) {
        hostJTextField = new JTextField(getDefaultHost());

        gridBagConstraints.insets = new Insets(10, 5, 10, 5);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 5;

        pane.add(hostJTextField, gridBagConstraints);
    }

    public static void addPortJLabel(Container pane) {
        portJLabel = new JLabel("Port: ");

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;

        pane.add(portJLabel, gridBagConstraints);
    }

    public static void addPortJTextField(Container pane) {
        portJTextField = new JTextField(getDefaultPort() + "");

        gridBagConstraints.insets = new Insets(10, 5, 10, 5);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 5;

        pane.add(portJTextField, gridBagConstraints);
    }

    public static void addConnectionJButtons(Container pane) {
        connectJButton = new JButton("Connect");
        disconnectJButton = new JButton("Disconnect");
        disconnectJButton.setEnabled(false);

        connectJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectJButton.setEnabled(false);
                disconnectJButton.setEnabled(true);

                System.out.println("trying to connect");

                Client client = new Client(hostJTextField.getText(), Integer.parseInt(portJTextField.getText()));
                client.runClient();
            }
        });
        disconnectJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectJButton.setEnabled(true);
                disconnectJButton.setEnabled(false);
            }
        });

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 5;

        pane.add(connectJButton, gridBagConstraints);

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;

        pane.add(disconnectJButton, gridBagConstraints);
    }

    public static void addMessagesJTextArea(Container pane) {
        messagesJTextArea = new JTextArea();
        messagesJTextArea.setEditable(false);

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 300;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

//        JScrollPane jScrollPane = new JScrollPane(messagesJTextArea);
//        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        Add scrollbar

        pane.add(messagesJTextArea, gridBagConstraints);
    }

    public static void addClientMessageJLabel(Container pane) {
        clientMessageJLabel = new JLabel("Your message:");

        gridBagConstraints.insets = new Insets(10, 10, 5, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        pane.add(clientMessageJLabel, gridBagConstraints);
    }

    public static void addClientMessageJTextField(Container pane) {
        clientMessageJTextField = new JTextField();

        gridBagConstraints.insets = new Insets(5, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        pane.add(clientMessageJTextField, gridBagConstraints);
    }

    public static void addSendJButton(Container pane) {
        sendJButton = new JButton("Send");

        gridBagConstraints.insets = new Insets(0, 10, 5, 10);
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 5;

        pane.add(sendJButton, gridBagConstraints);
    }

    public static void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());

        addHostJLabel(pane);
        addHostJTextField(pane);
        addPortJLabel(pane);
        addPortJTextField(pane);
        addConnectionJButtons(pane);
        addMessagesJTextArea(pane);
        addClientMessageJLabel(pane);
        addClientMessageJTextField(pane);
        addSendJButton(pane);
    }

    public static void createAndShowGIU() {
        JFrame jFrame = new JFrame("ClientGUI");
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        addComponentsToPane(jFrame.getContentPane());

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void addAuthJLabel(Container pane) {
        authJLabel = new JLabel("Please, login or signup");

        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
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

    public static void addAuthJButtons(Container pane) {
        loginJButton = new JButton("Login");

        gridBagConstraints.anchor    = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets    = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx     = 0;
        gridBagConstraints.gridy     = 5;
        gridBagConstraints.ipadx     = 5;
        gridBagConstraints.ipady     = 5;

        pane.add(loginJButton, gridBagConstraints);

        signupJButton = new JButton("Signup");

        gridBagConstraints        = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx  = 1;
        gridBagConstraints.gridy  = 5;
        gridBagConstraints.ipadx  = 5;
        gridBagConstraints.ipady  = 5;

        pane.add(signupJButton, gridBagConstraints);
    }

    public static void createAndShowAuthorization() {
        JFrame authJFrame = new JFrame("Authorization");
        authJFrame.setDefaultCloseOperation(authJFrame.EXIT_ON_CLOSE);
        authJFrame.setLayout(new GridBagLayout());

        addAuthJLabel(authJFrame);
        addAuthJPanel(authJFrame);
        addAuthJButtons(authJFrame);

        authJFrame.pack();
        authJFrame.setVisible(true);
    }

    public static void main(String []args) {
        createAndShowAuthorization();
    }
}
