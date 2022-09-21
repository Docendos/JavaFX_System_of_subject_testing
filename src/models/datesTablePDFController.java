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

public class datesTablePDFController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<New_Creature> creaturesTable;

    @FXML
    private TableColumn<New_Creature, String> dateCol;

    @FXML
    private TableColumn<New_Creature, String> lastCol;

    @FXML
    private TableColumn<New_Creature, String> nameCol;

    ObservableList<New_Creature> CreaturesList = FXCollections.observableArrayList();

    New_Creature creature = null;

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
        String query = "SELECT last, name, asks_date\n" +
                "FROM askers A\n" +
                "JOIN asks ON asks.askers_id = A.id \n" +
                "WHERE asks_date >= ALL (SELECT asks_date\n" +
                "\t\t\t\t\t\t\tFROM asks\n" +
                "\t\t\t\t\t\t\tWHERE asks.askers_id = A.id)\n";

        PreparedStatement pr = getConnect().prepareStatement(query);
        ResultSet res = pr.executeQuery();

        while(res.next()) {
            String queryName = res.getString("name");
            String queryLast = res.getString("last");
            Date queryCount = res.getDate("asks_date");

            CreaturesList.add(new New_Creature(queryName, queryLast, queryCount));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        nameCol.setCellValueFactory(new PropertyValueFactory<New_Creature, String>("name"));
        lastCol.setCellValueFactory(new PropertyValueFactory<New_Creature, String>("last"));
        dateCol.setCellValueFactory(new PropertyValueFactory<New_Creature, String>("dat"));


        creaturesTable.setItems(CreaturesList);}
}

