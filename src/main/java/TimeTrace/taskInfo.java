package TimeTrace;

import java.util.ArrayList;

public class taskInfo {
    private String task_name;
    private String start_time;
    private String end_time;
    private String time_duration;
    private String expected_time;
    private ArrayList<distractedTime> distracted_time;
    private double focus_rate;
    private String date;


    public taskInfo(String tName , String sTime , String eTime , String exTime , ArrayList<distractedTime> disTime , String d){
        task_name = tName;
        start_time = sTime;
        end_time = eTime;
        expected_time = exTime;
        distracted_time = disTime;
        date = d;
    }

    public String getStartTime() { return start_time; }
    public String getEndTime() { return end_time; }
    public String getTimeDuration(){return time_duration;}
    public String getExpectedTime() { return expected_time; }
    public ArrayList<distractedTime> getDistractedTime() { return distracted_time; }
    public double getFocusRate() { return focus_rate;}
    public String getTaskName(){ return task_name;}
    public String getDate(){return date;}

    public void setStartTime(String st) { start_time = st; }
    public void setEndTime(String et) { end_time = et; }
    public void setTimeDuration(String td){ time_duration = td;}
    public void setExpectedTime(String et) { expected_time = et; }
    public void setDistractedTime(ArrayList<distractedTime> dt) { distracted_time = dt; }
    public void setFocusRate(double fr) { focus_rate = fr;}
    public void setTaskName(String tn){ task_name = tn;}
    public void setDate(String d){ date = d;}

    public String totalDistractedTime(ArrayList<distractedTime> dTime){
        int result = 0;
        for (int i = 0 ; i < dTime.size() ; i++) {
            int time = tool.getTotalSec(dTime.get(i).getTotalTime());
            result += time;
        }
        return tool.convertSecToHHMMSS(result);
    }

    public void calculateFocusRate(){
        focus_rate = (double) (tool.subtractToSec(time_duration, totalDistractedTime(distracted_time))) / tool.getTotalSec(time_duration);
        focus_rate = Math.round(focus_rate * 100.0) / 100.0;
    }

    public void calculateTimeDuration(){
        time_duration = tool.subtractToHHMMSS(end_time , start_time);
    }


    @Override 
    public String toString() {
        return String.format("%s%n%s%n%s%n%s%n%s%n%s%n%.2f%n%s",
                task_name, start_time, end_time, time_duration, expected_time, distracted_time, focus_rate, date);
    }
}
