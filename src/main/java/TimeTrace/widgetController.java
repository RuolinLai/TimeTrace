package TimeTrace;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class widgetController {
    @FXML
    private Group zoomRoot;

    @FXML
    private Group reminder_group;

    @FXML
    private Rectangle no_button;

    @FXML
    private Rectangle yes_button;

    @FXML
    private Rectangle confirm_button;

    @FXML
    private ComboBox<String> choice_combobox;
    

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Group distracted_group;

    @FXML
    private Label distracted_stop_watch_label;

    @FXML
    private Label distracted_start_time_label;

    @FXML
    private Rectangle return_to_work_button;


    @FXML
    private Group what_happened_group;

    @FXML
    private Label task_name_label11;

    @FXML
    private Group stop_watch_group;

    @FXML
    private Label stop_watch_label;


    @FXML
    private Rectangle mark_button;

    @FXML
    private Rectangle done_button;

    @FXML
    private Label start_time_label;

    @FXML 
    Label task_name_label;


    private taskInfo current_taskInfo = publicVariables.getCurrentTaskInfo();


    //---------stop watch--------
    private timerController time_controller = new timerController(
            publicVariables.getReminderTimeInterval(),
            time -> stop_watch_label.setText(time),
            time -> distracted_stop_watch_label.setText(time),
            () -> {
                try {
                    tool.playAudio("reminder");
                    stop_watch_group.setVisible(false);
                    reminder_group.setVisible(true);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    );

    public void initialize() {
        choice_combobox.getItems().addAll(
            "Spacing out", 
            "Getting distracted by electronic devices", 
            "Playing with desk items", 
            "Taking a rest", 
            "Falling asleep",
            "Chatting with a neighbor"
        );
        choice_combobox.setEditable(true);
        setupMouseDragged();
        setupScrollZoom();
        task_name_label.setText(current_taskInfo.getTaskName());
        start_time_label.setText("start at: " + current_taskInfo.getStartTime());
        time_controller.enterStage();
        tool.addSound(done_button, "done");
        tool.addSound(mark_button, "click");
        tool.addSound(confirm_button, "click");
        tool.addSound(return_to_work_button, "click");


    
    }

    @FXML 
    private void taskDone(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();

            menuController newMenuC = loader.getController();

            Stage mainStage = new Stage();
            mainStage.setTitle("TimeTrace");
            mainStage.setScene(new Scene(root, 979, 639));
            mainStage.setResizable(false);
            mainStage.show();


            time_controller.handle_time_done();
            publicVariables.getCurrentTaskInfo().setDistractedTime(distracted_array);
            publicVariables.getCurrentTaskInfo().setEndTime(tool.getCurrentTime());
            publicVariables.getCurrentTaskInfo().calculateTimeDuration();
            publicVariables.getCurrentTaskInfo().calculateFocusRate();
            tool.addTask(publicVariables.getCurrentTaskInfo());

            Stage widgetStage = (Stage) done_button.getScene().getWindow();
            widgetStage.close();

            newMenuC.transitionToTaskDoneViaAnalysis();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private distractedTime distracted_time = new distractedTime();
    private ArrayList<distractedTime> distracted_array = new ArrayList<distractedTime>();

   @FXML 
    private void mark(){
        time_controller.handle_time_mark();
        stop_watch_group.setVisible(false);
        what_happened_group.setVisible(true);
        distracted_time.setStartTime(tool.getCurrentTime());
        distracted_start_time_label.setText("start at: " + tool.getCurrentTime());
    }

    @FXML
    private void confirm(){
        String userInput = choice_combobox.getValue();

        if (userInput == null || userInput.trim().isEmpty()) {
            userInput = choice_combobox.getEditor().getText();
        }

        distracted_time.setName(userInput);

        what_happened_group.setVisible(false);
        distracted_group.setVisible(true);
    }

    @FXML
    private void return_to_work(){
        distracted_time.setEndTime(tool.getCurrentTime());
        distracted_time.calTotalTime();
        distracted_array.add(distracted_time);
        time_controller.handle_time_return();
        distracted_group.setVisible(false);
        stop_watch_group.setVisible(true);
        distracted_time = new distractedTime();
    }

    @FXML 
    private void yes(){
        tool.playAudio("click");
        reminder_group.setVisible(false);
        stop_watch_group.setVisible(true);
        time_controller.isTaskRunning = false;
    }

    @FXML 
    private void no(){
        tool.playAudio("click");
        reminder_group.setVisible(false);
        mark();
    }




    private double deltaX;
    private double deltaY;
    private double iniX;
    private double iniY;

    private void setupMouseDragged() {
        
        rootPane.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                deltaX = e.getSceneX();
                deltaY = e.getSceneY();
                
                iniX = e.getScreenX();
                iniY = e.getScreenY();
            }
        });

        rootPane.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setX(e.getScreenX() - deltaX);
                stage.setY(e.getScreenY() - deltaY);
            }
        });

    }

    private double baseW;
    private double baseH;
    private double decoW;
    private double decoH;

    private void setupScrollZoom() {
        final double SCALE_FACTOR = 1.1;
        final double MIN_SCALE = 0.3;
        final double MAX_SCALE = 3.0;

        final double[] currentScale = {1.0};

        rootPane.setOnScroll(e -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();

            if (baseW == 0) {
                baseW = rootPane.getPrefWidth();   // 或者 rootPane.getWidth()
                baseH = rootPane.getPrefHeight();
                decoW = stage.getWidth() - stage.getScene().getWidth();
                decoH = stage.getHeight() - stage.getScene().getHeight();
            }

            if (e.getDeltaY() > 0) {
                currentScale[0] = Math.min(MAX_SCALE, currentScale[0] * SCALE_FACTOR);
            } else {
                currentScale[0] = Math.max(MIN_SCALE, currentScale[0] / SCALE_FACTOR);
            }

            Scale scale = new Scale(currentScale[0], currentScale[0], 0, 0);
            rootPane.getTransforms().setAll(scale);

            stage.setWidth(baseW * currentScale[0] + decoW);
            stage.setHeight(baseH * currentScale[0] + decoH);
        });
    }

    
}
