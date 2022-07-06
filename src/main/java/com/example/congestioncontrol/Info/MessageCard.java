package com.example.congestioncontrol.Info;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MessageCard {

    @FXML
    public Label hostNameText;
    @FXML
    public Label timeText;
    @FXML
    public Label hostAddressText;
    @FXML
    private BorderPane cells;
    @FXML
    private Label feedback;

    public MessageCard() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InfoModel.class.getResource("message-card.fxml"));
            fxmlLoader.setController(this);
            cells = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateItem(MessageCardModel message, JFXListCell cell) {
        cell.setText(null);
        cell.setBorder(null);
        cell.setStyle(null);
        cell.setBackground(null);
        cell.setPadding(new Insets(0,0,0,0));
        hostAddressText.setText(message.hostAddress);
        timeText.setText(message.time);
        hostNameText.setText(message.hostName);
        feedback.setVisible(message.isFeedback);
        cell.setGraphic(cells);
        System.out.println("Message Card: " + message.message);
    }
}