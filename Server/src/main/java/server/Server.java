package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private static  final int PORT=8007;
    ServerSocket serverSocket=null;

    static List<Socket> clientSockets=new ArrayList<>();

    public Server() {

        try {
            serverSocket=new ServerSocket(PORT);
            System.out.println("Server is Started.Listening on post "+ PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

