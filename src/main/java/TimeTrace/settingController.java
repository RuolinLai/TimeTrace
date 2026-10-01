package TimeTrace;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class settingController {
    @FXML
    private Label success_label;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField input_checkInterval;

    @FXML
    private TextField input_volume;

    public void load(){
        input_checkInterval.setText(String.valueOf(publicVariables.getReminderTimeInterval()));
        input_volume.setText(String.valueOf(publicVariables.getVolume() * 100));
        
        if (success_label != null) {
            success_label.setVisible(false);
        }
    }

    @FXML
    private void initialize() {
        input_checkInterval.textProperty().addListener((observable, oldValue, newValue) -> {
            hideErrorIfNotEmpty(newValue);
        });
        input_volume.textProperty().addListener((observable, oldValue, newValue) -> {
            hideErrorIfNotEmpty(newValue);
        });

        input_checkInterval.setOnAction(event -> handleTimeInterval());
        input_volume.setOnAction(event -> handleVolume());
    }

    private void handleTimeInterval() {
        String str = input_checkInterval.getText().trim();

        if (tool.isNonNegativeNumber(str)) {
            double num = Double.parseDouble(str);
            publicVariables.setReminderTimeInterval(num);
            
            showSuccessMessage();
        } else {
            errorLabel.setText("The interval has to be a non-negative number!");
            errorLabel.setManaged(true);
            errorLabel.setVisible(true);
        }
    }

    private void handleVolume() {
        String str = input_volume.getText().trim();

        if (tool.isNonNegativeNumber(str)) {
            double num = Double.parseDouble(str);
            publicVariables.setVolume(num / 100.0);
            
            showSuccessMessage();
        } else {
            errorLabel.setText("The volume has to be a non-negative number!");
            errorLabel.setManaged(true);
            errorLabel.setVisible(true);
        }
    }

    private void hideErrorIfNotEmpty(String value) {
        if (!value.trim().isEmpty()) {
            errorLabel.setVisible(false);
        }
    }

    private void showSuccessMessage() {
        if (success_label != null) {
            success_label.setVisible(true);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> success_label.setVisible(false));
            delay.play();
        }
    }
}