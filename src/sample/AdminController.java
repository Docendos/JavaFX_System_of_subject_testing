package sample;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.FXML.Page2;

import static sample.DbConn.getConnect;

public class AdminController {

    @FXML
    public javafx.scene.control.Button bt;
    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label label1;

    @FXML
    private Label label2;


    @FXML
    void home(ActionEvent event) throws IOException {
        loadPage("/sample/FXML/Home.fxml");
    }

    @FXML
    void page1(ActionEvent event) throws IOException{
        loadPage("/sample/FXML/Page1.fxml");
    }

    @FXML
    void page2(ActionEvent event) throws IOException{
        loadPage("/sample/FXML/tablePaneP2.fxml");
    }

    @FXML
    void page3(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/logTabPane.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    @FXML
    public void page4(ActionEvent actionEvent) throws IOException {
        loadPage("/sample/FXML/Page4.fxml");
    }

    @FXML
    public void page5(ActionEvent actionEvent) throws IOException {
        loadPage("/sample/FXML/Page5.fxml");
    }

    @FXML
    public void nxt(ActionEvent actionEvent) {
        Stage stage = (Stage) bt.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/FXML/AddAsk.fxml"));
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
    void initialize() throws IOException, SQLException {

        String post_select = "SELECT COUNT(*) AS count_students FROM students";
        PreparedStatement prepSt = getConnect().prepareStatement(post_select);
        ResultSet post_res = prepSt.executeQuery();
        while (post_res.next()) {
            label1.setText(String.valueOf(post_res.getInt("count_students")) + " students are registered");
        }


        String select = "SELECT COUNT(*) AS count_askers FROM askers";
        PreparedStatement prep = getConnect().prepareStatement(select);
        ResultSet res = prep.executeQuery();
        while (res.next()) {
            label2.setText(String.valueOf(res.getInt("count_askers"))+" askers are registered");
        }

        Parent root = null;

        root = FXMLLoader.load(getClass().getResource("/sample/FXML/Home.fxml"));
        bp.setCenter(root);
    }

    private void loadPage(String page) throws IOException {
        Parent root = null;

        root = FXMLLoader.load(getClass().getResource(page));
        bp.setCenter(root);
    }

}

