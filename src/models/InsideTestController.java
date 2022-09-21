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
import static sample.FXML.Home.test_id;

public class InsideTestController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<TestAsks> asksTable;

    @FXML
    private TableColumn<TestAsks, String> dateCol;

    @FXML
    private TableColumn<TestAsks, String> lastCol;

    @FXML
    private TableColumn<TestAsks, String> markCol;

    @FXML
    private TableColumn<TestAsks, String> nameCol;

    ObservableList<TestAsks> testAsksList = FXCollections.observableArrayList();

    TestAsks testAsks = null;

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
        testAsksList.clear();
        String query = "select students.name, students.last, mark, test_date\n" +
                "from test_student\n" +
                "join students on test_student.students_id = students.id\n" +
                "where test_id = ?";

        PreparedStatement pr = getConnect().prepareStatement(query);
        pr.setInt(1, test_id);

        ResultSet res = pr.executeQuery();

        while(res.next()) {
            String queryName = res.getString("name");
            String queryLast = res.getString("last");
            int queryMark = res.getInt("mark");
            Date queryDate = res.getDate("test_date");

            testAsksList.add(new TestAsks(queryName, queryLast, queryMark, queryDate));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        nameCol.setCellValueFactory(new PropertyValueFactory<TestAsks, String>("name"));
        lastCol.setCellValueFactory(new PropertyValueFactory<TestAsks, String>("last"));
        markCol.setCellValueFactory(new PropertyValueFactory<TestAsks, String>("mark"));
        dateCol.setCellValueFactory(new PropertyValueFactory<TestAsks, String>("date"));

        asksTable.setItems(testAsksList);}

}
