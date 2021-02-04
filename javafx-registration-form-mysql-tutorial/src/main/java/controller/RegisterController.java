package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import dao.JdbcDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.plugin.javascript.navig.Anchor;

public class RegisterController {

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private ChoiceBox rolesList;

    @FXML
    private GridPane rootPane;

    @FXML
    public void register(ActionEvent event) throws SQLException, IOException {

        Window owner = registerButton.getScene().getWindow();

        System.out.println(emailIdField.getText());
        System.out.println(passwordField.getText());

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }
        Object role = rolesList.getValue();
        if (role == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please choose your role");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();


        JdbcDao jdbcDao = new JdbcDao();
        if (jdbcDao.registerUser(emailId, password, role.toString())) {
            showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                    "Welcome user " + emailIdField.getText());
            toMainPage();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to login instead?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                toMainPage();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    @FXML
    public void login(ActionEvent event) throws SQLException {

        Window owner = loginButton.getScene().getWindow();

        System.out.println(emailIdField.getText());
        System.out.println(passwordField.getText());

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }
        Object role = rolesList.getValue();
        if (role == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please choose the role you want to login to the Application");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        JdbcDao jdbcDao = new JdbcDao();
        if (jdbcDao.checkLogin(emailId, password, role.toString())) {
            try {
                System.out.println(getClass());
                toMainPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, owner, "Authentication Fail",
                    "Please input your credential again !!!");
        }

    }

    public void toMainPage() throws IOException {
        GridPane pane = FXMLLoader.load(getClass().getResource("/fxml/main_form.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}