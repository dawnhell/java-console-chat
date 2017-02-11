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

    public static String getDefaultHost() {
        return DefaultHost;
    }

    public static int getDefaultPort() {
        return DefaultPort;
    }

    public static void main(String []args) {
        JFrame jFrame = new JFrame("ClientGUI");
        jFrame.setLayout(new FlowLayout());

        JLabel hostJLabel = new JLabel("Host: ");
        hostJLabel.setBounds(10, 10, 50, 30);

        JTextField hostJTextField = new JTextField(getDefaultHost());
        hostJTextField.setBounds(60, 10, 150, 30);

        JLabel portJLabel = new JLabel("Port: ");
        portJLabel.setBounds(230, 10, 50, 30);

        JTextField portJTextField = new JTextField(getDefaultPort() + "");
        portJTextField.setBounds(280, 10, 150, 30);

        JButton connectJButton = new JButton("Connect");
        connectJButton.setBounds(450, 10, 130, 30);
        connectJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectJButton.setText("Connecting...");
                connectJButton.setEnabled(false);
            }
        });

        JTextArea messagesJTextArea = new JTextArea();
        messagesJTextArea.setBounds(10, 70, 400, 300);

        JLabel clientMessageJLabel = new JLabel("Your message:");
        clientMessageJLabel.setBounds(10, 380, 200, 30);

        JTextArea clientMessageJTextArea = new JTextArea();
        clientMessageJTextArea.setBounds(10, 410, 310, 50);

        JButton sendJButton = new JButton("Send");
        sendJButton.setBounds(330, 420, 80, 30);


        jFrame.add(hostJLabel);
        jFrame.add(hostJTextField);
        jFrame.add(portJLabel);
        jFrame.add(portJTextField);
        jFrame.add(connectJButton);
        jFrame.add(messagesJTextArea);
        jFrame.add(clientMessageJLabel);
        jFrame.add(clientMessageJTextArea);
        jFrame.add(sendJButton);

        jFrame.setSize(600, 500);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
