package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane rootPane;



    @FXML
    public void submitFile(ActionEvent actionEvent) throws IOException {
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/paper_info_form.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Conference Management System");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    @FXML
    public void showAll(ActionEvent actionEvent) {
    }
}
