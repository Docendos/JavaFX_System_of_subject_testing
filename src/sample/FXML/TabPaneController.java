package sample.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class TabPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab tab1Tab;

    @FXML
    private Tab tab2Tab;

    @FXML
    void initialize() throws IOException {
        Parent root1 = null;
        root1 = FXMLLoader.load(getClass().getResource("/sample/FXML/Page2.fxml"));
        tab1Tab.setContent(root1);

        Parent root2 = null;
        root2 = FXMLLoader.load(getClass().getResource("/sample/FXML/Page2_1.fxml"));
        tab2Tab.setContent(root2);
    }



}

