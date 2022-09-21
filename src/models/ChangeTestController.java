package models;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.Ask;
import sample.AskController;
import sample.DbConn;

import static sample.DbConn.getConnect;
import static sample.FXML.Home.test_id;

public class ChangeTestController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    NewAsk ask = null;

    @FXML
    private TableView<NewAsk> asksTable;

    @FXML
    private TableColumn<NewAsk, String> editCol;

    @FXML
    private TableColumn<NewAsk, String> idCol;

    @FXML
    private TableColumn<NewAsk, String> levelCol;

    @FXML
    private TableColumn<NewAsk, String> textCol;

    ObservableList<NewAsk> AskList = FXCollections.observableArrayList();

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void initialize() {
        try {
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(test_id);
    }

    private void loadDate() throws SQLException {
///////////////////////////////////////////////////////////////////////////////////////////////////
        //Connection connection = DbConn.getConnect();
        AskList.clear();
        String query = "SELECT asks_id, asks_text, asks_level\n" +
                "FROM asks\n" +
                "JOIN asks_answers USING(asks_id)\n" +
                "JOIN asks_answers_test USING(asks_answers_id)\n" +
                "JOIN the_test USING(test_id)\n" +
                "WHERE rightness=1 AND test_id=?";

        PreparedStatement pr = getConnect().prepareStatement(query);
        pr.setInt(1, test_id);

        ResultSet res = pr.executeQuery();

        while(res.next()) {
            Integer queryId = res.getInt("asks_id");
            String queryText = res.getString("asks_text");
            String queryLevel = res.getString("asks_level");

            AskList.add(new NewAsk(queryId, queryText, queryLevel));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////
        idCol.setCellValueFactory(new PropertyValueFactory<NewAsk, String>("id"));
        textCol.setCellValueFactory(new PropertyValueFactory<NewAsk, String>("text"));
        levelCol.setCellValueFactory(new PropertyValueFactory<NewAsk, String>("level"));


        asksTable.setItems(AskList);
        ////////////////////////////////////////////////
        Callback<TableColumn<NewAsk, String>, TableCell<NewAsk, String>> cellFoctory = new Callback<TableColumn<NewAsk, String>, TableCell<NewAsk, String>>() {
            @Override
            public TableCell<NewAsk, String> call(TableColumn<NewAsk, String> param) {
                // make cell containing buttons
                final TableCell<NewAsk, String> cell = new TableCell<NewAsk, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);


                            deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                            + "-glyph-size:28px;"
                                            + "-fx-fill:#ff1744;"
                            );


                            deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                 /*
                                    try {
                                        ask = asksTable.getSelectionModel().getSelectedItem();
                                        String query = "DELETE FROM asks WHERE asks_id  =" + ask.getId();
                                        Connection connection = getConnect();
                                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();


                                    } catch (SQLException ex) {
                                        Logger.getLogger(AskController.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                   */
                                }
                            });

                            HBox managebtn = new HBox(deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
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
        asksTable.setItems(AskList);
    }



}
