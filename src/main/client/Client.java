package main.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by vladislav on 1/26/17.
 */

public class Client {
    public static void main(String []args) {
        final String HOST_NAME = "localhost";
        final int    PORT      = 8181;

        try {
            Socket socket = new Socket(HOST_NAME, PORT);
            System.out.println("Connected to the sever " + HOST_NAME + ":" + PORT);

            PrintStream ps = new PrintStream(socket.getOutputStream());
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            BufferedReader userBF = new BufferedReader(
                    new InputStreamReader(
                            System.in));
            String userMessage;
            while((userMessage = userBF.readLine()) != null) {
                ps.println(userMessage);
                System.out.println("Server:" + bf.readLine());
            }
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
