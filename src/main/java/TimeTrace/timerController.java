package TimeTrace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class timerController {
    private final double xMinutes;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();

    private int timer1Seconds = 0;
    private int timer2Seconds = 0;
    private int timer3Seconds = 0;

    private boolean isTimer1Running = false;
    private boolean isTimer2Running = false;
    public boolean isTaskRunning = false;

    private final Consumer<String> onTimer1Update;
    private final Consumer<String> onTimer2Update;
    private final Runnable onTaskExecute;

    public timerController(double xMinutes, Consumer<String> onTimer1Update, Consumer<String> onTimer2Update, Runnable onTaskExecute) {
        this.xMinutes = xMinutes;
        this.onTimer1Update = onTimer1Update;
        this.onTimer2Update = onTimer2Update;
        this.onTaskExecute = onTaskExecute;
    }

    public void enterStage() {
        isTimer1Running = true;
        isTimer2Running = false;
        isTaskRunning = false;
        timer1Seconds = 0;
        timer2Seconds = 0;
        timer3Seconds = 0;

        scheduler.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private void tick() {
        if (isTimer1Running) {
            timer1Seconds++;
            
            if (!isTaskRunning) {
                timer3Seconds++;
                if (timer3Seconds >= xMinutes * 60) {
                    isTaskRunning = true;
                    timer3Seconds = 0;
                    executeTask(); 
                }
            }

            String timeStr = formatTime(timer1Seconds);
            javafx.application.Platform.runLater(() -> onTimer1Update.accept(timeStr));
        }

        if (isTimer2Running) {
            timer2Seconds++;
            String timeStr = formatTime(timer2Seconds);
            javafx.application.Platform.runLater(() -> onTimer2Update.accept(timeStr));
        }
    }

    public void handle_time_mark() {
        if (isTimer1Running) {
            isTimer1Running = false;
            isTimer2Running = true;
            timer2Seconds = 0;
            timer3Seconds = 0;
            isTaskRunning = false;
        }
    }

    public void handle_time_return() {
        if (isTimer2Running) {
            isTimer2Running = false;
            isTimer1Running = true;
            timer3Seconds = 0;
            isTaskRunning = false;
        }
    }

    public void handle_time_done() {
        isTimer1Running = false;
        isTimer2Running = false;
        scheduler.shutdown();
        taskExecutor.shutdown();
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d h %02d min %02d s", hours, minutes, seconds);
    }

    private void executeTask() {
        if (onTaskExecute != null) {
            taskExecutor.submit(() -> {
                onTaskExecute.run();
            });
        } else {
            isTaskRunning = false;
        }
    }
}