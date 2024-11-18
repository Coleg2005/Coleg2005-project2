import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    // initializes fields
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    // constructor
    public Client(String host, int port) {

        // tries to set socket, out, and in
        try {
            socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            // catch
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    // returns the socket
    public Socket getSocket() {
        return socket;
    }

    public void handshake() {
        // writes out the code to get accepted
        try {
            out.writeBytes("12345\n");
            
            // flushes out
            out.flush();

        } catch (Exception e) {
            // catch
            System.err.println("Error during handshake: " + e.getMessage());
        }
    }

    public String request(String message) {

        // tries to request the server to get the number of factirs
        try {
            // writes out the integer as a string
            out.writeBytes(message + "\n");
            // flushes out
            out.flush();
            // takes in response from server
            String response = in.readLine();
            // returns response
            return response;
        } catch (Exception e) {
            // catches error and returns null
            System.err.println("Error sending request: " + e.getMessage());
            return null;
        }
    }

    // disconnects from server
    public void disconnect() {
        // tries to closes everything if it exists
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            // catch and error message
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
