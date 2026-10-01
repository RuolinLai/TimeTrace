package TimeTrace;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class menuController {
    @FXML
    private AnchorPane analysis_button;

    @FXML
    private AnchorPane setting_button;

    @FXML
    private AnchorPane home_button;    


    @FXML
    private AnchorPane contentPane;
    private final Map<String, Parent> screenMap = new HashMap<>();

    private analysisController analysisC;
    private homeController homeC;
    private taskDoneController taskDoneC;
    private AIchatController AIcharC;
    private settingController settingC;

    @FXML
    public void initialize() {
        try {
            publicVariables.setMainMenuController(this);

            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent homeView = homeLoader.load();
            homeC = homeLoader.getController();
            homeC.setMenuController(this);
            screenMap.put("home", homeView);

            FXMLLoader analysisLoader = new FXMLLoader(getClass().getResource("/fxml/analysis.fxml"));
            Parent analysisView = analysisLoader.load();
            analysisC = analysisLoader.getController();
            if (analysisC != null) {
                analysisC.setMenuController(this);
            }
            tool.addSound(home_button,"click");
            tool.addSound(analysis_button, "click");
            tool.addSound(setting_button, "click");
            screenMap.put("analysis", analysisView);

            screenMap.put("start_task", FXMLLoader.load(getClass().getResource("/fxml/start_task.fxml")));
            
            FXMLLoader AIchatLoader = new FXMLLoader(getClass().getResource("/fxml/AIchat.fxml"));
            Parent AIchatView = AIchatLoader.load();
            AIcharC = AIchatLoader.getController();
            screenMap.put("AIchat", AIchatView);

            FXMLLoader settingLoader = new FXMLLoader(getClass().getResource("/fxml/setting.fxml"));
            Parent settingView = settingLoader.load();
            settingC = settingLoader.getController();
            screenMap.put("setting", settingView);

            switchToHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transitionToTaskDoneViaAnalysis() {
        switchToAnalysis(); 
        if (analysisC != null) {
            analysisC.handleTaskDone();
        }
    }

    public void switchToScreen(String name) {
        loadScreen(name);
    }

    public void switchToTaskDone() {
        loadScreen("task_done");
        taskDoneC.load();
    }

    public void switchToAIchat(){
        loadScreen("AIchat");
        AIcharC.load();
    }
    

    @FXML
    private void switchToSetting(){
        loadScreen("setting");
        settingC.load();
    }
    

    @FXML
    private void switchToHome(){
        loadScreen("home");
        homeC.loadGridData();
    }

    @FXML
    private void switchToAnalysis(){
        loadScreen("analysis");
        analysisC.loadGridData();
    }

    private void loadScreen(String name) {
        Parent screen = screenMap.get(name);
        
        if (screen == null && name.equals("task_done")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task_done.fxml"));
                screen = loader.load();
                taskDoneC = loader.getController();
                screenMap.put("task_done", screen);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        if (screen != null) {
            contentPane.getChildren().setAll(screen);
            AnchorPane.setTopAnchor(screen, 0.0);
            AnchorPane.setBottomAnchor(screen, 0.0);
            AnchorPane.setLeftAnchor(screen, 0.0);
            AnchorPane.setRightAnchor(screen, 0.0);
        }
    }
}