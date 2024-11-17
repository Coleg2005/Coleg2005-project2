import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Server {

    private ServerSocket server = null;
    private ArrayList<LocalDateTime> connectionTimes = new ArrayList<>();

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }

    public void serve(int clients) {
        System.out.println("Waiting for clients...");
        for (int i = 0; i < clients; i++) {
            try {
                Socket clientSocket = server.accept();
                connectionTimes.add(LocalDateTime.now());
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                // Handle client in a separate thread
                new ClientHandler(clientSocket).start();
            } catch (Exception e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        try {
            if (server != null) {
                server.close();
                System.out.println("Server shut down.");
            }
        } catch (Exception e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        return connectionTimes;
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                String message;
                while ((message = in.readLine()) != null) {

                    System.out.println("This is the message: " + message);

                    int input = (int)message.toCharArray()[0];

                    int ans = factor(input);
                    System.out.println("Received: " + message);


                    
                    out.println("The number " + input + " has " + ans + " factors"); // Echo the message back to the client
                }
            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
                } catch (Exception e) {
                    System.err.println("Error closing client connection: " + e.getMessage());
                }
            }
        }
    }

    public int factor(int input) {

        int count = 0;

        for(int i = 1; i < input/2; i++) {
            if(input % i == 0) {

                if(i != input / i) {
                    count++;
                }
                count++;
            }
        }

        return count;
    }
}
