package sample.FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AddUserController;
import models.Student_Info;
import sample.DbConn;

public class Page4_1 {


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student_Info student = null;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button SmthButton;

    @FXML
    private TableColumn<Student_Info, String> describeCol;

    @FXML
    private TableColumn<Student_Info, String> idCol;

    @FXML
    private TableColumn<Student_Info, String> lastNameCol;

    @FXML
    private TableColumn<Student_Info, String> loginCol;

    @FXML
    private TableColumn<Student_Info, String> middleNameCol;

    @FXML
    private TableColumn<Student_Info, String> nameCol;

    @FXML
    private TableColumn<Student_Info, String> passwordCol;

    @FXML
    private TableView<Student_Info> studentsTable;

    ObservableList<Student_Info> StudentList = FXCollections.observableArrayList();

    @FXML
    void refreshTable(MouseEvent event) {
        try {
            String quer = "UPDATE askers SET additional_column = 'Create max number of asks'\n" +
                    "WHERE askers.id \n" +
                    "IN (SELECT askers.id\n" +
                    "     FROM askers\n" +
                    "    JOIN asks ON asks.askers_id = askers.id\n" +
                    "   GROUP BY askers.id\n" +
                    "   HAVING COUNT(*)>=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM asks\n" +
                    "\t\t\t\t\tGROUP BY askers_id))";
            PreparedStatement pSt = connection.prepareStatement(quer);
            pSt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            StudentList.clear();

            query = "SELECT id, name, last, midle, login, password, additional_column FROM askers";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                StudentList.add(new Student_Info(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("last"),
                        resultSet.getString("midle"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("additional_column")));
                studentsTable.setItems(StudentList);

            }


        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void toAskers(ActionEvent event) {

    }

    @FXML
    void initialize() {
        try {
            connection = DbConn.getConnect();
            StudentList.clear();
            String quer = "UPDATE askers SET additional_column = 'Create max number of asks'\n" +
                    "WHERE askers.id \n" +
                    "IN (SELECT askers.id\n" +
                    "     FROM askers\n" +
                    "    JOIN asks ON asks.askers_id = askers.id\n" +
                    "   GROUP BY askers.id\n" +
                    "   HAVING COUNT(*)>=ALL(SELECT COUNT(*)\n" +
                    "\t\t\t\t\tFROM asks\n" +
                    "\t\t\t\t\tGROUP BY askers_id))";
            PreparedStatement pSt = connection.prepareStatement(quer);
            pSt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            loadDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDate() throws SQLException{
        connection = DbConn.getConnect();
        StudentList.clear();
        query = "select id, name, last, midle, login, password, additional_column  from askers";

        Statement statement = connection.createStatement();
        ResultSet queryOutput = statement.executeQuery(query);

        while(queryOutput.next()){
            Integer queryId = queryOutput.getInt("id");
            String queryName = queryOutput.getString("name");
            String queryLastName = queryOutput.getString("last");
            String queryMiddleName = queryOutput.getString("midle");
            String queryUserName = queryOutput.getString("login");
            String queryPassword = queryOutput.getString("password");
            String queryAdditional = queryOutput.getString("additional_column");

            StudentList.add(new Student_Info(queryId, queryName, queryLastName, queryMiddleName, queryUserName, queryPassword, queryAdditional));
        }


        idCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("lastName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("middleName"));
        loginCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("userName"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("password"));
        describeCol.setCellValueFactory(new PropertyValueFactory<Student_Info, String>("info"));

        studentsTable.setItems(StudentList);}

}

