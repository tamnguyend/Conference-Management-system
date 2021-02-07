package controller;

import dao.JdbcDao;
import entity.DTO.PaperDTO;
import entity.DTO.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Window;
import main.UserSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UploadPaperController {

    @FXML
    private Button closeButton;

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
        UserSession userSession = UserSession.getInstance("",0,"");
        int id = userSession.getUserId();
        String titleFieldText = titleField.getText();
        String abstractFieldText = abstractField.getText();
        String keywordFieldText = keywordField.getText();
        String subjectFieldText = subjectField.getText();
        List<Integer> authorIds = Arrays.asList(1, 2);
        PaperDTO paperDTO = new PaperDTO(userSession.getUserId(), titleFieldText,
                abstractFieldText, keywordFieldText, subjectFieldText, authorIds);

        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.upload(paperDTO);
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
}
