package serverpkg;

import controller.ServerRoomController;
import dto.Profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataInputStream inputStream;

    private DataOutputStream outputStream;

    private ServerRoomController serverRoomController;

    public ClientHandler(Socket clientSocket, ServerRoomController serverRoomController, String message) {
        this.clientSocket = clientSocket;
        this.serverRoomController = serverRoomController;
        this.serverRoomController.setInfo(message);         
        broadcastMsg("*NewUser*",message);
    }

    public ClientHandler(Socket clientSocket) {
        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {
        try{
            inputStream=new DataInputStream(clientSocket.getInputStream());
            outputStream=new DataOutputStream(clientSocket.getOutputStream());

            while (true){
                String message= URLDecoder.decode(inputStream.readUTF(), "UTF-8");
//                if (message.equals("*NewUser*")){
//                    String messages = URLDecoder.decode(inputStream.readUTF(), "UTF-8");
//                    serverRoomController.setInfo(messages);
//                    broadcastMsg(message,messages);
//                } 
                switch (message) {
                    case "#imag3*":
                        String senderName = inputStream.readUTF();
                        int imageSize = inputStream.readInt();
                        byte[] imageBytes = new byte[imageSize];
                        inputStream.readFully(imageBytes);
                        broadcastMsg(senderName, imageBytes);
                        break;
                    case "*UserExists*": {
                        String messages = URLDecoder.decode(inputStream.readUTF(), "UTF-8");
                        boolean isUserExists = Server.getServerInstance().checkName(messages);
                        outputStream.writeUTF(String.valueOf(isUserExists));
                        outputStream.flush();
                        break;
                    }
                    case "*UserActive*": {
                        String messages = URLDecoder.decode(inputStream.readUTF(), "UTF-8");
                        boolean isUserActive = Server.getServerInstance().getActiveStatus(messages);
                        outputStream.writeUTF(String.valueOf(isUserActive));
                        outputStream.flush();
                        break;
                    }
                    case "*getProfile*": {
                        String messages = URLDecoder.decode(inputStream.readUTF(), "UTF-8");
                        Profile profile = Server.getServerInstance().getProfile(messages);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(profile);
                        objectOutputStream.flush();
                        break;
                    }
                    case "*NewUser*": {
                        String messages = inputStream.readUTF();
                        serverRoomController.setInfo(messages);
                        broadcastMsg(message, messages);
                        break;
                    }
                    case "*UserDeactivate*": {
                        String fName = inputStream.readUTF();
                        Server.getServerInstance().userDeActivate(fName);
                        break;
                    }
                    default:
                        broadcastMsg(message);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void broadcastMsg(String message, String messages) {
        try {
            for (Socket socket : Server.clientSockets) {
                if(socket != clientSocket){
                    outputStream=new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(message);
                    outputStream.writeUTF(messages);
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastMsg(String senderName, byte[] imageBytes) {
        try {
            for (Socket socket : Server.clientSockets) {
                if(socket != clientSocket){
                    outputStream=new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(URLEncoder.encode("#imag3*", "UTF-8"));
                    outputStream.writeUTF(URLEncoder.encode(senderName, "UTF-8"));
                    outputStream.writeInt(imageBytes.length);
                    outputStream.write(imageBytes);
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void broadcastMsg(String message) {
        try {
            for (Socket socket : Server.clientSockets) {
                if(socket != clientSocket){
                    outputStream=new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(URLEncoder.encode(message, "UTF-8"));
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

