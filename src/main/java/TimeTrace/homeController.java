package TimeTrace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

public class homeController {
    @FXML
    private Button start_button;

    // @FXML 
    // private timeBarController timeBarContainerController;

    @FXML
    private GridPane task_gridPane;

    @FXML
    private HBox timeBarContainer;

    private menuController menuC;

    public void setMenuController(menuController menuC) {
        this.menuC = menuC;
    }

    private timeBarController timeBarC;

    @FXML
    private void handleStartButton() {
        if (menuC != null) {
            menuC.switchToScreen("start_task");
        }
    }

    @FXML
    private void initialize() {
        tool.addSound(start_button, "click");
        RowConstraints headerRow = new RowConstraints();
        headerRow.setPrefHeight(140);
        task_gridPane.getRowConstraints().add(0, headerRow);
    }

    public void loadGridData() {
        //--------grid pane---------------------------------------
        task_gridPane.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            return rowIndex != null && rowIndex > 0;
        });

        task_gridPane.getRowConstraints().clear();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = now.format(formatter);
        ArrayList<ArrayList<String>> data = tool.getDataForChart("home_grid" , formattedDate);
        if (data.isEmpty()){
            Label mergedLabel = new Label("Nothing's here!");
            mergedLabel.setWrapText(true);
            mergedLabel.setFont(javafx.scene.text.Font.font("System", 50));
            mergedLabel.setTextFill(javafx.scene.paint.Color.valueOf("#333333"));
            mergedLabel.setWrapText(true);
            GridPane.setHalignment(mergedLabel, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(mergedLabel, javafx.geometry.VPos.CENTER);

            task_gridPane.add(mergedLabel, 0, 1);

            GridPane.setColumnSpan(mergedLabel, 3);
        }
        else{
            int rowIndex = 1;
            for (ArrayList<String> fileItems : data) {
                
                RowConstraints row = new RowConstraints();
                row.setMinHeight(100);
                row.setPrefHeight(140);
                row.setMaxHeight(140);
                task_gridPane.getRowConstraints().add(row);

                int colIndex = 0;
                for (int i = 0 ; i < fileItems.size() ; i++) {
                    String item = fileItems.get(i);
                    if (i == 1){
                        item = tool.convertTimeToWords(item);
                    }
                    Label label = new Label(item);
                    label.setFont(javafx.scene.text.Font.font("System", 25));
                    label.setTextFill(javafx.scene.paint.Color.valueOf("#333333"));
                    label.setWrapText(true);
                    GridPane.setHalignment(label, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
                    task_gridPane.add(label, colIndex, rowIndex);
                    colIndex++;
                }
                rowIndex++;
            }
        }
    //---------------------time bar------------------------
        // if (timeBarContainerController != null) {
        //     timeBarContainerController.loadTimeBar();
        // }
        // else{
        //     System.out.println("load time bar failed");
        // }
    }
}
