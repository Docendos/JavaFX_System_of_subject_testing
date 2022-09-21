package models;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.DbConn;

import static java.lang.Integer.parseInt;

public class AddStudentController {

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;
    private boolean update;
    int StudentId;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField lastNameFld;

    @FXML
    private TextField middleNameFld;

    @FXML
    private TextField nameFld;

    @FXML
    private TextField passwordFld;

    @FXML
    private TextField userNameFld;

    @FXML
    void clean() {
        nameFld.setText(null);
        lastNameFld.setText(null);
        middleNameFld.setText(null);
        userNameFld.setText(null);
        passwordFld.setText(null);
    }

    @FXML
    void save(MouseEvent event) {
        connection = DbConn.getConnect();
        String name = nameFld.getText();
        String lastName = lastNameFld.getText();
        String middleName = middleNameFld.getText();
        String userName = userNameFld.getText();
        String password = passwordFld.getText();


        if (name.isEmpty() || lastName.isEmpty() || middleName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            System.out.println("An error with AddStud occurred");
        } else {
            getQuery();
            insert();
            clean();

        }
    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFld.getText());
            preparedStatement.setString(2, lastNameFld.getText());
            preparedStatement.setString(3, middleNameFld.getText());
            preparedStatement.setString(4, userNameFld.getText());
            preparedStatement.setString(5, passwordFld.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO students(name, last, midle, login, password) VALUES (?,?,?,?,?)";
        } else {
            query = "UPDATE students SET name=?, last=?, midle=?, login=?, password=? WHERE id = " + StudentId;
        }
    }

    @FXML
    void initialize() {

    }

    public void setTextField( int id, String name, String lastName, String middleName, String userName, String password) {
        StudentId = id;
        nameFld.setText(name);
        lastNameFld.setText(lastName);
        middleNameFld.setText(middleName);
        userNameFld.setText(userName);
        passwordFld.setText(password);
    }

    public void setUpdate(boolean b) {
        this.update = b;
    }
}

