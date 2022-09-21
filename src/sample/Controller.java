package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import helpers.Shake;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.event.MouseEvent;

import static java.lang.System.out;
import static sample.DbConn.getConnect;

public class Controller {
    public static int id_asker;
    public static int id_student;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField LoginField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button SignInButton;

    @FXML
    void initialize() {
    SignInButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String loginText = LoginField.getText().trim();
            String loginPassword = PasswordField.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals("")){
                loginUser(loginText,loginPassword);
            } else {
                System.out.println("Error");
            }

        }
    });}

    private void loginUser(String loginText, String loginPassword) {


        if(loginText.equals("a") && loginPassword.equals("a")) break_though("/sample/FXML/Admin.fxml");


        int i = search_asker(loginText, loginPassword);
        int j = search_student(loginText, loginPassword);


            if(i>=1){
            find_asker(loginText, loginPassword);
            break_though("/sample/FXML/Ask_Asker.fxml");
            } else if (j>=1){
                find_student(loginText, loginPassword);
                break_though("/sample/FXML/Tester.fxml");
            } else {
                Shake userLoginAnim = new Shake(LoginField);
                Shake userPasswordAnim = new Shake(PasswordField);
                userPasswordAnim.playAnim();
                userLoginAnim.playAnim();
            }

    }

    public void registr(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/Register.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void break_though (String str){
        Stage stage = (Stage) SignInButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(str));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public int search_student(String loginText, String loginPassword){
        String select = "SELECT * FROM askers WHERE login = ? AND password = ?";
        int i = 0;
        PreparedStatement pr = null;
        try {
            pr = getConnect().prepareStatement(select);
        pr.setString(1, loginText);
        pr.setString(2, loginPassword);

        ResultSet res = pr.executeQuery();


        while(res.next()) {
            i++;
        }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return i;
    }

    public int search_asker(String loginText, String loginPassword){
        String select2 = "SELECT * FROM students WHERE login = ? AND password = ?";
        int j = 0;
        PreparedStatement pr = null;
        try {
            PreparedStatement pr2 = getConnect().prepareStatement(select2);
            pr2.setString(1, loginText);
            pr2.setString(2, loginPassword);

            ResultSet res2 = pr2.executeQuery();

            while(res2.next()) {
                j++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return j;
    }


    public void find_student(String loginText, String loginPassword){

        String post_select= "SELECT id FROM students WHERE login = ? AND password = ?";
        PreparedStatement prepSt = null;
        try {
            prepSt = getConnect().prepareStatement(post_select);

        prepSt.setString(1, loginText);
        prepSt.setString(2, loginPassword);

        ResultSet post_res = prepSt.executeQuery();

        while(post_res.next()) {
            this.id_student = post_res.getInt("id");
        }
        } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }

    public void find_asker(String loginText, String loginPassword){

        String post_select= "SELECT id FROM askers WHERE login = ? AND password = ?";
        PreparedStatement prepSt = null;
        try {
            prepSt = getConnect().prepareStatement(post_select);

            prepSt.setString(1, loginText);
            prepSt.setString(2, loginPassword);

            ResultSet post_res = prepSt.executeQuery();

            while(post_res.next()) {
                this.id_asker = post_res.getInt("id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

