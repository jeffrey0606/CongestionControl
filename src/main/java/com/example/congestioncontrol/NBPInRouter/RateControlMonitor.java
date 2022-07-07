package com.example.congestioncontrol.NBPInRouter;

import com.example.congestioncontrol.Toaster;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;

public class RateControlMonitor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private XYChart.Series series;

    public static StackPane root;

    public static ArrayList<RateSeriesDataModel> seriesData = new ArrayList<>();
    public static ObservableList<XYChart.Data<Number, Number>> seriesDataObservableList;

    public static void add(RateSeriesDataModel seriesDataModel, boolean fromInit) {
        XYChart.Data data = new XYChart.Data(seriesDataModel.x(), seriesDataModel.y());
        Number x = seriesDataModel.x();
//        if(!fromInit) {
//            seriesData.add(x.intValue() - 1, seriesDataModel);
//        }
//        System.out.println("data: " + data);
        Platform.runLater(() -> {
            seriesDataObservableList.add(x.intValue() - 2, data);
        });
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RateControl Line Chart");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(100);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        xAxis.setLabel("Number of Packets Send");
        yAxis.setLabel("Rate At InRouter");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);


        lineChart.setTitle("Rate Control Monitoring, " + new Date().toLocaleString());
        //defining a series
//        yAxis.setTickUnit(0.25);
        series = new XYChart.Series();

        series.setName("Packet Transfer Rate");
//        series.getData().clear();

        if(seriesDataObservableList == null) {
            seriesDataObservableList = series.getData();
        } else {
            int len = seriesDataObservableList.size();
            System.out.println("len: " + len);
            for (int i = 0; i < len; i++) {
                XYChart.Data<Number, Number> data = seriesDataObservableList.get(i);
                XYChart.Data newData = new XYChart.Data(data.getXValue(), data.getYValue());

                series.getData().add(i, newData);
            }
            seriesDataObservableList.clear();
            seriesDataObservableList = series.getData();
        }

//        series.setData(seriesDataObservableList);
        //populating the series with data


//        seriesDataObservableList.addListener(new ListChangeListener<XYChart.Data<Number, Number>>() {
//            @Override
//            public void onChanged(Change<? extends XYChart.Data<Number, Number>> change) {
//
//            }
//        });
//        for (int i = 0; i <seriesData.size(); i++) {
//
//            add(seriesData.get(i), true);
//        }

//        seriesDataObservableList.add(new XYChart.Data(1, 23));
//        seriesDataObservableList.add(new XYChart.Data(2, 14));
//        seriesDataObservableList.add(new XYChart.Data(3, 15));
//        series.getData().add(new XYChart.Data(4, 24));
//        series.getData().add(new XYChart.Data(5, 34));
//        series.getData().add(new XYChart.Data(6, 36));
//        series.getData().add(new XYChart.Data(7, 22));
//        series.getData().add(new XYChart.Data(8, 45));
//        series.getData().add(new XYChart.Data(9, 43));
//        series.getData().add(new XYChart.Data(10, 17));
//        series.getData().add(new XYChart.Data(11, 29));
//        series.getData().add(new XYChart.Data(12, 25));

        StackPane root = new StackPane();
        this.root = root;
        root.getChildren().add(lineChart);

        Scene scene = new Scene(root, 1224, 600);


        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }
}
