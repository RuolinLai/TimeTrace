package TimeTrace;

import java.util.ArrayList;

public class publicVariables {
    private static taskInfo currentTaskInfo;
    private static double reminder_time_interval = 10;
    private static ArrayList<ArrayList<String>> timeBarData;
    public static ArrayList<Object> data_for_AI = new ArrayList<>();
    private static double volume = 1;

    public static void setVolume(Double v){
        volume = v;
    }

    public static double getVolume(){
        return volume;
    }

    public static void setTimeBarData(ArrayList<ArrayList<String>> tD) {
        timeBarData = tD;
    }

    public static void setCurrentTaskInfo(taskInfo t) {
        currentTaskInfo = t;
    }

    public static taskInfo getCurrentTaskInfo() {
        return currentTaskInfo;
    }

    public static double getReminderTimeInterval() {
        return reminder_time_interval;
    }

    public static ArrayList<ArrayList<String>> getTimeBarData(){
        return timeBarData;
    }

    public static void setReminderTimeInterval(double interval) {
        reminder_time_interval = interval;
    }


    private static menuController mainMenuController;

    public static menuController getMainMenuController() {
        return mainMenuController;
    }

    public static void setMainMenuController(menuController controller) {
        mainMenuController = controller;
    }
}