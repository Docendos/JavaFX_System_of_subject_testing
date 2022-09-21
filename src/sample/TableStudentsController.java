package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.util.Callback;
import models.AddStudentController;
import models.AddUserController;
import models.Student;
import models.User;

public class TableStudentsController {

        String query = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = null;

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Button Back111Button;

        @FXML
        private TableColumn<Student, String> editCol;

        @FXML
        private TableColumn<Student, String> idCol;

        @FXML
        private TableColumn<Student, String> lastNameCol;

        @FXML
        private TableColumn<Student, String> loginCol;

        @FXML
        private TableColumn<Student, String> middleNameCol;

        @FXML
        private TableColumn<Student, String> nameCol;

        @FXML
        private TableColumn<Student, String> passwordCol;

        @FXML
        private TableView<Student> studentsTable;

        ObservableList<Student> StudentList = FXCollections.observableArrayList();
        @FXML
        void BackToAskers(ActionEvent event) {
                Stage stage = (Stage) Back111Button.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/AddAsk.fxml"));
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
                        Parent parent = FXMLLoader.load(getClass().getResource("/sample/FXML/AddStudent.fxml"));
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
        void refreshTable() {
                try {
                        StudentList.clear();

                        query = "SELECT id, name, last, midle, login, password FROM students";
                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()){
                                StudentList.add(new  Student(
                                        resultSet.getInt("id"),
                                        resultSet.getString("name"),
                                        resultSet.getString("last"),
                                        resultSet.getString("midle"),
                                        resultSet.getString("login"),
                                        resultSet.getString("password")));
                                studentsTable.setItems(StudentList);

                        }


                } catch (SQLException ex) {
                        Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        void initialize() {

                try {
                        loadDate();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        private void loadDate() throws SQLException{
                connection = DbConn.getConnect();
                StudentList.clear();
                query = "select id, name, last, midle, login, password from students";

                Statement statement = connection.createStatement();
                ResultSet queryOutput = statement.executeQuery(query);

                while(queryOutput.next()){
                        Integer queryId = queryOutput.getInt("id");
                        String queryName = queryOutput.getString("name");
                        String queryLastName = queryOutput.getString("last");
                        String queryMiddleName = queryOutput.getString("midle");
                        String queryUserName = queryOutput.getString("login");
                        String queryPassword = queryOutput.getString("password");

                        StudentList.add(new Student(queryId, queryName, queryLastName, queryMiddleName, queryUserName, queryPassword));
                }


                idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
                nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
                lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
                middleNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("middleName"));
                loginCol.setCellValueFactory(new PropertyValueFactory<Student, String>("userName"));
                passwordCol.setCellValueFactory(new PropertyValueFactory<Student, String>("password"));

                studentsTable.setItems(StudentList);
                //////
                Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
                        @Override
                        public TableCell<Student, String> call(TableColumn<Student, String> param) {
                                // make cell containing buttons
                                final TableCell<Student, String> cell = new TableCell<Student, String>() {
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
                                                                                student = studentsTable.getSelectionModel().getSelectedItem();
                                                                                query = "DELETE FROM students WHERE id  =" + student.getId();
                                                                                connection = DbConn.getConnect();
                                                                                preparedStatement = connection.prepareStatement(query);
                                                                                preparedStatement.execute();
                                                                                refreshTable();

                                                                        } catch (SQLException ex) {
                                                                                Logger.getLogger(TableStudentsController.class.getName()).log(Level.SEVERE, null, ex);
                                                                        }


                                                                }
                                                        });

                                                        editIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                @Override
                                                                public void handle(MouseEvent event) {

                                                                        student = studentsTable.getSelectionModel().getSelectedItem();
                                                                        FXMLLoader loader = new FXMLLoader();
                                                                        loader.setLocation(getClass().getResource("/sample/FXML/AddStudent.fxml"));
                                                                        try {
                                                                                loader.load();
                                                                        } catch (IOException ex) {
                                                                                Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                                                        }

                                                                        AddStudentController addStudentController = loader.getController();
                                                                        addStudentController.setUpdate(true);
                                                                        addStudentController.setTextField(student.getId(), student.getName(),
                                                                                student.getLastName(), student.getMiddleName(), student.getUserName(), student.getPassword());
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
                studentsTable.setItems(StudentList);
        }

}

