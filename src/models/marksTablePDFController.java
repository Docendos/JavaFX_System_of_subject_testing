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

public class marksTablePDFController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<NewCreature> creaturesTable;

    @FXML
    private TableColumn<NewCreature, String> lastCol;

    @FXML
    private TableColumn<NewCreature, String> nameCol;

    @FXML
    private TableColumn<NewCreature, String> numberCol;

    ObservableList<NewCreature> CreaturesList = FXCollections.observableArrayList();

    NewCreature creature = null;

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
        String query = "SELECT last, name, mark\n" +
                "FROM students S\n" +
                "JOIN test_student ON S.id = test_student.students_id\n" +
                "WHERE mark >= ALL (SELECT mark\n" +
                "\t\t\t\t   FROM test_student\n" +
                "\t\t\t\t   WHERE test_student.students_id = S.id)\n";

        PreparedStatement pr = getConnect().prepareStatement(query);
        ResultSet res = pr.executeQuery();

        while(res.next()) {
            String queryName = res.getString("name");
            String queryLast = res.getString("last");
            int queryCount = 0;
            int queryMark= res.getInt("mark");

            CreaturesList.add(new NewCreature(queryName, queryLast, queryCount, queryMark));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        nameCol.setCellValueFactory(new PropertyValueFactory<NewCreature, String>("name"));
        lastCol.setCellValueFactory(new PropertyValueFactory<NewCreature, String>("last"));
        numberCol.setCellValueFactory(new PropertyValueFactory<NewCreature, String>("mark"));


        creaturesTable.setItems(CreaturesList);}

}