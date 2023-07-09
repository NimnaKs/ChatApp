package controller;


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

import java.io.*;
import java.net.Socket;
import java.net.URL;
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

    private static final String SERVER_IP="localhost";

    private static final int SERVER_PORT=8006;

    private Profile profile=null;

    public ChatRoomController() {
        this.profile=new Profile("Nimna","Kaveesha","assets/user1.png");
    }

    public ChatRoomController(Profile profile) {
        this.profile = profile;
    }

    @FXML
    void onMouseClickSend(MouseEvent event) {
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
}
