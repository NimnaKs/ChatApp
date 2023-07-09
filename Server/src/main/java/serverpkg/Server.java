package serverpkg;

import dto.Profile;
import model.LoginModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private static  final int PORT=8008;
    ServerSocket serverSocket=null;
    static List<Socket> clientSockets=new ArrayList<>();

    private static Server server=null;
    private Server() {

        try {
            serverSocket=new ServerSocket(PORT);
            System.out.println("Server is Started.Listening on post "+ PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Server getServerInstance(){
        return (server==null)?server=new Server():server;
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
        Server.getServerInstance().start();
    }

    public boolean checkName(String username) throws SQLException {
        return LoginModel.checkName(username);
    }

    public boolean getActiveStatus(String username) throws SQLException {
        return LoginModel.getActiveStatus(username);
    }

    public Profile getProfile(String username) throws SQLException {
        return LoginModel.getProfile(username);
    }

    public boolean userDeActivate(String fName) throws SQLException {
        return LoginModel.userDeactivate(fName);
    }
}

