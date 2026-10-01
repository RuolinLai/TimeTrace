package TimeTrace;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class timeBarController {

    @FXML
    private HBox timebar_Hbox;

    private final String[] colorPool = {
        "#4e3a9f",
        "#dcd6f7",
        "#a08fe4",
        "#5e70f7",
        "#eda9c3"
    };

    private final Random random = new Random();

    public void loadTimeBar() {

        ArrayList<ArrayList<String>> rawIntervals =
                publicVariables.getTimeBarData();

        timebar_Hbox.getChildren().clear();

        if (rawIntervals == null || rawIntervals.isEmpty()) {
            return;
        }

        timebar_Hbox.setSpacing(0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");


        ArrayList<ArrayList<String>> timeIntervals = new ArrayList<>();

        for (int i = 0; i < rawIntervals.size(); i++) {

            ArrayList<String> current = rawIntervals.get(i);

            if (i > 0) {

                ArrayList<String> previous = rawIntervals.get(i - 1);

                LocalTime previousEnd =
                        LocalTime.parse(previous.get(2), formatter);

                LocalTime currentStart =
                        LocalTime.parse(current.get(1), formatter);

                if (previousEnd.isBefore(currentStart)) {

                    ArrayList<String> gap = new ArrayList<>();

                    gap.add("");
                    gap.add(previousEnd.format(formatter));
                    gap.add(currentStart.format(formatter));
                    gap.add("GAP");

                    timeIntervals.add(gap);
                }
            }

            timeIntervals.add(current);
        }


        long totalDurationSeconds = 0;

        long[] durations = new long[timeIntervals.size()];

        for (int i = 0; i < timeIntervals.size(); i++) {

            List<String> interval = timeIntervals.get(i);

            LocalTime start =
                    LocalTime.parse(interval.get(1), formatter);

            LocalTime end =
                    LocalTime.parse(interval.get(2), formatter);

            long duration =
                    ChronoUnit.SECONDS.between(start, end);

            durations[i] = duration;

            totalDurationSeconds += duration;
        }

        if (totalDurationSeconds <= 0) {
            return;
        }


        double containerHeight = timebar_Hbox.getPrefHeight();

        double barHeight =
                containerHeight > 50
                        ? containerHeight - 35
                        : 25;

        String previousColor = "";


        for (int i = 0; i < timeIntervals.size(); i++) {

            List<String> interval = timeIntervals.get(i);

            String taskName = interval.get(0);
            String startTimeStr = interval.get(1);
            String endTimeStr = interval.get(2);

            boolean isGap =
                    interval.size() > 3
                    && "GAP".equals(interval.get(3));

            double ratio =
                    (double) durations[i] / totalDurationSeconds;


            VBox segmentColumn = new VBox(2);


            double leftPadding =
                    timebar_Hbox.getPadding().getLeft();

            double rightPadding =
                    timebar_Hbox.getPadding().getRight();

            double usableWidth =
                    timebar_Hbox.widthProperty()
                            .subtract(leftPadding + rightPadding)
                            .multiply(ratio)
                            .doubleValue();



            segmentColumn.minWidthProperty().bind(
                    timebar_Hbox.widthProperty()
                            .subtract(leftPadding + rightPadding)
                            .multiply(ratio)
            );

            segmentColumn.prefWidthProperty().bind(
                    timebar_Hbox.widthProperty()
                            .subtract(leftPadding + rightPadding)
                            .multiply(ratio)
            );

            segmentColumn.maxWidthProperty().bind(
                    timebar_Hbox.widthProperty()
                            .subtract(leftPadding + rightPadding)
                            .multiply(ratio)
            );


            Label nameLabel = new Label(taskName);

            nameLabel.setStyle(
                    "-fx-font-size: 11px;" +
                    "-fx-text-fill: #333333;"
            );

            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setMaxWidth(Double.MAX_VALUE);

            nameLabel.setOpacity(0.0);


            Pane segment = new Pane();

            segment.prefWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            segment.minWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            segment.maxWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            segment.setPrefHeight(barHeight);

            segment.getStyleClass().add("time-segment");


            if (isGap) {

                segment.setStyle(
                        "-fx-background-color: #E0E0E0;"
                );

            } else {

                String randomColor;

                do {
                    randomColor =
                            colorPool[
                                    random.nextInt(colorPool.length)
                            ];
                }
                while (randomColor.equals(previousColor));

                previousColor = randomColor;

                segment.setStyle(
                        "-fx-background-color: "
                        + randomColor
                        + ";"
                );
            }


            if (i == 0 &&
                i == timeIntervals.size() - 1) {

                segment.getStyleClass()
                        .add("time-segment-only-one");

            } else if (i == 0) {

                segment.getStyleClass()
                        .add("time-segment-first");

            } else if (i == timeIntervals.size() - 1) {

                segment.getStyleClass()
                        .add("time-segment-last");
            }


            AnchorPane timeArea = new AnchorPane();

            timeArea.prefWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            timeArea.minWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            timeArea.maxWidthProperty().bind(
                    segmentColumn.widthProperty()
            );

            timeArea.setPrefHeight(20);


            Label startLabel = new Label(startTimeStr);

            startLabel.setStyle(
                    "-fx-font-size: 10px;" +
                    "-fx-text-fill: #666666;"
            );

            startLabel.setOpacity(0.0);

            AnchorPane.setLeftAnchor(
                    startLabel,
                    0.0
            );

            AnchorPane.setTopAnchor(
                    startLabel,
                    2.0
            );


            startLabel.translateXProperty().bind(
                    startLabel.widthProperty().negate()
            );



            Label endLabel = new Label(endTimeStr);

            endLabel.setStyle(
                    "-fx-font-size: 10px;" +
                    "-fx-text-fill: #666666;"
            );

            endLabel.setOpacity(0.0);

            AnchorPane.setLeftAnchor(
                    endLabel,
                    0.0
            );

            AnchorPane.setTopAnchor(
                    endLabel,
                    2.0
            );


            endLabel.translateXProperty().bind(
                    timeArea.widthProperty()
            );

            timeArea.getChildren().addAll(
                    startLabel,
                    endLabel
            );


            segmentColumn.setOnMouseEntered(e -> {

                if (!isGap && !taskName.isEmpty()) {
                    nameLabel.setOpacity(1.0);
                }

                startLabel.setOpacity(1.0);
                endLabel.setOpacity(1.0);
            });

            segmentColumn.setOnMouseExited(e -> {

                nameLabel.setOpacity(0.0);

                startLabel.setOpacity(0.0);
                endLabel.setOpacity(0.0);
            });



            segmentColumn.getChildren().addAll(
                    nameLabel,
                    segment,
                    timeArea
            );


            timebar_Hbox.getChildren()
                    .add(segmentColumn);
        }
    }

    @FXML
    private void initialize() {
        timebar_Hbox.setSpacing(0);
    }
}