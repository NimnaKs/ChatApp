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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serverpkg.Server;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
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

    @FXML
    private ImageView EmojiBtn;

    @FXML
    private AnchorPane emojiAnchorpane;

    private Pane emojiPane;
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
        deActivateUser();
        client.clientLeftMsg();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        headerLbl.setText("        "+profile.getF_name()+"'s Chat Room");
        profileName.setText(profile.getF_name()+" "+profile.getL_name());
        Image image = new Image(profile.getProfilePhoto());
        profileImg.setImage(image);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        emojiAnchorpane.setVisible(false);
    }
    @FXML
    void onActionOpenGallery(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        Stage primaryStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        String filePath=null;

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
        }

        // Read the image file
        assert filePath != null;
        File imageFile = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] imageBytes = new byte[(int) imageFile.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();

        addPhoto("Me",imageBytes,"#7A8194","CENTER_RIGHT");

        // Send image bytes to the client
        client.sendImg(profile.getF_name(),imageBytes);

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

    public void addPhoto(String fname, byte[] imageBytes, String colorCode, String alignment) {
        Platform.runLater(() -> {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(180, 180);
            anchorPane.setStyle("-fx-background-color: " + colorCode + ";" +
                    "-fx-padding: 5px;" +
                    "-fx-background-radius: 5;");

            Label label = new Label(fname);
            label.setStyle("-fx-background-color: " + colorCode + ";" +
                    "-fx-padding: 0px;" +
                    "-fx-font-size: 12px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-family: Arial; " +
                    "-fx-background-radius: 5;");

            AnchorPane.setTopAnchor(label, 2.0);
            AnchorPane.setLeftAnchor(label, 2.0);
            anchorPane.getChildren().add(label);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(byteArrayInputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            AnchorPane.setTopAnchor(imageView, 20.0);
            AnchorPane.setRightAnchor(imageView, 0.0);
            anchorPane.getChildren().add(imageView);

            HBox messageContainer = new HBox(anchorPane);
            messageContainer.setAlignment(Pos.valueOf(alignment));
            chatContainer.getChildren().add(messageContainer);
            chatContainer.setSpacing(10);
            scrollPane.setVvalue(1.0);
        });
    }

    @FXML
    void onActionGetEmojiPane(MouseEvent event) {
        emojiAnchorpane.setVisible(!emojiAnchorpane.isVisible());
    }

    @FXML
    void onActionEmoji1(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE0E"+" ");
    }

    @FXML
    void onActionEmoji10(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE20"+" ");
    }

    @FXML
    void onActionEmoji2(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE0D"+" ");
    }

    @FXML
    void onActionEmoji3(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE22"+" ");
    }

    @FXML
    void onActionEmoji4(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE0A"+" ");
    }

    @FXML
    void onActionEmoji5(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE18"+" ");
    }

    @FXML
    void onActionEmoji6(MouseEvent event) {
        txtMsg.appendText("✨"+" ");
    }

    @FXML
    void onActionEmoji7(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDC4D"+" ");
    }

    @FXML
    void onActionEmoji8(MouseEvent event) {
        txtMsg.appendText("❤️"+" ");
    }

    @FXML
    void onActionEmoji9(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE02"+" ");
    }

    public Profile getProfile() {
        return profile;
    }

}
