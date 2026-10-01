package TimeTrace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;


public class taskDoneController {


    @FXML
    private PieChart time_distribution_piechart;

    @FXML
    private Label task_name_label;

    @FXML
    private Label time_spent_label;

    @FXML
    private Label expected_time_label;

    @FXML
    private Label start_at_label;

    @FXML
    private Label total_distracted_time_label;

    @FXML
    private Label times_of_distracted_label;

    @FXML
    private Label focus_rate_label;



    

    @FXML
    private void initialize() {
        load();
    }

    public void load(){
        taskInfo current_taskInfo = publicVariables.getCurrentTaskInfo();
        task_name_label.setText(current_taskInfo.getTaskName());
        time_spent_label.setText("Time spent: " + tool.convertTimeToWords(current_taskInfo.getTimeDuration()));
        expected_time_label.setText("Expected time: " + tool.convertTimeToWords(current_taskInfo.getExpectedTime()));
        start_at_label.setText("Started at: " + current_taskInfo.getStartTime());
        int total_dt = 0;
        for (int i = 0 ; i < current_taskInfo.getDistractedTime().size() ; i++){
            distractedTime dt = current_taskInfo.getDistractedTime().get(i);
            total_dt += tool.getTotalSec(dt.getTotalTime());
        }
        String total_dt_str = tool.convertTimeToWords(tool.convertSecToHHMMSS(total_dt));
        total_distracted_time_label.setText("Total distracted time: " + total_dt_str);
        times_of_distracted_label.setText("Times of distracted: " + current_taskInfo.getDistractedTime().size());
        focus_rate_label.setText("Focus rate: " + current_taskInfo.getFocusRate());

        //--------------piechart-------------------
        time_distribution_piechart.setTitle("Time distribution");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data(current_taskInfo.getTaskName(), tool.subtractToSec(current_taskInfo.getTimeDuration(),tool.convertSecToHHMMSS(total_dt)))
        );
        for (distractedTime dTime : current_taskInfo.getDistractedTime()){
            pieChartData.add(new PieChart.Data(dTime.getName() , tool.getTotalSec(dTime.getTotalTime())));
        }
        time_distribution_piechart.setData(pieChartData);
    }
}


