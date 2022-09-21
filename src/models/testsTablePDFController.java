package models;

import java.net.URL;
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
import static sample.FXML.Home.test_id;

public class testsTablePDFController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Creature> creaturesTable;

    @FXML
    private TableColumn<Creature, String> lastCol;

    @FXML
    private TableColumn<Creature, String> nameCol;

    @FXML
    private TableColumn<Creature, String> numberCol;

    ObservableList<Creature> CreaturesList = FXCollections.observableArrayList();

    Creature creature = null;

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
        String query = "SELECT students.name, students.last, COUNT(*)\n" +
                "FROM students\n" +
                "JOIN test_student ON students.id=test_student.students_id\n" +
                "GROUP BY students.name, students.last\n";

        PreparedStatement pr = getConnect().prepareStatement(query);
        ResultSet res = pr.executeQuery();

        while(res.next()) {
            String queryName = res.getString("name");
            String queryLast = res.getString("last");
            int queryCount = res.getInt("count");

            CreaturesList.add(new Creature(queryName, queryLast, queryCount));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        nameCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("name"));
        lastCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("last"));
        numberCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("count"));


        creaturesTable.setItems(CreaturesList);}

}