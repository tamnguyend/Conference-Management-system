package controller;

import dao.JdbcDao;
import entity.DTO.PaperDTO;
import entity.DTO.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Window;
import main.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.TextFields;

public class UploadPaperController implements Initializable {

    @FXML
    private Button uploadPaper;

    @FXML
    private TextField titleField;

    @FXML
    private TextField abstractField;

    @FXML
    private TextField keywordField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField associateAuthors;

    List<UserDTO> authorList;

    UserSession userSession = UserSession.getInstance("", 0, "");

    @FXML
    public void upload(ActionEvent actionEvent) {

        Window owner = uploadPaper.getScene().getWindow();

        if (titleField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter paper title");
            return;
        }
        if (abstractField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter abstract value");
            return;
        }

        if (keywordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your paper keyword");
            return;
        }
        if (subjectField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a subject paper");
            return;
        }
        if (associateAuthors.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a associate author");
            return;
        }

        int id = userSession.getUserId();
        String titleFieldText = titleField.getText();
        String abstractFieldText = abstractField.getText();
        String keywordFieldText = keywordField.getText();
        String subjectFieldText = subjectField.getText();

        List<Integer> authorIds = authorList.stream()
                .filter(i -> i.getEmail().equals(associateAuthors.getText()))
                .filter(i -> !i.getEmail().equals(userSession.getUserName()))
                .map(UserDTO::getUserId).collect(Collectors.toList());
        PaperDTO paperDTO = new PaperDTO(userSession.getUserId(), titleFieldText,
                abstractFieldText, keywordFieldText, subjectFieldText, authorIds);

        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.uploadPaper(paperDTO);
//        if (jdbcDao.registerUser(userDTO)) {
//            showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
//                    "Welcome user " + emailIdField.getText());
//            toMainPage();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setContentText("Do you want to login instead?");
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.YES) {
//                toMainPage();
//            } else {
//                // ... user chose CANCEL or closed the dialog
//            }
//        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JdbcDao dao = new JdbcDao();
        authorList = dao.getAllAuthor();
        List<String> authors = authorList
                .stream()
                .filter(author -> author.getEmail().equals(userSession.getUserName()))
                .map(UserDTO::getEmail).collect(Collectors.toList());
        TextFields.bindAutoCompletion(associateAuthors, authors);
    }
}
