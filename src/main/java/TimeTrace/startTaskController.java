package TimeTrace;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class startTaskController {
    @FXML
    private Label label;

    @FXML
    private TextField task_name;

    @FXML 
    private TextField input_min;

    @FXML 
    private TextField input_hour;

    @FXML 
    private Label label_h;

    @FXML 
    private Label label_min;

    private String str_task_name;

    private taskInfo new_task;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        task_name.textProperty().addListener((observable, oldValue, newValue) -> {
            hideErrorIfNotEmpty(newValue);
        });
        input_hour.textProperty().addListener((observable, oldValue, newValue) -> {
            hideErrorIfNotEmpty(newValue);
        });
        input_min.textProperty().addListener((observable, oldValue, newValue) -> {
            hideErrorIfNotEmpty(newValue);
        });

        input_hour.setOnAction(event -> handleTimeSubmit());
        input_min.setOnAction(event -> handleTimeSubmit());


        task_name.setOnAction(event -> {
            str_task_name = task_name.getText().trim();
            
            if (!str_task_name.isEmpty()) {
                task_name.setVisible(false);
                task_name.setManaged(false);
                label_h.setVisible(true);
                label_min.setVisible(true);
                input_min.setVisible(true);
                input_hour.setVisible(true);
                label.setText("Expected time needed:");
            } else {
                errorLabel.setText("Task name cannot be blank!");
                errorLabel.setVisible(true);
            }
        });
    }

    private void handleTimeSubmit() {
        String hourStr = input_hour.getText().trim();
        String minStr = input_min.getText().trim();

        if (tool.isPositiveInteger(hourStr) && tool.isPositiveInteger(minStr)) {
            String expectedTime = hourStr + ":" + minStr + ":00";
            String currentDate = LocalDate.now().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = LocalTime.now().format(formatter);
            new_task = new taskInfo(str_task_name , currentTime , null , expectedTime , null , currentDate);
            publicVariables.setCurrentTaskInfo(new_task);
            open_widget();
        } else {
            errorLabel.setText("Both inputs have to be positive integers!");
            errorLabel.setVisible(true);
        }
    }

    private void hideErrorIfNotEmpty(String value) {
        if (!value.trim().isEmpty()) {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
    }


    private void open_widget(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget.fxml"));
            Parent root = loader.load();

            Stage widgetStage = new Stage();
            widgetStage.initStyle(StageStyle.UNDECORATED);
            widgetStage.setScene(new Scene(root));
            widgetStage.setResizable(false); 
            widgetStage.show();

            Stage currentStage = (Stage) input_min.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
