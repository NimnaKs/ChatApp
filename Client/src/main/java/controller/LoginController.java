package controller;

import com.jfoenix.controls.JFXButton;
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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void onActionAddUser(ActionEvent event) throws IOException {
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

}

