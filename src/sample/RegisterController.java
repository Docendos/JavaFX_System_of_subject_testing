package sample;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;

import static sample.DbConn.getConnect;

public class RegisterController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Back2Button;

    @FXML
    private void handleButtonAction( ) throws IOException {
        Stage stage = (Stage) Back2Button.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/sample.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    private TextField LoginField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private Button SignUpButton;


    @FXML
    private TextField SignUpLastName;

    @FXML
    private TextField SignUpName;

    @FXML
    private TextField SignUpPatronymic;


    @FXML
    void initialize() {


        SignUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signUpNewUser();
            }
        });
    }

    private void signUpNewUser() {

        String firstName = SignUpName.getText();
        String lastName = SignUpLastName.getText();
        String middleName = SignUpPatronymic.getText();
        String userName = LoginField.getText();
        String password = PasswordField.getText();

        String insert = "INSERT INTO students(name, last, midle, login, password) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement prst = getConnect().prepareStatement(insert);
            prst.setString(1,firstName);
            prst.setString(2,lastName);
            prst.setString(3,middleName);
            prst.setString(4,userName);
            prst.setString(5,password);

            prst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SignUpName.setText(null);
        SignUpLastName.setText(null);
        SignUpPatronymic.setText(null);
        LoginField.setText(null);
        PasswordField.setText(null);

    }

}

