import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static  final int PORT=8006;
    ServerSocket serverSocket;

    public Server() {

        try {
            serverSocket=new ServerSocket(PORT);
            System.out.println("Server is Started.Listening on post "+ PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

