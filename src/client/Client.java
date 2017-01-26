package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

/**
 * Created by vladislav on 1/26/17.
 */

public class Client {
    private final String HOST_NAME = "localhost";
    private final int    PORT      = 8181;

    public static void main(String []args) {
        try {
            Socket socket = new Socket("localhost", 8181);//this can't be reffered from static context
            System.out.println("Connected to the sever " + "localhost" + ":" + "8181");

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
                System.out.println("Server:");
                System.out.println(bf.readLine());
            }
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
