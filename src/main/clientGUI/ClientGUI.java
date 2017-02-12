package main.clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vladislav on 2/11/17.
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

    public static String getDefaultHost() {
        return DefaultHost;
    }

    public static int getDefaultPort() {
        return DefaultPort;
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

    public static void addMessageJTextArea(Container pane) {
        messagesJTextArea = new JTextArea();

        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 300;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

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
        addMessageJTextArea(pane);
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

    public static void main(String []args) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGIU();
                }
            });
    }
}
