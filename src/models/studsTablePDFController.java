package models;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static sample.DbConn.getConnect;

public class studsTablePDFController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Stud> creaturesTable;

    @FXML
    private TableColumn<Stud, String> countCol;

    @FXML
    private TableColumn<Stud, String> lastCol;

    @FXML
    private TableColumn<Stud, String> nameCol;

    @FXML
    private TableColumn<Stud, String> textCol;

    ObservableList<Stud> CreaturesList = FXCollections.observableArrayList();

    Stud creature = null;

    @FXML
    void initialize() {
        try {
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadDate() throws SQLException {
///////////////////////////////////////////////////////////////////////////////////////////////////
        //Connection connection = DbConn.getConnect();
        CreaturesList.clear();
        String query = "SELECT name, last, MIN(mark) AS mark, 'With minimal mark ever got' AS text\n" +
                "FROM students\n" +
                "JOIN test_student ON students.id = test_student.students_id\n" +
                "GROUP BY name, last\n" +
                "UNION \n" +
                "SELECT name, last, MAX(mark) AS mark, 'With maximal mark ever got' AS text\n" +
                "FROM students\n" +
                "JOIN test_student ON students.id = test_student.students_id\n" +
                "GROUP BY name, last\n";

        PreparedStatement pr = getConnect().prepareStatement(query);
        ResultSet res = pr.executeQuery();

        while(res.next()) {
            String queryName = res.getString("name");
            String queryLast = res.getString("last");
            int queryCount = res.getInt("mark");
            String queryText = res.getString("text");

            CreaturesList.add(new Stud(queryName, queryLast, queryCount, queryText));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        nameCol.setCellValueFactory(new PropertyValueFactory<Stud, String>("name"));
        lastCol.setCellValueFactory(new PropertyValueFactory<Stud, String>("last"));
        countCol.setCellValueFactory(new PropertyValueFactory<Stud, String>("count"));
        textCol.setCellValueFactory(new PropertyValueFactory<Stud, String>("text"));

        creaturesTable.setItems(CreaturesList);}

}

