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
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }

    public void serve(int clients) {
        for (int i = 0; i < clients; i++) {
            try {
                Socket clientSocket = server.accept();
                connectionTimes.add(LocalDateTime.now());

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

                String code = in.readLine();
                if (code == null || !code.equals("12345")) {
                    // If the code is invalid, disconnect the client
                    out.println("couldn't handshake");
                    clientSocket.close();
                    return;
                }

                String message;
                while ((message = in.readLine()) != null) {

                    if (message.length() > 10) {
                        out.println("There was an exception on the server");
                    }

                    int input = Integer.parseInt(message);

                    int ans = factor(input);
                    
                    out.println("The number " + input + " has " + ans + " factors"); 
                }
            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println("Error closing client connection: " + e.getMessage());
                }
            }
        }
    }

    public int factor(int input) {
        int count = 0;

        for(int i = 1; i <= input ; i++) {
            if(input % i == 0) {
                count++;
            }
        }

        return count;
    }
}
