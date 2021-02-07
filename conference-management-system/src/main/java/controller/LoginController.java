package controller;

import dao.JdbcDao;
import entity.DTO.UserResponseDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import main.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

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
        toRegistrationPage();
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
        UserResponseDTO userResponseDTO = jdbcDao.checkLogin(emailId, password);
        if (userResponseDTO != null) {
            try {
                UserSession.getInstance(userResponseDTO.getUsername(),userResponseDTO.getUserId(),userResponseDTO.getRole());
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

    public void toRegistrationPage() throws IOException {
        GridPane pane = FXMLLoader.load(getClass().getResource("/fxml/registration_form.fxml"));
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