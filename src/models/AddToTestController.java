package models;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import sample.DbConn;

import static sample.DbConn.getConnect;

public class AddToTestController {

    String test_name = null;
    String answers_text=null;
    int test_id;
    int asks_id;
    int answers_id;

    int asks_answers_id;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> ChoiceBoxTests;

    @FXML
    void add(MouseEvent event) {
        try {
        String query = "SELECT test_id FROM the_test WHERE test_name=?";
        Connection connection = DbConn.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, test_name);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
           test_id = resultSet.getInt("test_id");
        }

        //System.out.println(test_id);

        String sel= "SELECT answers_id FROM answers WHERE answers_text = ?";
        PreparedStatement pr2 = getConnect().prepareStatement(sel);
        pr2.setString(1, answers_text);
        ResultSet post_res2 = pr2.executeQuery();

        while(post_res2.next()) {
             answers_id = post_res2.getInt("answers_id");
        }

        String sl = "SELECT asks_answers_id FROM asks_answers WHERE asks_id = ? AND answers_id=?";
        PreparedStatement pr3 = getConnect().prepareStatement(sl);
        pr3.setInt(1, asks_id);
        pr3.setInt(2, answers_id);
        ResultSet post_res3 = pr3.executeQuery();

        while(post_res3.next()) {
            asks_answers_id = post_res3.getInt("asks_answers_id");
        }

        String quer = "INSERT INTO asks_answers_test(asks_answers_id, test_id, max_mark) VALUES (?,?,10)";
        PreparedStatement pSt = connection.prepareStatement(quer);
        pSt.setInt(1, asks_answers_id);
        pSt.setInt(2, test_id);
        pSt.execute();

        ChoiceBoxTests.setValue(null);

        //System.out.println(asks_answers_id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void initialize() {

        try {
            String sel = "SELECT test_name FROM the_test";
            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                ChoiceBoxTests.getItems().add(resultSet.getString("test_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ChoiceBoxTests.setOnAction(this::getValueTest);

    }

    public void getValueTest(ActionEvent event){
        String test = ChoiceBoxTests.getValue();
        test_name = test;
        //System.out.println(test_name);
    }

    public void getIDs(int id, String answer){
      asks_id = id;
      answers_text = answer;
    }

}
