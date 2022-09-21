package sample;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.logging.Level;
import javafx.geometry.Insets;

import javafx.util.Callback;
import models.AddUserController;
import models.User;

import static java.lang.Integer.parseInt;

public class AskController {


    @FXML
    private TableView<User> usersTable;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Back11Button;

    @FXML
    private Button toTestersButton;
    @FXML
    private TableColumn<User, String> editCol;

    @FXML
    private TableColumn<User, String> idCol;

    @FXML
    private TableColumn<User, String> lastNameCol;

    @FXML
    private TableColumn<User, String> loginCol;

    @FXML
    private TableColumn<User, String> middleNameCol;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> passwordCol;


    ObservableList<User> UserList = FXCollections.observableArrayList();


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;

    @FXML
    void Action(ActionEvent event) {
        Stage stage = (Stage) Back11Button.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/sample.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/FXML/AddUser.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void print(MouseEvent event) {

    }


    @FXML
    void ToTesters(ActionEvent event) {
        Stage stage = (Stage) toTestersButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/TableStudents.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }
    @FXML
    void refreshTable() {
        try {
            UserList.clear();

            query = "SELECT id, name, last, midle, login, password FROM askers";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                UserList.add(new  User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("last"),
                        resultSet.getString("midle"),
                        resultSet.getString("login"),
                        resultSet.getString("password")));
                usersTable.setItems(UserList);

            }


        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Action() throws IOException {
        Stage stage = (Stage) Back11Button.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/sample.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }







    @FXML
    void initialize() {
        try {
            loadDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void loadDate() throws SQLException {
        connection = DbConn.getConnect();
        UserList.clear();
        query = "select id, name, last, midle, login, password from askers";

        Statement statement = connection.createStatement();
        ResultSet queryOutput = statement.executeQuery(query);

        while(queryOutput.next()){
            Integer queryId = queryOutput.getInt("id");
            String queryName = queryOutput.getString("name");
            String queryLastName = queryOutput.getString("last");
            String queryMiddleName = queryOutput.getString("midle");
            String queryUserName = queryOutput.getString("login");
            String queryPassword = queryOutput.getString("password");

            UserList.add(new User(queryId, queryName, queryLastName, queryMiddleName, queryUserName, queryPassword));
        }


        idCol.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("middleName"));
        loginCol.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<User, String>("password"));

        usersTable.setItems(UserList);
        ///////////////////
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFoctory = new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                // make cell containing buttons
                final TableCell<User, String> cell = new TableCell<User, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                            deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );
                            editIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#00E676;"
                            );
                            deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    try {
                                        user = usersTable.getSelectionModel().getSelectedItem();
                                        query = "DELETE FROM askers WHERE id  =" + user.getId();
                                        connection = DbConn.getConnect();
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();
                                        refreshTable();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }


                                }
                            });

                            editIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    user = usersTable.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/AddUser.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    AddUserController addUserController = loader.getController();
                                    addUserController.setUpdate(true);
                                    addUserController.setTextField(user.getId(), user.getName(),
                                            user.getLastName(), user.getMiddleName(), user.getUserName(), user.getPassword());
                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();


                                }
                            });

                            HBox managebtn = new HBox(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                            setGraphic(managebtn);

                            setText(null);

                        }
                    }

                };

                return cell;
            }
        };
        editCol.setCellFactory(cellFoctory);
        usersTable.setItems(UserList);

    }

}

