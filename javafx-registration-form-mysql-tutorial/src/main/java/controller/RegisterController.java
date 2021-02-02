package controller;

import java.sql.SQLException;

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
import javafx.stage.Stage;
import javafx.stage.Window;

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
    public void register(ActionEvent event) throws SQLException {

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

        String emailId = emailIdField.getText();
        String password = passwordField.getText();
        Object role = rolesList.getValue();

        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.registerUser(emailId, password, role.toString());

        showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + emailIdField.getText());
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

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        JdbcDao jdbcDao = new JdbcDao();
        if (jdbcDao.checkLogin(emailId, password)) {
            try {
                System.out.println(getClass());
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_form.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Main Page");
                stage.setScene(new Scene(root, 800, 500));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setData() {
        ObservableList<String> roles = FXCollections.observableArrayList("Author", "Referee");
        rolesList.getItems().clear();
        rolesList.getItems().addAll(roles);
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