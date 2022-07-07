package com.example.congestioncontrol;

import com.example.congestioncontrol.NBPInRouter.RateControlMonitor;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Toaster {

    private JFXSnackbar snackbar;



    public void lazyInit() {
        if (snackbar == null) {
            snackbar = new JFXSnackbar(RateControlMonitor.root);
            snackbar.setPrefWidth(600);

//            snackbar.setLayoutX(RateControlMonitor.root.getLayoutX() + (RateControlMonitor.root.getMaxWidth() * 0.5));
//            snackbar.setLayoutY(RateControlMonitor.root.getLayoutY() + (RateControlMonitor.root.getMaxHeight() * 0.5));
        }
    }


    public void showToast(String message) {

        Platform.runLater(() -> {
            lazyInit();
            snackbar.show(message, 1000);
        });
    }

    public void showToast(String message, String action, EventHandler<ActionEvent> eventHandler) {
//        Platform.runLater(() -> {
//            lazyInit();
//            snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(message, action, eventHandler), Duration.seconds(5), null));
//        });
    }
}