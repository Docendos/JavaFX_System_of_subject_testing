package sample;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static sample.DbConn.getConnect;

public class TesterController {

    public static int test_id;
    int count=0;
    String thema_name=null;
    String test_name=null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BackSampleButton;

    @FXML
    private ChoiceBox<String> ChoiceBoxNoTest;

    @FXML
    private ChoiceBox<String> ChoiceBoxSubject;

    @FXML
    private TextField FildFIO;

    @FXML
    private Button StartButton;

    @FXML
    public void start(ActionEvent actionEvent) throws IOException, SQLException {

        //System.out.println(test_name);

        try {
            String sel = "SELECT test_id FROM the_test WHERE test_name=?";
            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);

            pr.setString(1, test_name);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                this.test_id = resultSet.getInt("test_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(pass);

        Stage stage = (Stage) StartButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/Testing.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }



    @FXML
    void toSample(ActionEvent event) {
        Stage stage = (Stage) BackSampleButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/sample.fxml"));
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

    @FXML
    void initialize() {
        try {
        String select = "SELECT thema_name FROM thema";
        PreparedStatement prepSt = getConnect().prepareStatement(select);
        ResultSet res = prepSt.executeQuery();

        while(res.next()){
            ChoiceBoxSubject.getItems().add(res.getString("thema_name"));
        }

        ChoiceBoxSubject.setOnAction(this::getValueThema);
        ChoiceBoxNoTest.setOnAction(this::getValueTest);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getValueThema(ActionEvent event){
        String choice = ChoiceBoxSubject.getValue();
        ChoiceBoxNoTest.setValue(null);
        ChoiceBoxNoTest.getItems().clear();
        count=0;
        thema_name = choice;

    }


    public void getValueTest(ActionEvent event){
        String test = ChoiceBoxNoTest.getValue();
        test_name = test;
    }

    public void update(javafx.scene.input.MouseEvent mouseEvent) {
        //ChoiceBoxNoTest.getItems().removeAll();
        count++;
        if(count<=1){
        try {
            String sel = "SELECT test_name FROM the_test JOIN thema USING(thema_id) WHERE thema_name=?";
            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);
            //System.out.println(thema_name);
            pr.setString(1, thema_name);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                ChoiceBoxNoTest.getItems().add(resultSet.getString("test_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        //System.out.println(ChoiceBoxNoTest.getValue());
    }


}


