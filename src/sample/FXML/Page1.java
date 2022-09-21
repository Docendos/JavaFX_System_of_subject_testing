package sample.FXML;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import static sample.DbConn.getConnect;

public class Page1 {


    @FXML
    private Button findButton;

    @FXML
    private Label lbmax;

    @FXML
    private Label lbmin;

    @FXML
    private Button maxMonthButton;

    @FXML
    private Button maxWeekButton;

    @FXML
    private Button minMonthButton;

    @FXML
    private Button minWeekButton;

    @FXML
    private TextArea monthArea;

    @FXML
    private TextField questionField;

    @FXML
    private TextArea showArea;

    @FXML
    private TextField symbolsFld;

    @FXML
    private TextArea weekArea;

    @FXML
    public void find(javafx.event.ActionEvent actionEvent) throws SQLException {

        showArea.setText(null);
        String post_select= "SELECT name,last,midle FROM students WHERE name LIKE \'%"+symbolsFld.getText()+"%\'";
        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = prepSt.executeQuery();

        while(post_res.next()) {
            showArea.appendText("Student:\n");
            showArea.appendText("Name: " + post_res.getString("name")+"\n");
            showArea.appendText("Last name: " + post_res.getString("last")+"\n");
            showArea.appendText("Middle name: " + post_res.getString("midle")+"\n");
            showArea.appendText("-----------"+"\n");
        }
    }


    @FXML
    void initialize()  {
        try {
        String post_select= "SELECT COUNT(*)\n" +
                            "FROM asks";
        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = null;

            post_res = prepSt.executeQuery();


        while(post_res.next()) {
            questionField.setText(post_res.getString("count"));
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String post_select= "SELECT MIN(mark), MAX(mark)\n" +
                    "FROM test_student";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            while(post_res.next()) {
                lbmin.setText("Minimal mark is -> " + post_res.getString("min"));
                lbmax.setText("Maximal mark is -> " + post_res.getString("max"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void getMaxWeek(ActionEvent actionEvent) throws SQLException {
        weekArea.setText(null);
        try {
            String post_select= "SELECT students.name, COUNT(*)\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY students.name\n" +
                    "HAVING COUNT(*)>=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM test_student\n" +
                    "\t\t\t\t\tGROUP BY students_id)\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            while(post_res.next()) {
                weekArea.appendText("Name - " + post_res.getString("name") + "\n");
                weekArea.appendText("Number of tests - " + post_res.getString("count"));
                weekArea.appendText("\n----------------\n");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void getMinWeek(ActionEvent actionEvent) {
        weekArea.setText(null);
        try {
            String post_select= "SELECT students.name, COUNT(*)\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY students.name\n" +
                    "HAVING COUNT(*)<=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM test_student\n" +
                    "\t\t\t\t\tGROUP BY students_id)\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            while(post_res.next()) {
                weekArea.appendText("Name - " + post_res.getString("name")+"\n");
                weekArea.appendText("Number of tests - " + post_res.getString("count"));
                weekArea.appendText("\n----------------\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMaxMonth(ActionEvent actionEvent) {
        monthArea.setText(null);
        try {
            String post_select= "SELECT askers.last, COUNT(*)\n" +
                    "FROM askers\n" +
                    "JOIN asks ON asks.askers_id = askers.id\n" +
                    "GROUP BY askers.last\n" +
                    "HAVING COUNT(*)>=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM asks\n" +
                    "\t\t\t\t\tGROUP BY askers_id)";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            while(post_res.next()) {
                monthArea.appendText("Name - " + post_res.getString("last") + "\n");
                monthArea.appendText("Number of asks - " + post_res.getString("count"));
                monthArea.appendText("\n-----------\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void getMinMonth(ActionEvent actionEvent) {
        monthArea.setText(null);
        try {
            String post_select= "SELECT askers.last, COUNT(*)\n" +
                    "FROM askers\n" +
                    "JOIN asks ON asks.askers_id = askers.id\n" +
                    "GROUP BY askers.last\n" +
                    "HAVING COUNT(*)<=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM asks\n" +
                    "\t\t\t\t\tGROUP BY askers_id)";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            while(post_res.next()) {
                monthArea.appendText("Name - " + post_res.getString("last")+"\n");
                monthArea.appendText("Number of asks - " + post_res.getString("count"));
                monthArea.appendText("\n-----------\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

