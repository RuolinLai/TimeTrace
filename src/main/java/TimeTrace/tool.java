package TimeTrace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class tool {
    public static boolean isNonNegativeNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("^\\d+(\\.\\d+)?$");
    }

    public static boolean isPositiveInteger(String str) {
        if (str == null) return false;
        return str.matches("^[0-9]\\d*$");
    }


    public static String getCurrentTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = now.format(formatter);
        return formattedDate;
    }

    public static ArrayList<String> getTask(String file_name){
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/" + file_name + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }



    public static void addTask(taskInfo newTask){
        try {
            String start_time = newTask.getStartTime().replace(":", "-");
            File file = new File("data/" + newTask.getDate() + "-" + start_time + ".txt");
            try (FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw)) {
                pw.print(newTask.toString());
            }

        } catch (IOException e) {
            System.out.println("file IO error: " + e);
        }
    }

    public static int subtractToSec(String t1, String t2) {
        String[] time1 = t1.split(":");
        String[] time2 = t2.split(":");
        
        int t1_h = Integer.parseInt(time1[0]);
        int t1_min = Integer.parseInt(time1[1]);
        int t1_sec = time1.length > 2 ? Integer.parseInt(time1[2]) : 0;
        
        int t2_h = Integer.parseInt(time2[0]);
        int t2_min = Integer.parseInt(time2[1]);
        int t2_sec = time2.length > 2 ? Integer.parseInt(time2[2]) : 0;
        
        int t1_total_secs = t1_h * 3600 + t1_min * 60 + t1_sec;
        int t2_total_secs = t2_h * 3600 + t2_min * 60 + t2_sec;
        
        return t1_total_secs - t2_total_secs;
    }

    public static String subtractToHHMMSS(String t1, String t2) {
        int diffSecs = Math.abs(tool.subtractToSec(t1, t2)); 
        
        int h = diffSecs / 3600;
        int m = (diffSecs % 3600) / 60;
        int s = diffSecs % 60;
        
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static int getTotalSec(String time){
        String[] tArray = time.split(":");
        int hour = Integer.parseInt(tArray[0]);
        int min = Integer.parseInt(tArray[1]);
        int sec = Integer.parseInt(tArray[2]);
        return (hour*3600 + min*60 + sec);
    }

    public static double getTotalMin(String time){
        double sec = (double)getTotalSec(time);
        double min = sec/60;
        min = Math.round(min * 100.0) / 100.0;
        return min;
    }

    public static int addToSec(String t1 , String t2){
        int a = tool.getTotalSec(t1);
        int b = tool.getTotalSec(t2);
        return a+b;
    }



    public static String convertSecToHHMMSS(int totalSeconds) {
        if (totalSeconds < 0) {
            totalSeconds = 0;
        }
        
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String convertTimeToWords(String time){
        String[] time1 = time.split(":");
        int h = Integer.parseInt(time1[0]);
        int min = Integer.parseInt(time1[1]);
        int sec = (time1.length > 2) ? Integer.parseInt(time1[2]) : 0;
        return  h + " h " + min + " min " + sec + " s";
    }


    public static taskInfo fromFileLines(List<String> lines) {
        if (lines == null || lines.size() < 8) {
            return null;
        }

        String taskName = lines.get(0);
        String startTime = lines.get(1);
        String endTime = lines.get(2);
        String timeDuration = lines.get(3);
        String expectedTime = lines.get(4);
        String distractedStr = lines.get(5);
        double focusRate = Double.parseDouble(lines.get(6));
        String date = lines.get(7);

        ArrayList<distractedTime> distractedList = parseDistractedTime(distractedStr);

        taskInfo task = new taskInfo(taskName, startTime, endTime, expectedTime, distractedList, date);
        
        task.setTimeDuration(timeDuration);
        task.setFocusRate(focusRate);

        return task;
    }

    private static ArrayList<distractedTime> parseDistractedTime(String lineText) {
        ArrayList<distractedTime> list = new ArrayList<>();
        if (lineText == null || lineText.equals("[]") || lineText.length() <= 2) {
            return list;
        }

        String trimmed = lineText.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }

        String[] innerLists = trimmed.split("\\],\s*\\[");
        for (String inner : innerLists) {
            String[] parts = inner.split(",");
            if (parts.length >= 4) {
                String name = parts[0].replace("[", "").trim();
                String start = parts[1].trim();
                String end = parts[2].trim();
                String total = parts[3].replace("]", "").trim();
                
                distractedTime dt = new distractedTime(name, start, end, total);
                list.add(dt);
            }
        }
        return list;
    }

    public static List<String> readFileLines(String filename) {
        String folderPath = "data/"; 
        
        Path path = Paths.get(folderPath + filename);
        
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static ArrayList<ArrayList<String>> getDataForChart(String type , String date) {
        ArrayList<ArrayList<String>> allFilesData = new ArrayList<>();
        
        String todayStr = date;
        Path folderPath = Paths.get("data/");
        ArrayList<ArrayList<String>> timebar_data = new ArrayList<>();


        try (Stream<Path> paths = Files.list(folderPath)) {
            List<Path> targetFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().startsWith(todayStr))
                    .sorted()
                    .toList();
            publicVariables.data_for_AI.clear();
            for (Path file : targetFiles) {
                List<String> lines = Files.readAllLines(file);
                ArrayList<String> fileItems = new ArrayList<>();
                taskInfo new_TaskInfo = fromFileLines(lines);
                fileItems.add(new_TaskInfo.getTaskName());//name of the task

                // ArrayList<String> tempArray = new ArrayList<>();
                // tempArray.add(new_TaskInfo.getTaskName());
                // tempArray.add(new_TaskInfo.getStartTime());
                // tempArray.add(new_TaskInfo.getEndTime());
                // timebar_data.add(tempArray);


                publicVariables.data_for_AI.add(new_TaskInfo);


                if (type.equals("analysis_grid")){
                    fileItems.add(new_TaskInfo.totalDistractedTime(new_TaskInfo.getDistractedTime()));
                    fileItems.add(new_TaskInfo.getTimeDuration());
                    fileItems.add(String.valueOf(new_TaskInfo.getFocusRate()));
                    fileItems.add(new_TaskInfo.getDate()+"-"+new_TaskInfo.getStartTime().replace(":", "-")+".txt");

                }
                else if (type.equals("analysis_barchart")){
                    fileItems.add(String.valueOf(tool.getTotalMin(new_TaskInfo.totalDistractedTime(new_TaskInfo.getDistractedTime()))));
                }
                else if (type.equals("analysis_piechart")){
                    fileItems.add(String.valueOf(tool.getTotalSec(new_TaskInfo.getTimeDuration())));
                }
                else if (type.equals("home_grid")){
                    fileItems.add(new_TaskInfo.getTimeDuration());
                    fileItems.add(String.valueOf(new_TaskInfo.getFocusRate()));
                }


                allFilesData.add(fileItems);
            }
            publicVariables.setTimeBarData(timebar_data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allFilesData;
    }



    public static void playAudio(String audioFileName) {
        try {
            String audioPath = "/media/" + audioFileName + ".mp3";
            var resource = tool.class.getResource(audioPath);
            
            if (resource == null) {
                System.err.println("找不到音频文件: " + audioPath);
                return;
            }
            
            Media media = new Media(resource.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            
            mediaPlayer.setVolume(publicVariables.getVolume()); 
            
            mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
            mediaPlayer.play();
            
        } catch (Exception e) {
            System.err.println("播放音频失败: " + e.getMessage());
        }
    }
    public static void addSound(Node node, String audioFileName) {
        try {
            String audioPath = "/media/" + audioFileName + ".mp3";
            var resource = tool.class.getResource(audioPath);
            
            if (resource == null) {
                System.err.println("找不到音频文件: " + audioPath);
                return;
            }
            
            String mediaUrl = resource.toExternalForm();

            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    Media media = new Media(mediaUrl);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(publicVariables.getVolume());
                    mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
                    mediaPlayer.play();
                } catch (Exception e) {
                    System.err.println("failed to play audio: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("audio loading error: " + e.getMessage());
        }
    }

}
