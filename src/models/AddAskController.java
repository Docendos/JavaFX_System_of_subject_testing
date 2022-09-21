package models;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.Ask;
import sample.Controller;
import sample.DbConn;

import static sample.Controller.id_asker;
import static sample.DbConn.getConnect;
import static sample.TesterController.test_id;

public class AddAskController {

   int asks_id;
   int answers_id;

   int subject_id;

   String answers_text = null;

   String subject_name=null;

    @FXML
    private ChoiceBox<String> ChoiceBoxSubject;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dateCalendarField;

    @FXML
    private TextField answerFld;

    @FXML
    private TextField dateFld;

    @FXML
    private TextField levelFld;

    @FXML
    private TextField textFld;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Ask ask = null;
    int askId;
    private boolean update;

    Controller controller = new Controller();

    @FXML
    void clean() {
        textFld.setText(null);
        levelFld.setText(null);
        dateCalendarField.setValue(null);
        answerFld.setText(null);
        ChoiceBoxSubject.setValue(null);
    }

    @FXML
    void save(MouseEvent event) {
        connection = DbConn.getConnect();
        String text = textFld.getText();
        String level = levelFld.getText();
        Date date = Date.valueOf(dateCalendarField.getValue());
        String answer = answerFld.getText();


        if (text.isEmpty() || level.isEmpty()  || answer.isEmpty() || date == null) {
            System.out.println("An error with AddAsk occurred");
        } else {
            if(update==false){
            getQuery();
            insert_info();
            clean();} else {
                getQuery();
                update_info();
                clean();
            }

        }
    }

    private void update_info() {
        try {

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, textFld.getText());
        preparedStatement.setString(2, levelFld.getText());
        preparedStatement.setDate(3, Date.valueOf(dateCalendarField.getValue()));
        preparedStatement.setInt(4,subject_id);
        preparedStatement.setString(5, answerFld.getText());
        preparedStatement.execute();



        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert_info() {

        try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, textFld.getText());
                preparedStatement.setString(2, levelFld.getText());
                preparedStatement.setDate(3, Date.valueOf(dateCalendarField.getValue()));
                preparedStatement.setInt(4, subject_id);
                preparedStatement.setInt(5, id_asker);
                preparedStatement.setString(6, answerFld.getText());
                preparedStatement.execute();


            String post_select= "SELECT asks_id FROM asks WHERE asks_text = ? AND asks_level = ? AND askers_id = ?";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);
            prepSt.setString(1, textFld.getText());
            prepSt.setString(2, levelFld.getText());
            prepSt.setInt(3, id_asker);

            ResultSet post_res = prepSt.executeQuery();

            while(post_res.next()) {
              asks_id = post_res.getInt("asks_id");
            }

            //System.out.println(asks_id);


            String sel= "SELECT answers_id FROM answers WHERE answers_text = ?";
            PreparedStatement pr2 = getConnect().prepareStatement(sel);
            pr2.setString(1, answerFld.getText());
            ResultSet post_res2 = pr2.executeQuery();

            while(post_res2.next()) {
                answers_id = post_res2.getInt("answers_id");
            }

            //System.out.println(answers_id);


            String quer = "INSERT INTO asks_answers(asks_id, answers_id, rightness) VALUES (?,?,1)";
            PreparedStatement pSt = connection.prepareStatement(quer);
            pSt.setInt(1, asks_id);
            pSt.setInt(2, answers_id);
            pSt.execute();


            } catch (SQLException ex) {
                Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
            }





    }



    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO asks(asks_text, asks_level, asks_date, subject_id, askers_id) VALUES (?,?,?,?,?);"+
                    "INSERT INTO answers(answers_text) values(?)";
        } else {
            query = "UPDATE asks SET asks_text=?, asks_level=?, asks_date=?, subject_id=? WHERE asks_id = " + askId + ";" +
                    "UPDATE answers SET answers_text=? WHERE answers_text ='" + answers_text + "'";
        }
    }


    @FXML
    void initialize() {
        try {
            String select = "SELECT subject_name FROM subject";
            PreparedStatement prepSt = getConnect().prepareStatement(select);
            ResultSet res = prepSt.executeQuery();

            while(res.next()){
                ChoiceBoxSubject.getItems().add(res.getString("subject_name"));
            }

            ChoiceBoxSubject.setOnAction(this::getValueSubject);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getValueSubject(ActionEvent event){
        String choice = ChoiceBoxSubject.getValue();
        subject_name = choice;


        try {
            String sel = "SELECT subject_id FROM subject WHERE subject_name=?";

            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);

            pr.setString(1, subject_name);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                subject_id = resultSet.getInt("subject_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

       //System.out.println(subject_id);
    }

    public void setUpdate(boolean b) {
        this.update = b;
    }

    public void setTextField(int id, String text, String level, Date date, String subject, String answer, String author) {
        askId = id;
        textFld.setText(text);
        levelFld.setText(level);
        //dateFld.setText(String.valueOf(date));
        //subjectFld.setText(middleName);
        answerFld.setText(answer);
        dateCalendarField.setValue(date.toLocalDate());
        answers_text = String.valueOf(answer);
        ChoiceBoxSubject.setValue(subject);
    }
}

