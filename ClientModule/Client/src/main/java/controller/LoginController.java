package controller;

import clientpkg.Client;
import com.jfoenix.controls.JFXButton;
import dto.Profile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import serverpkg.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8020;
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView loadingImgView;
    @FXML
    private Label loadingLbl;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private TextField userName;
    @FXML
    private Label errorLbl;
    @FXML
    private Label errorLbl1;
    private Server server;
    private double xOffset = 0;
    private double yOffset = 0;
    private Profile profile = null;
    private Socket socket;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    @FXML
    void onActionAddUser(ActionEvent event) throws IOException {

        String isUserExists = "false";
        String isUserActive = "false";

        socket = new Socket(SERVER_IP, SERVER_PORT);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.writeUTF(URLEncoder.encode("*Login User*", StandardCharsets.UTF_8));
        dataOutputStream.writeUTF(URLEncoder.encode("*UserExists*", StandardCharsets.UTF_8));
        dataOutputStream.writeUTF(URLEncoder.encode(userName.getText(), StandardCharsets.UTF_8));
        dataOutputStream.flush();

        isUserExists = URLDecoder.decode(dataInputStream.readUTF(), StandardCharsets.UTF_8);

        dataOutputStream.writeUTF(URLEncoder.encode("*UserActive*", StandardCharsets.UTF_8));
        dataOutputStream.writeUTF(URLEncoder.encode(userName.getText(), StandardCharsets.UTF_8));
        dataOutputStream.flush();

        isUserActive = URLDecoder.decode(dataInputStream.readUTF(), StandardCharsets.UTF_8);

        if (isUserExists.equals("true")) {
            if (isUserActive.equals("true")) {
                try {
                    dataOutputStream.writeUTF(URLEncoder.encode("*getProfile*", StandardCharsets.UTF_8));
                    dataOutputStream.writeUTF(URLEncoder.encode(userName.getText(), StandardCharsets.UTF_8));
                    ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
                    Profile profile = (Profile) objectInputStream.readObject();
                    Stage newStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chatRoom.fxml"));
                    loader.setControllerFactory(controllerClass -> {
                        return new ChatRoomController(profile);
                    });
                    Parent root = loader.load();
                    ChatRoomController controller = loader.getController();
                    Client client = new Client(controller);
                    controller.setClient(client);
                    Thread clientThread = new Thread(client);
                    clientThread.setDaemon(true); // Set the thread as a daemon thread
                    clientThread.start();

                    Scene scene = new Scene(root);


                    root.setOnMousePressed(this::handleMousePressed);
                    root.setOnMouseDragged(this::handleMouseDragged);

                    newStage.setScene(scene);
                    newStage.initStyle(StageStyle.UNDECORATED);
                    newStage.show();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                userName.clear();
                errorLbl.setVisible(false);
                errorLbl1.setVisible(false);
            } else {
                System.out.println("User Already Active");
                errorLbl.setVisible(false);
                errorLbl1.setVisible(true);
            }
        } else {
            System.out.println("User DoesNot Exists");
            errorLbl.setVisible(true);
            errorLbl1.setVisible(false);
        }
    }

    @FXML
    void onMouseClick(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml")));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        server=Server.getServerInstance();

        loginBtn.setVisible(false);
        userName.setVisible(false);
        errorLbl.setVisible(false);
        errorLbl1.setVisible(false);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            loadingLbl.setVisible(false);
            loadingImgView.setVisible(false);
            loginBtn.setVisible(true);
            userName.setVisible(true);
        }));
        timeline.play();
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}

