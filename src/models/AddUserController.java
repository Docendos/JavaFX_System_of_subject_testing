package models;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.DbConn;
import java.util.logging.Level;
import static java.lang.Integer.parseInt;

public class AddUserController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    //@FXML
    //private TextField idFld;

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

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;
    private boolean update;
    int UserId;


    @FXML
    void clean() {
        //idFld.setText(null);
        nameFld.setText(null);
        lastNameFld.setText(null);
        middleNameFld.setText(null);
        userNameFld.setText(null);
        passwordFld.setText(null);
    }

    @FXML
    void save() {

        connection = DbConn.getConnect();
        //String id = idFld.getText();
        String name = nameFld.getText();
        String lastName = lastNameFld.getText();
        String middleName = middleNameFld.getText();
        String userName = userNameFld.getText();
        String password = passwordFld.getText();


        if (name.isEmpty() || lastName.isEmpty() || middleName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            System.out.println("An error occurred");
        } else {
            getQuery();
            insert();
            clean();

        }

    }



    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO askers(name, last, midle, login, password) VALUES (?,?,?,?,?)";
        } else {
            query = "UPDATE askers SET name=?, last=?, midle=?, login=?, password=? WHERE id = " + UserId;
        }
    }

    private void insert() {
        try {

            preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setInt(1, parseInt(idFld.getText()));
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

    @FXML
    void initialize() {


    }

    public void setTextField(int id, String name, String lastName, String middleName, String userName, String password) {
        UserId = id;
        //idFld.setText(String.valueOf(id));
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
