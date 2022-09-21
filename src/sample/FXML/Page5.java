package sample.FXML;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.DbConn;

import static sample.DbConn.getConnect;

public class Page5 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button findButton;

    @FXML
    private TextField firstMarkButton;

    @FXML
    private TextField secondMarkButton;

    @FXML
    private TextArea showArea;

    @FXML
    private Button showButton;

    @FXML
    void find(ActionEvent event) throws SQLException {
        showArea.setText(null);
        String post_select= "SELECT last, name\n" +
                "FROM askers\n" +
                "LEFT JOIN (SELECT askers_id\n" +
                "           FROM asks\n" +
                "\t\t   JOIN askers ON asks.askers_id = askers.id\n" +
                "           WHERE asks_date >= current_date-7) A\n" +
                "ON A.askers_id = askers.id\n" +
                "WHERE A.askers_id is NULL\n";
        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = prepSt.executeQuery();

        while(post_res.next()) {
            showArea.appendText("Asker:\n");
            showArea.appendText("Name: " + post_res.getString("name")+"\n");
            showArea.appendText("Last name: " + post_res.getString("last")+"\n");
            showArea.appendText("-----------"+"\n");
        }
    }

    @FXML
    void show(ActionEvent event) throws SQLException {

        int firstMark = Integer.parseInt(firstMarkButton.getText());
        int secondMark = Integer.parseInt(secondMarkButton.getText());

        showArea.setText(null);
        String quer= "SELECT last, name\n" +
                "FROM students\n" +
                "LEFT JOIN (SELECT id\n" +
                "\t       FROM students\n" +
                "\t\t   JOIN test_student ON students.id=test_student.students_id\n" +
                "\t\t   WHERE mark BETWEEN ? AND ?) X\n" +
                "USING(id)\n" +
                "WHERE X.id is NULL\n";

        Connection connection = DbConn.getConnect();
        PreparedStatement pSt = connection.prepareStatement(quer);
        pSt.setInt(1, firstMark);
        pSt.setInt(2, secondMark);

        ResultSet post_res = pSt.executeQuery();;

        while(post_res.next()) {
            showArea.appendText("Student:\n");
            showArea.appendText("Name: " + post_res.getString("name")+"\n");
            showArea.appendText("Last name: " + post_res.getString("last")+"\n");
            showArea.appendText("-----------"+"\n");
        }
    }

    @FXML
    void initialize() {
    }

}
