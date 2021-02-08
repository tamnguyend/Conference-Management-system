package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.UserSession;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;

public class MainController {

    @FXML
    private GridPane mainPane;

    @FXML
    public void submitFile(ActionEvent actionEvent) throws IOException {
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/paper_info_form.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Conference Management System");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    @FXML
    public void showAll(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        UserSession userSession = new UserSession("",0,"");
        userSession.cleanUserSession();
        GridPane pane = FXMLLoader.load(getClass().getResource("/fxml/login_form.fxml"));

        mainPane.getChildren().setAll(pane);
    }
}
