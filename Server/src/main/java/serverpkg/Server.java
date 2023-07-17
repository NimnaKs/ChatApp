package serverpkg;

import controller.ServerRoomController;
import dto.Profile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoginModel;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private static final int PORT = 8020;
    static List<Socket> clientSockets = new ArrayList<>();
    private static Server server;
    ServerSocket serverSocket = null;
    private ServerRoomController serverRoomController;
    private double xOffset = 0;
    private double yOffset = 0;

    private String message = null;

    private Server() {

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Server getServerInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
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

    public void userDeActivate(String fName) throws SQLException {
        LoginModel.userDeactivate(fName);
    }

    public ServerRoomController getServerRoomController() {
        return serverRoomController;
    }

    public void setServerRoomController(ServerRoomController serverRoomController) {
        this.serverRoomController = serverRoomController;
    }

    public void load() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/serverRoom.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        root.setOnMousePressed(this::handleMousePressed);
        root.setOnMouseDragged(this::handleMouseDragged);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ServerRoomController controller = loader.getController();
        server.setServerRoomController(controller);
        controller.setInfo("Server is Started .....");
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();
        primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
    }

    @Override
    public void run() {
        try {
            while (true) {
//                serverRoomController.setInfo("Server is Listening.....");
                Socket clientSocket = serverSocket.accept();

                boolean isAClient = isAClient(clientSocket);

                ClientHandler clientHandler;
                if (isAClient) {
                    clientSockets.add(clientSocket);
                    clientHandler = new ClientHandler(clientSocket, serverRoomController, message);
                } else {
                    clientHandler = new ClientHandler(clientSocket);
                }
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAClient(Socket clientSocket) throws IOException {
        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        message = URLDecoder.decode(inputStream.readUTF(), "UTF-8");
        if (message.equals("*NewUser*")) {
            message = inputStream.readUTF();
            return true;
        }
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

