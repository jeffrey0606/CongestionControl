package com.example.congestioncontrol.NBPSource;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Source extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Source.class.getResource("source-view.fxml"));
        AnchorPane sourceLayout = fxmlLoader.load();
        Scene scene = new Scene(sourceLayout, 600.0, 610);
        stage.setTitle("NBP Source");
        stage.setScene(scene);
        stage.show();
    }
}
