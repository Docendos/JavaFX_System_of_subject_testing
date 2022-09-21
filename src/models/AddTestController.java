package models;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.DbConn;
import sample.Test;

import static sample.DbConn.getConnect;

public class AddTestController {

    int thema_id;
    String thema_name=null;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Test test = null;
    private boolean update;
    int TestId;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> ChoiceBoxThema;

    @FXML
    private TextField textFld;

    @FXML
    void clean() {
        textFld.setText(null);
        ChoiceBoxThema.setValue(null);
    }

    @FXML
    void save(MouseEvent event) {

        connection = DbConn.getConnect();
        String text = textFld.getText();
        String thema = String.valueOf(ChoiceBoxThema.getValue());



        if (text.isEmpty() || thema.isEmpty()) {
            System.out.println("An error with AddTest occurred");
        } else {
            if(update==false){
                getQuery();
                insert();
                clean();}
            else {
                getQuery();
                update_info();
                clean();
            }
        }
    }

    private void insert() {

        try {
            String sel= "SELECT thema_id FROM thema WHERE thema_name = ?";
            PreparedStatement pr2 = getConnect().prepareStatement(sel);
            pr2.setString(1, thema_name);
            ResultSet post_res2 = pr2.executeQuery();

            while(post_res2.next()) {
                thema_id = post_res2.getInt("thema_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textFld.getText());
            preparedStatement.setInt(2, thema_id);
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void update_info(){

        try {
            String sel= "SELECT thema_id FROM thema WHERE thema_name = ?";
            PreparedStatement pr2 = getConnect().prepareStatement(sel);
            pr2.setString(1, thema_name);
            ResultSet post_res2 = pr2.executeQuery();

            while(post_res2.next()) {
                thema_id = post_res2.getInt("thema_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
        query = "UPDATE the_test SET test_name=?, thema_id=? WHERE test_id = " + TestId;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, textFld.getText());
        preparedStatement.setInt(2, thema_id);
        preparedStatement.execute();

    } catch (SQLException ex) {
        Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }


    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO the_test(test_name, thema_id) VALUES (?,?)";
        } else {
            //query = "UPDATE the_test SET test_name=?, thema_id=1 WHERE test_id = " + TestId;
        }
    }

    @FXML
    void initialize() {
        try {
            String select = "SELECT thema_name FROM thema";
            PreparedStatement prepSt = getConnect().prepareStatement(select);
            ResultSet res = prepSt.executeQuery();

            while(res.next()){
                ChoiceBoxThema.getItems().add(res.getString("thema_name"));
            }

            ChoiceBoxThema.setOnAction(this::getValueSubject);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getValueSubject(ActionEvent event){
        String choice = ChoiceBoxThema.getValue();
        thema_name = choice;}

    public void setUpdate(boolean b) {
        this.update = b;
    }

    public void setTextField(int id, String text, String thema) {
        TestId = id;
        textFld.setText(text);
        ChoiceBoxThema.setValue(thema);
    }
}
