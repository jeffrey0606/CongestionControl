package com.example.congestioncontrol.Info;

import com.jfoenix.controls.JFXListCell;

public class InfoCellCard<T>  extends JFXListCell<T> {
    @Override
    public void updateItem(T infoModel, boolean empty) {
        super.updateItem(infoModel, empty);
        if(infoModel != null || !empty) {
            if (infoModel.getClass().getName().contains("MessageCardModel")) {
                MessageCard messageCard = new MessageCard();
                messageCard.updateItem((MessageCardModel) infoModel, this);
            } else {
                PacketCard packetCard = new PacketCard();
                packetCard.updateItem((PacketCardModel) infoModel, this);
            }
        }
    }
}
