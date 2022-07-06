package com.example.congestioncontrol.NBPOutRouter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OutRouter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OutRouter.class.getResource("outRouter-view.fxml"));
        AnchorPane sourceLayout = fxmlLoader.load();
        Scene scene = new Scene(sourceLayout, 600.0, 350);
        stage.setTitle("NBP Out Router");
        stage.setScene(scene);
        stage.show();
    }
}
