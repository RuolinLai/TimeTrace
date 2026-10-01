package TimeTrace;

import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AIchatController {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox chatContainer;
    @FXML private TextField inputField;
    @FXML private Button sendButton;

    @FXML 
    public void initialize(){
        load();

        inputField.setOnAction(event -> {
            handleSendMessage();
        });

        tool.addSound(sendButton , "click");
    }

    public void load() {
        ArrayList<Object> taskList = publicVariables.data_for_AI;
        
        String taskDataSummary = formatTaskListToString(taskList);

        AiService.resetChat(taskDataSummary);
        chatContainer.getChildren().clear();

        chatContainer.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));
        
        appendMessage("AI", "Hi! I've load your Time Trace data, feel free to ask me anything about your task!");
    }

    private String formatTaskListToString(ArrayList<Object> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            System.out.println("-------------no data------------");
            return "No task data";
        }
        StringBuilder sb = new StringBuilder();
        for (Object task : taskList) {
            sb.append(task.toString()).append("\n");
        }
        System.out.println("data: " + sb.toString() + "============");
        return sb.toString();
    }

    @FXML
    private void handleSendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        appendMessage("I", userText);
        inputField.clear();
        inputField.setDisable(true);
        sendButton.setDisable(true);

        Task<String> aiTask = new Task<>() {
            @Override
            protected String call() {
                return AiService.getAiReading(userText);
            }
        };

        aiTask.setOnSucceeded(e -> {
            appendMessage("AI", aiTask.getValue());
            inputField.setDisable(false);
            sendButton.setDisable(false);
            inputField.requestFocus();
        });

        aiTask.setOnFailed(e -> {
            appendMessage("System", "Failed.");
            inputField.setDisable(false);
            sendButton.setDisable(false);
        });

        new Thread(aiTask).start();
    }

    private void appendMessage(String sender, String message) {
        Text senderText = new Text(sender + ":\n");
        senderText.setStyle("-fx-font-weight: bold; -fx-fill: " + (sender.equals("I") ? "#2b579a;" : "#65a37c;"));

        Text contentText = new Text(message);
        TextFlow textFlow = new TextFlow(senderText, contentText);
        textFlow.setPadding(new Insets(12, 12, 12, 12));
        textFlow.setStyle("-fx-background-color: " + (sender.equals("I") ? "#e1f5fe;" : "#f1f1f1;") + 
                          " -fx-background-radius: 8px;"+" -fx-font-size: 20px;" + "-fx-font-family: System");

        VBox box = new VBox(textFlow);
        box.setPadding(new Insets(2, 0, 2, 0));
        chatContainer.getChildren().add(box);
    }
}