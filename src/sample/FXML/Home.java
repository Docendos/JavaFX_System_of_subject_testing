package sample.FXML;

import java.awt.event.ActionEvent;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.*;
import sample.Ask;
import sample.AskController;
import sample.DbConn;
import sample.Test;

import static sample.DbConn.getConnect;

public class Home {


    public static int test_id;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Test test = null;

    @FXML
    private TableColumn<Test, String> editCol;

    @FXML
    private TableColumn<Test, String> idCol;

    @FXML
    private TableColumn<Test, String> nameCol;

    @FXML
    private TableView<Test> testsTable;

    @FXML
    private TableColumn<Test, String> themaCol;

    @FXML
    private TextField searchFld;


    ObservableList<Test> TestList = FXCollections.observableArrayList();

    @FXML
    public void getAddView(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/FXML/AddTest.fxml"));
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
    public void refreshTable() {

        try {
            TestList.clear();

            query = "SELECT test_id, test_name, thema_name \n" +
                    "FROM the_test\n" +
                    "JOIN thema USING(thema_id)";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                TestList.add(new Test(
                        resultSet.getInt("test_id"),
                        resultSet.getString("test_name"),
                        resultSet.getString("thema_name")));
                testsTable.setItems(TestList);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void initialize() throws SQLException {
        try {
            loadDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDate() throws SQLException{
        connection = getConnect();
        TestList.clear();
        query = "SELECT test_id, test_name, thema_name \n" +
                "FROM the_test\n" +
                "JOIN thema USING(thema_id)";

        Statement statement = connection.createStatement();
        ResultSet queryOutput = statement.executeQuery(query);

        while(queryOutput.next()){
            Integer queryId = queryOutput.getInt("test_id");
            String queryName = queryOutput.getString("test_name");
            String queryThema = queryOutput.getString("thema_name");


            TestList.add(new Test(queryId, queryName, queryThema));
        }


        idCol.setCellValueFactory(new PropertyValueFactory<Test, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Test, String>("text"));
        themaCol.setCellValueFactory(new PropertyValueFactory<Test, String>("thema"));

        testsTable.setItems(TestList);
///////////////////////////////////////////////////////////////////////////////////////
        /*
        FilteredList<Test> filteredData = new FilteredList<>(TestList, b->true);
        searchFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(test -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(test.getText().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(test.getThema().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Test> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(testsTable.comparatorProperty());

        testsTable.setItems(sortedData);*/
//DEAD AREA
/////////////////////////////////////////////////////////////////////////////////////
        Callback<TableColumn<Test, String>, TableCell<Test, String>> cellFoctory = new Callback<TableColumn<Test, String>, TableCell<Test, String>>() {
            @Override
            public TableCell<Test, String> call(TableColumn<Test, String> param) {
                // make cell containing buttons
                final TableCell<Test, String> cell = new TableCell<Test, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.BOOK);
                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            FontAwesomeIconView insideIcon = new FontAwesomeIconView(FontAwesomeIcon.TASKS);

                            showIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#00E676;"
                            );

                            editIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#00E676;"
                            );
                            deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );
                            insideIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );

                            showIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    test = testsTable.getSelectionModel().getSelectedItem();
                                    test_id = test.getId();
                                    //System.out.println(test_id);


                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/TesterController.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    ChangeTestController changeTestController = loader.getController();

                                    //changeTestController.getIdTest(test.getId());

                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();


                                }
                            });


                            insideIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    test = testsTable.getSelectionModel().getSelectedItem();
                                    test_id = test.getId();


                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/InsideTest.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }


                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();


                                }
                            });


                            editIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    test = testsTable.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/AddTest.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    AddTestController addTestController = loader.getController();
                                    addTestController.setUpdate(true);
                                    addTestController.setTextField(test.getId(),test.getText(), test.getThema());
                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();


                                }
                            });

                            deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    try {
                                        test = testsTable.getSelectionModel().getSelectedItem();
                                        query = "DELETE FROM the_test WHERE test_id  =" + test.getId();
                                        connection = DbConn.getConnect();
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();
                                        refreshTable();

                                    } catch (SQLException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }


                                }
                            });


                            HBox managebtn = new HBox(showIcon, insideIcon, editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(showIcon, new Insets(2, 3, 0, 2));
                            HBox.setMargin(insideIcon, new Insets(2, 3, 0, 2));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));

                            setGraphic(managebtn);

                            setText(null);

                        }
                    }

                };

                return cell;
            }
        };
        editCol.setCellFactory(cellFoctory);
        testsTable.setItems(TestList);

        FilteredList<Test> filteredData = new FilteredList<>(TestList, b->true);
        searchFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(test -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(test.getText().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(test.getThema().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Test> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(testsTable.comparatorProperty());

        testsTable.setItems(sortedData);


    }

}
