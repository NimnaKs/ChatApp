package clientpkg;

import controller.ChatRoomController;
import serverpkg.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Client implements Runnable {
    private static final String SERVER_IP="localhost";

    private static final int SERVER_PORT=8008;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        while (true) {
            try {

                String message = URLDecoder.decode(dataInputStream.readUTF(), "UTF-8");
                System.out.println("recived Msg" + message);
                if (message.equals("#imag3*")) {
                    String fname = URLDecoder.decode(dataInputStream.readUTF(), "UTF-8");
                    int imageSize = dataInputStream.readInt();
                    System.out.println(imageSize);
                    byte[] imageBytes = new byte[imageSize];
                    dataInputStream.readFully(imageBytes);

                    controller.addPhoto(fname, imageBytes,"#373E4E","CENTER_LEFT");
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
            dataOutputStream.writeUTF(URLEncoder.encode(message, "UTF-8"));
            dataOutputStream.flush();
            System.out.println("Client Flush Msg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendImg(String fName, byte[] imageBytes) {
        try {
            dataOutputStream.writeUTF(URLEncoder.encode("#imag3*", "UTF-8"));
            dataOutputStream.writeUTF(URLEncoder.encode(fName, "UTF-8"));
            dataOutputStream.writeInt(imageBytes.length);
            dataOutputStream.write(imageBytes);
            dataOutputStream.flush();
            System.out.println("Image sent to client.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
