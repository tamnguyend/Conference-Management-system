package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import dao.JdbcDao;
import entity.DTO.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

public class RegisterController {

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField universityField;

    @FXML
    private Button registerButton;

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

        if (firstNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your first name");
            return;
        }
        if (lastNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a last name");
            return;
        }
        if (universityField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a university");
            return;
        }

        if (rolesList.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please choose your role");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String university = universityField.getText();
        UserDTO userDTO = new UserDTO(emailId, password, firstName, lastName, university, rolesList.getValue().toString());

        JdbcDao jdbcDao = new JdbcDao();
        if (jdbcDao.registerUser(userDTO)) {
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

    public void clear(ActionEvent actionEvent) {
        emailIdField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        universityField.clear();
    }
}