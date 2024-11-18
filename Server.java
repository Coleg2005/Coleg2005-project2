// Imports
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Server {

    // initialize server and times arraylist
    private ServerSocket server = null;
    private ArrayList<LocalDateTime> connectionTimes = new ArrayList<>();

    // Server constructor
    public Server(int port) {

        // tries to set the server to the port given
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            // caught error and message
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }

    // serve method
    public void serve(int clients) {
        // accepts the number of clients given
        for (int i = 0; i < clients; i++) {
            // tries to accept each client socket and saves the time
            try {
                Socket clientSocket = server.accept();
                connectionTimes.add(LocalDateTime.now());

                // starts a new thread for the client                
                new ClientHandler(clientSocket).start();
            } catch (Exception e) {
                // caught error message
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    // disconnects the server and client
    public void disconnect() {
        // tries to close the server if the server exists
        try {
            if (server != null) {
                server.close();
            }
        } catch (Exception e) {
            // caught error message
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }

    // returns the array list of times
    public ArrayList<LocalDateTime> getConnectedTimes() {
        return connectionTimes;
    }

    // Client handler thread
    private class ClientHandler extends Thread {

        // initializes the new socket
        private Socket clientSocket;

        public ClientHandler(Socket socket) {

            // sets the socket
            this.clientSocket = socket;
        }

        // run
        public void run() {

            // in website, 
            try (
                // translates the output stream
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                // parses the input from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                // the code to let access to server
                String code = in.readLine();
                // checks that there is a code and that it is equal to 12345
                if (code == null || !code.equals("12345")) {
                    // If wrong code print couldnt handshake and close the socket
                    out.println("couldn't handshake");
                    clientSocket.close();
                    return;
                }

                // if it was the correct code
                String message;

                // takes message until null terminator
                while ((message = in.readLine()) != null) {
                    // if (message.length() > 10) {
                    //     out.println("There was an exception on the server");
                    // }

                    // tries 
                    try {
                        // parses the 
                        int input = Integer.parseInt(message); 
        
                        int ans = factor(input);
        
                        out.println("The number " + input + " has " + ans + " factors"); 
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing input: " + e.getMessage());
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
