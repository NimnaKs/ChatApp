package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ServerRoomController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton onActionAddUser;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox chatContainer;

    @FXML
    void onMouseClick(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/serverRoom.fxml")));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.hide();
    }

    public void setInfo(String info) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(info);
            messageLabel.setStyle("-fx-background-color:  #171821; " +
                    "-fx-padding: 5px;" +
                    "-fx-font-size: 12px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-family: Arial; " +
                    "-fx-background-radius: 5;");
            HBox messageContainer = new HBox(messageLabel);
            messageContainer.setAlignment(Pos.valueOf("CENTER_LEFT"));
            chatContainer.setSpacing(15);
            chatContainer.getChildren().add(messageContainer);

        });
    }
}
