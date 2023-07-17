package clientpkg;

import controller.ChatRoomController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {
    private static final String SERVER_IP="localhost";

    private static final int SERVER_PORT=8020;
    final private Socket socket;

    final private DataInputStream dataInputStream;

    final private DataOutputStream dataOutputStream;

    final private ChatRoomController controller;

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public ChatRoomController getController() {
        return controller;
    }

    public Client(ChatRoomController controller) {
        try {
            socket=new Socket(SERVER_IP,SERVER_PORT);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.controller=controller;
            dataOutputStream.writeUTF(URLEncoder.encode("*NewUser*", StandardCharsets.UTF_8));
            dataOutputStream.writeUTF(controller.getProfile().getF_name()+" Join the Chat");
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                String message = URLDecoder.decode(dataInputStream.readUTF(), "UTF-8");
                if (message.equals("#imag3*")) {
                    String fname = URLDecoder.decode(dataInputStream.readUTF(), "UTF-8");
                    int imageSize = dataInputStream.readInt();
                    byte[] imageBytes = new byte[imageSize];
                    dataInputStream.readFully(imageBytes);
                    controller.addPhoto(fname, imageBytes, "#373E4E", "CENTER_LEFT");
                } else if (message.equals("*NewUser*")) {
                    String messages = URLDecoder.decode(dataInputStream.readUTF(), StandardCharsets.UTF_8);
                    String receiveMsgStyle = "-fx-background-color:  #171821; " +
                            "-fx-padding: 5px;" +
                            "-fx-font-size: 10px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-font-family: Arial; " +
                            "-fx-background-radius: 5";
                    controller.addClientMessage(messages, receiveMsgStyle, "CENTER");
                } else {
                    String receiveMsgStyle = "-fx-background-color: #373E4E; " +
                            "-fx-padding: 5px;" +
                            "-fx-font-size: 12px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #FFFFFF;" +
                            "-fx-font-family: Arial; " +
                            "-fx-background-radius: 5";
                    controller.addClientMessage(message, receiveMsgStyle, "CENTER_LEFT");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMsg(String message) {
        try {
            dataOutputStream.writeUTF(URLEncoder.encode(message, StandardCharsets.UTF_8));
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendImg(String fName, byte[] imageBytes) {
        try {
            dataOutputStream.writeUTF(URLEncoder.encode("#imag3*", StandardCharsets.UTF_8));
            dataOutputStream.writeUTF(URLEncoder.encode(fName, StandardCharsets.UTF_8));
            dataOutputStream.writeInt(imageBytes.length);
            dataOutputStream.write(imageBytes);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clientLeftMsg() {
        try {
            dataOutputStream.writeUTF("*NewUser*");
            dataOutputStream.writeUTF(controller.getProfile().getF_name() + " Left the Chat");
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deActivateUser(String fName) {
        try {
            dataOutputStream.writeUTF("*UserDeactivate*");
            dataOutputStream.writeUTF(fName);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
