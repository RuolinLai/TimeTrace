package TimeTrace;

public class distractedTime {
    private String name;
    private String start_time;
    private String end_time;
    private String total_time;

    public distractedTime(){

    }

    public distractedTime(String n , String st , String et){
        name = n;
        start_time = st;
        end_time = et;
        total_time = tool.subtractToHHMMSS(end_time, start_time);
    }

    public distractedTime(String n , String st , String et , String tt){
        name = n;
        start_time = st;
        end_time = et;
        total_time = tt;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public String getTotalTime(){
        return total_time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
        calTotalTime();
    }

    public void calTotalTime(){
        total_time = tool.subtractToHHMMSS(end_time, start_time);
    }


    @Override 
    public String toString(){
        return String.format("[%s, %s, %s, %s]", 
            name, start_time, end_time, total_time);
    }
}