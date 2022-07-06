package com.example.congestioncontrol.Info;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PacketCard {

    @FXML
    public Label sourceText;
    @FXML
    public Label messageText;
    @FXML
    public Label destinationText;
    @FXML
    public Label timeText;
    @FXML
    private BorderPane cells;

    public PacketCard() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InfoModel.class.getResource("packet-card.fxml"));
            fxmlLoader.setController(this);
            cells = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateItem(PacketCardModel packet, JFXListCell cell) {
        cell.setText(null);
        cell.setBorder(null);
        cell.setStyle(null);
        cell.setBackground(null);
        cell.setPadding(new Insets(0,0,0,0));
        sourceText.setText(packet.source);
        messageText.setText(packet.message);
        destinationText.setText(packet.destinationIp);
        timeText.setText(packet.time);
        cell.setGraphic(cells);
        System.out.println("Packet Card: " + packet.source);
    }
}