package controller;


import clientpkg.Client;
import com.jfoenix.controls.JFXTextField;
import dto.Profile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serverpkg.Server;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private Label headerLbl;

    @FXML
    private ImageView profileImg;

    @FXML
    private Label profileName;

    @FXML
    private ImageView sendBtn;

    @FXML
    private JFXTextField txtMsg;

    @FXML
    private VBox chatContainer;

    @FXML
    private ScrollPane scrollPane;
    private Profile profile=null;

    private Client client;

    final private Server server=Server.getServerInstance();
    public void setClient(Client client) {
        this.client = client;
    }

    public ChatRoomController() {
        this.profile=new Profile("Nimna","Kaveesha","assets/user1.png");
    }

    public ChatRoomController(Profile profile) {
        this.profile = profile;
    }

    @FXML
    void onMouseClickSend(MouseEvent event) {
        String message=txtMsg.getText();
        client.sendMsg(profile.getF_name()+" : "+message);
        txtMsg.clear();
        String sendMsgStyle = "-fx-background-color: #7A8194; " +
                "-fx-padding: 5px;" +
                "-fx-font-size: 12px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: Arial; " +
                "-fx-background-radius: 5";
        addClientMessage("Me : "+message, sendMsgStyle,"CENTER_RIGHT");
    }

    @FXML
    void onMouseClick(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/chatRoom.fxml")));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        headerLbl.setText("        "+profile.getF_name()+"'s Chat Room");
        profileName.setText(profile.getF_name()+" "+profile.getL_name());
        Image image = new Image(profile.getProfilePhoto());
        profileImg.setImage(image);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    void onActionOpenGallery(MouseEvent event) throws IOException {

    }

    private void addClientPhoto(String filePath) {

    }

    public void addClientMessage(String message, String style, String alignment){
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            messageLabel.setStyle(style);
            HBox messageContainer = new HBox(messageLabel);
            messageContainer.setAlignment(Pos.valueOf(alignment));
            chatContainer.setSpacing(10);
            chatContainer.getChildren().add(messageContainer);
            scrollPane.setVvalue(1.0);
        });
    }

    public void deActivateUser() {
        try {
            boolean isDeactivate=server.userDeActivate(profile.getF_name());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
