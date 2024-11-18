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

        } catch (Exception e) {
            System.err.println("Error during handshake: " + e.getMessage());
        }
    }

    public String request(String message) {
        try {
            out.writeBytes(message + "\n");
            out.flush();
            String response = in.readLine();
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
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
