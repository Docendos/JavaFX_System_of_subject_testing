package sample.FXML;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

import static sample.DbConn.getConnect;

public class logTabPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea advancedLogShow;

    @FXML
    private TextArea normalLogShow;

    @FXML
    private Tab tab1Tab;


    @FXML
    private Tab tab2Tab;

    @FXML
    void initialize()  {
        try {
        normalLogShow.setText(null);
        String post_select= "SELECT * FROM logs";

        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = null;

            post_res = prepSt.executeQuery();


        int i = 0;
        while(post_res.next()) {
            i++;
            normalLogShow.appendText(i +". "+ post_res.getString("text")+" | ");
            normalLogShow.appendText( post_res.getTimestamp("added")+"\n");
        }
        normalLogShow.appendText("---------------\n");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            normalLogShow.setText(null);
            String post_select= "SELECT * FROM logs";

            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = null;

            post_res = prepSt.executeQuery();


            int i = 0;
            while(post_res.next()) {
                i++;
                normalLogShow.appendText(i +". "+ post_res.getString("text")+" | ");
                normalLogShow.appendText( post_res.getTimestamp("added")+"\n");
            }
            normalLogShow.appendText("---------------\n");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
        advancedLogShow.setText(null);
        String post_select= "SELECT * FROM students_history";

        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = prepSt.executeQuery();

        int i = 0;
        while(post_res.next()) {
            i++;
            advancedLogShow.appendText(i +". "+ post_res.getString("name")+" | ");
            advancedLogShow.appendText( post_res.getString("last")+" | ");
            advancedLogShow.appendText(post_res.getString("midle")+" | ");
            advancedLogShow.appendText(post_res.getString("login")+" | ");
            advancedLogShow.appendText(post_res.getString("password")+" | ");
            advancedLogShow.appendText(post_res.getTimestamp("start_date")+" | " + post_res.getTimestamp("end_date") + "\n");
        }
        advancedLogShow.appendText("---------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String post_select= "SELECT * FROM askers_history";

            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            int i = 0;
            while(post_res.next()) {
                i++;
                advancedLogShow.appendText(i +". "+ post_res.getString("name")+" | ");
                advancedLogShow.appendText( post_res.getString("last")+" | ");
                advancedLogShow.appendText(post_res.getString("midle")+" | ");
                advancedLogShow.appendText(post_res.getString("login")+" | ");
                advancedLogShow.appendText(post_res.getString("password")+" | ");
                advancedLogShow.appendText(post_res.getTimestamp("start_date")+" | " + post_res.getTimestamp("end_date") + "\n");
            }
            advancedLogShow.appendText("---------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            String post_select= "SELECT asks_text, asks_level, asks_date, start_date, end_date FROM asks_history";

            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            int i = 0;
            while(post_res.next()) {
                i++;
                advancedLogShow.appendText(i +". "+ post_res.getString("asks_text")+" | ");
                advancedLogShow.appendText( post_res.getString("asks_level")+" | ");
                advancedLogShow.appendText(post_res.getDate("asks_date")+" | ");
                advancedLogShow.appendText(post_res.getTimestamp("start_date")+" | " + post_res.getTimestamp("end_date") + "\n");
            }
            advancedLogShow.appendText("---------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            String post_select= "SELECT test_name, thema_name, start_date, end_date " +
                    "FROM the_test_history JOIN thema USING(thema_id)";

            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            int i = 0;
            while(post_res.next()) {
                i++;
                advancedLogShow.appendText(i +". "+ post_res.getString("test_name")+" | ");
                advancedLogShow.appendText( post_res.getString("thema_name")+" | ");
                advancedLogShow.appendText(post_res.getTimestamp("start_date")+" | " + post_res.getTimestamp("end_date") + "\n");
            }
            advancedLogShow.appendText("---------------\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
