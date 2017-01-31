package test;

import main.client.Client;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by vladislav on 1/26/17.
 */
class ClientTest {
    /*@org.junit.Test
    public void closeSocketConnection() throws Exception {

    }

    @org.junit.Test
    public void runClient() throws Exception {

    }

    @org.junit.Test
    public void main() throws Exception {

    }*/

    /*@BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void setDownStreams() {
        System.setOut(null);
        System.setErr(null);
    }*/


    @DisplayName("First test")
    @Test
    void testMethod1() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.out.println("this is the first test");
        //System.setOut(ps);

        Client client = new Client("localhost", 8181);
        System.out.println(os.toString());
        //assertEquals("Connected to the erver localhost on port 8181", outContent.toString());
    }

   /* @org.junit.jupiter.api.Test
    void closeSocketConnection() {

    }

    @org.junit.jupiter.api.Test
    void runClient() {

    }

    @org.junit.jupiter.api.Test
    void main() {

    }*/

}