package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.LoginModel;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private TextField f_name;

    @FXML
    private TextField l_name;

    @FXML
    private ImageView profileImg;

    @FXML
    private Label errorLbl;
    private String imgPath="/Users/mac/Desktop/ChatApp/Server/src/main/resources/assets/add-photo.png";

    @FXML
    void onActionAddUser(ActionEvent event) throws IOException {
        boolean addOk=false;
        try {
            addOk=LoginModel.saveUserDetails(imgPath,f_name.getText(),l_name.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (addOk){
            errorLbl.setVisible(true);
            imgPath="/Users/mac/Desktop/ChatApp/Server/src/main/resources/assets/add-photo.png";
            addPhoto();
            f_name.clear();
            l_name.clear();
        }else{
            errorLbl.setText("Details save Unsuccessful.");
        }
    }

    private void addPhoto() throws IOException {
        assert imgPath != null;
        File imageFile = new File(imgPath);
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] imageBytes = new byte[(int) imageFile.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        Image image = new Image(byteArrayInputStream);
        profileImg.setImage(image);
    }

    @FXML
    void onMouseClick(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addUserForm.fxml")));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.hide();
    }

    @FXML
    void onImgChange(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        Stage primaryStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            imgPath = selectedFile.getAbsolutePath();
        }

        addPhoto();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLbl.setVisible(false);
    }
}
