import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server");
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void handshake() {
        try {
            out.writeBytes("12345\n");
            
            out.flush();
            System.out.println("Handshake sent.");
            System.out.println("Before read in");
            socket.setSoTimeout(1000);
            System.out.println("Server response: " + in.readLine());
            System.out.println("After read in");

        } catch (Exception e) {
            System.out.println("In error");

            System.err.println("Error during handshake: " + e.getMessage());
        }
    }

    public String request(String message) {
        try {
            out.writeBytes(message + "\n");
            out.flush();
            socket.setSoTimeout(1000);
            String response = in.readLine();
            System.out.println("Request sent: " + message);
            System.out.println("Server response: " + response);
            return response;
        } catch (Exception e) {
            System.err.println("Error sending request: " + e.getMessage());
            return null;
        }
    }

    public void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            System.out.println("Disconnected from server");
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
