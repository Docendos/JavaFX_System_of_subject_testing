package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.FXML.Home;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static sample.Controller.id_student;
import static sample.DbConn.getConnect;
import static sample.TesterController.test_id;

public class TestingController {

    String[] arr = {"","","",""};
    int i=0;
    int count=0;

    Connection connection = null;
    String asks_text=null;
    int asks_id;

    int answers_id;

    String answers_text=null;

    int timer=0;

    int result = 0;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button NextButton;

    @FXML
    private Button QuitButton;

    @FXML
    private RadioButton Radio_AnswButton1;

    @FXML
    private RadioButton Radio_AnswButton2;

    @FXML
    private RadioButton Radio_AnswButton3;

    @FXML
    private RadioButton Radio_AnswButton4;

    @FXML
    private TextField questionField;

    @FXML
    public void quit(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) QuitButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/Tester.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void next(ActionEvent actionEvent) throws SQLException {

        //System.out.println(count);
        //System.out.println(timer);

    if(timer<count){
        timer++;
        try {
            String qv = "SELECT answers_id \n" +
                    "FROM answers\n" +
                    "WHERE answers_text=?";

            PreparedStatement p = null;

            p = getConnect().prepareStatement(qv);

            p.setString(1, answers_text);
            ResultSet r = p.executeQuery();

            while(r.next()){
                answers_id = r.getInt("answers_id");}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        try {
            connection = DbConn.getConnect();
            String quer = "INSERT INTO ans_tmp VALUES (?)";
            PreparedStatement pSt = connection.prepareStatement(quer);
            pSt.setInt(1, answers_id);
            pSt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Radio_AnswButton1.setDisable(false);
        Radio_AnswButton2.setDisable(false);
        Radio_AnswButton3.setDisable(false);
        Radio_AnswButton4.setDisable(false);

        Radio_AnswButton1.setSelected(false);
        Radio_AnswButton2.setSelected(false);
        Radio_AnswButton3.setSelected(false);
        Radio_AnswButton4.setSelected(false);


        questionField.setText(null);
        Radio_AnswButton1.setText(null);
        Radio_AnswButton3.setText(null);
        Radio_AnswButton3.setText(null);
        Radio_AnswButton4.setText(null);

        try {
            String sel = "SELECT asks_text, asks_id\n" +
                    "FROM asks\n" +
                    "JOIN asks_answers USING(asks_id) \n" +
                    "JOIN answers USING(answers_id)\n" +
                    "JOIN asks_answers_test USING(asks_answers_id)\n" +
                    "JOIN the_test USING(test_id)\n" +
                    "WHERE test_id=? \n" +
                    "AND asks_id NOT IN (SELECT id \n" +
                    "\t\t\t\t\tFROM temp)\n" +
                    "LIMIT 1";

            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);

            pr.setInt(1, test_id);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                asks_text = resultSet.getString("asks_text");
                asks_id = resultSet.getInt("asks_id");
            }

            questionField.setText(asks_text);
            //System.out.println(asks_text +" "+asks_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String qr = "SELECT answers_text\n" +
                "FROM asks \n" +
                "JOIN asks_answers USING(asks_id)\n" +
                "JOIN answers USING(answers_id)\n" +
                "JOIN asks_answers_test USING(asks_answers_id)\n" +
                "JOIN the_test USING(test_id)\n" +
                "WHERE test_id=? AND asks_id=?";

        PreparedStatement pre = null;

        pre = getConnect().prepareStatement(qr);

        pre.setInt(1, test_id);
        pre.setInt(2, asks_id);
        ResultSet resul = pre.executeQuery();

        while(resul.next()){
            arr[i] = resul.getString("answers_text");
            i+=1;
        }
        i=0;

        List<String> res = Arrays.asList(Arrays.stream(arr).toArray(String[]::new));
        Collections.shuffle(res);

        Radio_AnswButton1.setText(res.get(i));
        Radio_AnswButton2.setText(res.get(i+1));
        Radio_AnswButton3.setText(res.get(i+2));
        Radio_AnswButton4.setText(res.get(i+3));

        String quer1 = "INSERT INTO temp VALUES(?)";
        PreparedStatement prepS = null;
        prepS = getConnect().prepareStatement(quer1);
        prepS.setInt(1, asks_id);
        prepS.execute();

    if(timer==count-1) {
        NextButton.setText("Finish");
    }

    if(timer==count){
        NextButton.setDisable(true);

            try {
                // Creating Callable Statement

                String query = "SELECT * FROM array_intersect(?)";
                CallableStatement cs = getConnect().prepareCall(query);
                cs.setInt(1, test_id);
                ResultSet rs = cs.executeQuery();

                if (rs == null) {
                    System.out.println("Result is null");
                } else {
                    while(rs.next()){
                    result = rs.getInt("array_intersect");
                    }
                    connection.close();
                }


                try {
                    connection = DbConn.getConnect();
                    String quer = "INSERT INTO test_student(test_id, students_id, mark, test_date) VALUES (?,?,?,current_date)";
                    PreparedStatement pSt = connection.prepareStatement(quer);
                    pSt.setInt(1, test_id);
                    pSt.setInt(2, id_student);
                    pSt.setInt(3, result);
                    pSt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("RESULTS");
                alert.setHeaderText(null);
                alert.setContentText("Result: " + String.valueOf(result)+"/"+
                        String.valueOf(count*10) + "-" + String.valueOf( Math.round((double)result/(count*10))*100)+" %");
                alert.showAndWait();

                //System.out.println(id_student);



                try {
                    connection = DbConn.getConnect();
                    String quer = "delete from ans_tmp;\n" +
                            "delete from temp";
                    PreparedStatement pSt = connection.prepareStatement(quer);
                    pSt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    }
    else {}


    }


    @FXML
    void initialize() throws SQLException {

        timer=0;
        ///////////////////////////////

        try {
            String sel0 = "SELECT asks_text, asks_id\n" +
                    "FROM asks \n" +
                    "JOIN asks_answers USING(asks_id)\n" +
                    "JOIN answers USING(answers_id)\n" +
                    "JOIN asks_answers_test USING(asks_answers_id)\n" +
                    "JOIN the_test USING(test_id)\n" +
                    "WHERE test_id=? AND rightness=1";

            PreparedStatement pr0 = null;

            pr0 = getConnect().prepareStatement(sel0);

            pr0.setInt(1, test_id);
            ResultSet resultSet0 = pr0.executeQuery();

            while(resultSet0.next()){
                count++;
            }

            //System.out.println(count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //////////////////////////////



        try {
            String sel = "SELECT asks_text, asks_id\n" +
                    "FROM asks \n" +
                    "JOIN asks_answers USING(asks_id)\n" +
                    "JOIN answers USING(answers_id)\n" +
                    "JOIN asks_answers_test USING(asks_answers_id)\n" +
                    "JOIN the_test USING(test_id)\n" +
                    "WHERE test_id=? LIMIT 1";

            PreparedStatement pr = null;

            pr = getConnect().prepareStatement(sel);

            pr.setInt(1, test_id);
            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
                asks_text = resultSet.getString("asks_text");
                asks_id = resultSet.getInt("asks_id");
            }

            questionField.setText(asks_text);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "INSERT INTO temp VALUES(?)";
        PreparedStatement prepS = null;
        prepS = getConnect().prepareStatement(query);
        prepS.setInt(1, asks_id);
        prepS.execute();



        questionField.setText(asks_text);


        String qr = "SELECT answers_text\n" +
                "FROM asks \n" +
                "JOIN asks_answers USING(asks_id)\n" +
                "JOIN answers USING(answers_id)\n" +
                "JOIN asks_answers_test USING(asks_answers_id)\n" +
                "JOIN the_test USING(test_id)\n" +
                "WHERE test_id=? AND asks_id=?";

        PreparedStatement pre = null;

        pre = getConnect().prepareStatement(qr);

        pre.setInt(1, test_id);
        pre.setInt(2, asks_id);
        ResultSet resul = pre.executeQuery();

        while(resul.next()){
            arr[i] = resul.getString("answers_text");
            i+=1;
        }
        i=0;

        //String[] arr = {"","","",""};
        List<String> res = Arrays.asList(Arrays.stream(arr).toArray(String[]::new));
        Collections.shuffle(res);

        Radio_AnswButton1.setText(res.get(i));
        Radio_AnswButton2.setText(res.get(i+1));
        Radio_AnswButton3.setText(res.get(i+2));
        Radio_AnswButton4.setText(res.get(i+3));

        //System.out.println(asks_text +" "+asks_id);
    }


    public void check_first_btn(ActionEvent actionEvent) {
        if(Radio_AnswButton1.isSelected()){
            Radio_AnswButton2.setDisable(true);
            Radio_AnswButton3.setDisable(true);
            Radio_AnswButton4.setDisable(true);
            answers_text=Radio_AnswButton1.getText();
        } else {
            Radio_AnswButton2.setDisable(false);
            Radio_AnswButton3.setDisable(false);
            Radio_AnswButton4.setDisable(false);
        }
    }

    public void check_second_btn(ActionEvent actionEvent) {
        if(Radio_AnswButton2.isSelected()){
            Radio_AnswButton1.setDisable(true);
            Radio_AnswButton3.setDisable(true);
            Radio_AnswButton4.setDisable(true);
            answers_text=Radio_AnswButton2.getText();
        } else {
            Radio_AnswButton1.setDisable(false);
            Radio_AnswButton3.setDisable(false);
            Radio_AnswButton4.setDisable(false);
        }
    }

    public void check_third_btn(ActionEvent actionEvent) {
        if(Radio_AnswButton3.isSelected()){
            Radio_AnswButton1.setDisable(true);
            Radio_AnswButton2.setDisable(true);
            Radio_AnswButton4.setDisable(true);
            answers_text=Radio_AnswButton3.getText();
        } else {
            Radio_AnswButton1.setDisable(false);
            Radio_AnswButton2.setDisable(false);
            Radio_AnswButton4.setDisable(false);
        }
    }

    public void check_fourth_btn(ActionEvent actionEvent) {
        if(Radio_AnswButton4.isSelected()){
            Radio_AnswButton1.setDisable(true);
            Radio_AnswButton2.setDisable(true);
            Radio_AnswButton3.setDisable(true);
            answers_text=Radio_AnswButton4.getText();
        } else {
            Radio_AnswButton1.setDisable(false);
            Radio_AnswButton2.setDisable(false);
            Radio_AnswButton3.setDisable(false);
        }
    }
}

