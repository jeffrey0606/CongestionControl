package com.example.congestioncontrol.Info;

import com.example.congestioncontrol.NBPInRouter.Tim;

public class PacketCardModel extends InfoModel {
    public String source;
    public String message;
    public String destinationIp;

    public String time;

    public PacketCardModel(String source, String message, String destinationIp) {
        this.destinationIp = destinationIp;
        this.message = message;
        this.source = source;
        this.time = new Tim().calculateTime();
    }
}
