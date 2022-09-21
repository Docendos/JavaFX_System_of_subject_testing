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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.AddAskController;
import models.AddToTestController;
import models.AddUserController;
import models.User;

import static sample.DbConn.getConnect;

public class AsksAskersController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Back11Button;

    @FXML
    private TableColumn<Ask, String> answerCol;

    @FXML
    private TableView<Ask> asksTable;

    @FXML
    private TableColumn<Ask, String> authorCol;

    @FXML
    private TableColumn<Ask, String> dateCol;

    @FXML
    private TableColumn<Ask, String> editCol;

    @FXML
    private TableColumn<Ask, String> idCol;

    @FXML
    private TableColumn<Ask, String> levelCol;

    @FXML
    private TableColumn<Ask, String> subjectCol;

    @FXML
    private TableColumn<Ask, String> textCol;

    @FXML
    private Button toAskersButton;

    @FXML
    private DatePicker secondDate;

    @FXML
    private DatePicker firstDate;

    @FXML
    private TextArea showAsksArea;

    @FXML
    private Button showButton;

    @FXML
    public void show(ActionEvent actionEvent) throws SQLException {
        showAsksArea.setText(null);
        String post_select= "SELECT asks_text, asks_date, answers_text, askers.last\n" +
        "FROM asks\n" +
                "JOIN asks_answers USING(asks_id)\n" +
                "JOIN answers USING(answers_id)\n" +
                "JOIN subject USING(subject_id)\n" +
                "JOIN askers ON asks.askers_id = askers.id\n"+"WHERE rightness=1 " +
                "AND asks_date BETWEEN \'"+Date.valueOf(firstDate.getValue())+"\' AND \'" + Date.valueOf(secondDate.getValue())+"\'";
        PreparedStatement prepSt = getConnect().prepareStatement(post_select);

        ResultSet post_res = prepSt.executeQuery();

        int i = 0;
        while(post_res.next()) {
            i++;
            showAsksArea.appendText(i + ".\n" +"Ask: " + post_res.getString("asks_text")+"\n");
            showAsksArea.appendText( "Date: "+post_res.getDate("asks_date")+"\n");
            showAsksArea.appendText("Answer: " +post_res.getString("answers_text")+"\n");
            showAsksArea.appendText("Author: "+post_res.getString("last")+"\n");
            showAsksArea.appendText("---------------\n");
        }
    }


    @FXML
    public void toAskers(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) toAskersButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/AddAsk.fxml"));
        Parent root1 = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.show();
    }

    ObservableList<Ask> AskList = FXCollections.observableArrayList();


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Ask ask = null;

    @FXML
    void Action(ActionEvent event) throws IOException {
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
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/FXML/AddAskNormal.fxml"));
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
            AskList.clear();

            query = "SELECT asks_id, asks_text, asks_level, asks_date, subject_name, answers_text, askers.last\n" +
                    "FROM asks\n" +
                    "JOIN asks_answers USING(asks_id)\n" +
                    "JOIN answers USING(answers_id)\n" +
                    "JOIN subject USING(subject_id)\n" +
                    "JOIN askers ON asks.askers_id = askers.id\n"+"WHERE rightness=1";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                AskList.add(new  Ask(
                        resultSet.getInt("asks_id"),
                        resultSet.getString("asks_text"),
                        resultSet.getString("asks_level"),
                        resultSet.getDate("asks_date"),
                        resultSet.getString("subject_name"),
                        resultSet.getString("answers_text"),
                        resultSet.getString("last")));
                asksTable.setItems(AskList);

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
            throw new RuntimeException(e);
        }
    }

    private void loadDate() throws SQLException {
///////////////////////////////////////////////////////////////////////////////////////////////////
        connection = DbConn.getConnect();
        AskList.clear();
        query = "SELECT asks_id, asks_text, asks_level, asks_date, subject_name, answers_text, askers.last\n" +
                "FROM asks\n" +
                "JOIN asks_answers USING(asks_id)\n" +
                "JOIN answers USING(answers_id)\n" +
                "JOIN subject USING(subject_id)\n" +
                "JOIN askers ON asks.askers_id = askers.id\n" + "WHERE rightness=1";

        Statement statement = connection.createStatement();
        ResultSet queryOutput = statement.executeQuery(query);

        while(queryOutput.next()){
            Integer queryId = queryOutput.getInt("asks_id");
            String queryText = queryOutput.getString("asks_text");
            String queryLevel = queryOutput.getString("asks_level");
            Date queryDate = queryOutput.getDate("asks_date");
            String querySubject = queryOutput.getString("subject_name");
            String queryAnswer = queryOutput.getString("answers_text");
            String queryAuthor = queryOutput.getString("last");

            AskList.add(new Ask(queryId, queryText, queryLevel, queryDate, querySubject, queryAnswer, queryAuthor));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        idCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("id"));
        textCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("text"));
        levelCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("level"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("date"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("subject"));
        answerCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("answer"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Ask, String>("author"));

        asksTable.setItems(AskList);

        ////////////////////////////////////////////////
        Callback<TableColumn<Ask, String>, TableCell<Ask, String>> cellFoctory = new Callback<TableColumn<Ask, String>, TableCell<Ask, String>>() {
            @Override
            public TableCell<Ask, String> call(TableColumn<Ask, String> param) {
                // make cell containing buttons
                final TableCell<Ask, String> cell = new TableCell<Ask, String>() {
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
                            ///////////
                            FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);

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
                            addIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );

                            deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {

                                    try {
                                        ask = asksTable.getSelectionModel().getSelectedItem();
                                        query = "DELETE FROM asks WHERE asks_id  =" + ask.getId();
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

                                    ask = asksTable.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/AddAskNormal.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    AddAskController addAskController = loader.getController();
                                    addAskController.setUpdate(true);
                                    addAskController.setTextField(ask.getId(), ask.getText(),
                                            ask.getLevel(), ask.getDate(), ask.getSubject(), ask.getAnswer(),
                                            ask.getAuthor());
                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();


                                }
                            });

                            addIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    ask = asksTable.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/sample/FXML/AddToTest.fxml"));
                                    try {
                                        loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    AddToTestController addToTestController = loader.getController();
                                    addToTestController.getIDs(ask.getId(),  ask.getAnswer());

                                    Parent parent = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(parent));
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.show();
                                }
                            });

                            HBox managebtn = new HBox(addIcon, editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                            HBox.setMargin(addIcon, new Insets(2,2,0,3));

                            setGraphic(managebtn);

                            setText(null);

                        }
                    }

                };

                return cell;
            }
        };

        editCol.setCellFactory(cellFoctory);
        asksTable.setItems(AskList);

    }



}

