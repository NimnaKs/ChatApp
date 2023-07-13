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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private static  final int PORT=8020;
    ServerSocket serverSocket=null;
    static List<Socket> clientSockets=new ArrayList<>();

    private ServerRoomController serverRoomController;

    private static Server server;

    private double xOffset = 0;
    private double yOffset = 0;
    private Server() {

        try {
            serverSocket=new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Server getServerInstance(){
        if (server==null){
            server=new Server();
        }
        return server;
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket,serverRoomController);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public ServerRoomController getServerRoomController() {
        return serverRoomController;
    }

    public void setServerRoomController(ServerRoomController serverRoomController) {
        this.serverRoomController = serverRoomController;
    }

    public void load() throws IOException {
        Stage stage=new Stage();
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
}

