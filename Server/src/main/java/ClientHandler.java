import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataInputStream inputStream;

    private DataOutputStream outputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try{
            inputStream=new DataInputStream(clientSocket.getInputStream());
            outputStream=new DataOutputStream(clientSocket.getOutputStream());

            while (true){

                String message=inputStream.readUTF();
                System.out.println("Received message from client: " + message);

                if (message.equals("image")){
                    String senderName=inputStream.readUTF();
                    int imageSize = inputStream.readInt();
                    byte[] imageBytes = new byte[imageSize];
                    inputStream.readFully(imageBytes);

                    broadcastMsg(senderName,imageBytes);
                }else {
                    broadcastMsg(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void broadcastMsg(String senderName, byte[] imageBytes) {
        try {
            for (Socket socket : Server.clientSockets) {
                if(socket != clientSocket){
                    outputStream=new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF("#imag3*");
                    outputStream.writeUTF(senderName);
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
                    outputStream.writeUTF(message);
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

