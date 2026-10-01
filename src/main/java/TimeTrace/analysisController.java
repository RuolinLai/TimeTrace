package TimeTrace;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class analysisController {
    @FXML
    private Button aichat_button;

    @FXML
    private Label longest_distracted_task_label;

    @FXML
    private Label focus_rate_label;

    @FXML
    private PieChart time_spent_piechart;

    @FXML
    private BarChart<String,Number> distracted_times_barChart;

    @FXML
    private DatePicker date_picker;

    @FXML
    private GridPane task_gridPane;
    
    private menuController menuC;

    private String selected_date_str;

    public void setMenuController(menuController menuC) {
        this.menuC = menuC;
    }

    public void handleTaskDone() {
        if (menuC != null) {
            menuC.switchToScreen("task_done");
        }
    }

    private void handleDatePicker() {
        LocalDate selectedDate = date_picker.getValue();
        if (selectedDate != null) {
            
        } else {
            selectedDate = LocalDate.now();
        }
        selected_date_str = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    @FXML
    private void switchToAIchat(){
        menuC.switchToAIchat();
    }

    private ObservableList<PieChart.Data> convertToPieChartData(ArrayList<ArrayList<String>> rawData) {
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();

        for (ArrayList<String> item : rawData) {
            if (item.size() >= 2) {
                String label = item.get(0);
                double value = Double.parseDouble(item.get(1));
                
                dataList.add(new PieChart.Data(label, value));
            }
        }

        return dataList;
    }



    @FXML
    private void initialize() {

        tool.addSound(aichat_button, "click");

        RowConstraints headerRow = new RowConstraints();
        headerRow.setPrefHeight(140);
        task_gridPane.getRowConstraints().add(0, headerRow);

        distracted_times_barChart.setAnimated(false);
        distracted_times_barChart.getXAxis().setAnimated(false);
        distracted_times_barChart.getYAxis().setAnimated(false);

        date_picker.setValue(LocalDate.now());
        handleDatePicker();

        Platform.runLater(() -> {
            loadGridData();
        });

        date_picker.setOnAction(event -> {
            tool.playAudio("click");
            handleDatePicker();
            loadGridData();
        });
    }

    public void loadGridData() {
        Double focus_rate = 0.00;
        String longest_distracted_task = "";
        int longest_distracted_time = 0;
        //--------grid pane---------------------------------------
        task_gridPane.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            return rowIndex != null && rowIndex > 0;
        });

        task_gridPane.getRowConstraints().clear();

        ArrayList<ArrayList<String>> data = tool.getDataForChart("analysis_grid" , selected_date_str);
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
                for (int i = 0 ; i < fileItems.size()-2 ; i++) {
                    String item = fileItems.get(i);
                    if (i != 0){
                        item = tool.convertTimeToWords(item);
                    }
                    Label label = new Label(item);
                    label.setFont(javafx.scene.text.Font.font("System", 28));
                    label.setTextFill(javafx.scene.paint.Color.valueOf("#333333"));
                    label.setWrapText(true);
                    GridPane.setHalignment(label, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
                    label.setOnMouseClicked(event -> {
                        tool.playAudio("click");
                        handleRowClick(fileItems.get(4));
                    });
                    task_gridPane.add(label, colIndex, rowIndex);
                    colIndex++;
                }
                if (tool.getTotalSec(fileItems.get(1)) > longest_distracted_time){
                    longest_distracted_time = tool.getTotalSec(fileItems.get(1));
                    longest_distracted_task = fileItems.get(0);
                }
                focus_rate+=Double.parseDouble(fileItems.get(3));
                rowIndex++;
            }
        }
        focus_rate = (double)focus_rate/data.size();
        focus_rate = Math.round(focus_rate * 100.0) / 100.0;
        focus_rate_label.setText("Overall focus rate: " + String.valueOf(focus_rate));
        longest_distracted_task_label.setText("You get distracted the longest when you're doing " + longest_distracted_task);
        //-------------------------bar chart-------------
        distracted_times_barChart.getData().clear();
        distracted_times_barChart.setPrefWidth(846);
        ArrayList<ArrayList<String>> rawData = tool.getDataForChart("analysis_barchart" , selected_date_str);

        CategoryAxis xAxis = (CategoryAxis) distracted_times_barChart.getXAxis();
        xAxis.setLabel("Task name");
        xAxis.setTickLabelFont(javafx.scene.text.Font.font("System", 20));
        xAxis.setTickLabelsVisible(true);

        NumberAxis yAxis = (NumberAxis) distracted_times_barChart.getYAxis();
        yAxis.setLabel("Time get distracted");

        distracted_times_barChart.setTitle("The distracted time during each task");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Minutes");


        
        for (ArrayList<String> item : rawData) {
            String category = item.get(0);
            
            double value = Double.parseDouble((item.get(1))); 
            
            series.getData().add(new XYChart.Data<>(category, value));
        }

        distracted_times_barChart.getData().add(series);
        distracted_times_barChart.requestLayout();

        //-----------------pie chart--------------------
        ArrayList<ArrayList<String>> pie_rawData = tool.getDataForChart("analysis_piechart" , selected_date_str);
        ObservableList<PieChart.Data> pieChartData = convertToPieChartData(pie_rawData);

        time_spent_piechart.setData(pieChartData);
        time_spent_piechart.setTitle("Time distribution");
        // //-----------time bar----------------
        // if (timeBarContainerController != null) {
        //     timeBarContainerController.loadTimeBar();
        // }
        // else{
        //     System.out.println("load time bar failed");
        // }

    }


    private void handleRowClick(String filename) {
        List<String> fileLines = tool.readFileLines(filename);

        if (fileLines != null && !fileLines.isEmpty()) {
            taskInfo selectedTask = tool.fromFileLines(fileLines);

            if (selectedTask != null) {
                publicVariables.setCurrentTaskInfo(selectedTask);
                menuC.switchToTaskDone();

            }
        }
    }
}
