package com.example.congestioncontrol.NBPInRouter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class InRouter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InRouter.class.getResource("inRouter-view.fxml"));
        AnchorPane sourceLayout = fxmlLoader.load();
        Scene scene = new Scene(sourceLayout, 600.0, 610);
        stage.setTitle("NBP In Router");
        stage.setScene(scene);
        stage.show();
    }
}
